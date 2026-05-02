# Plan 2 — Native Image 迁移实施计划

## 背景

`feat/native` 分支的目标是把 `soloncode-cli` 用 GraalVM Native Image 编译成单文件二进制，启动从秒级降到百毫秒级，分发不再依赖 JDK。本文档落定本期范围内的三条约束、列出关键改造点，以及在动手前已识别出的潜在阻塞点。

## 一、目标与非目标

**目标**

- CLI 主路径以 native 二进制运行：默认交互、`run`、`web`、`acp`、`serve`
- 当前开发机平台（macOS arm64）能产出可用二进制并通过端到端验证

**非目标（本期不做）**

- 跨平台交叉构建与多架构产物
- 用户扩展（`solon.extend`）的任何兼容
- 服务端模板渲染
- macOS 代码签名 / notarization
- `soloncode-desktop`（Tauri）侧改动

## 二、三条已定约束

### 1. Thymeleaf + OGNL 视图：完全静态化

去掉服务端模板引擎。`chat.html` 仅有 4 个动态变量（`appTitle` / `appVersion` / `workspace` / `workname`），数据量极小，转为前端启动时 fetch 一个 JSON 接口即可。

### 2. `solon.extend` 用户扩展：直接弃用

native 下不可能加载未编译的外部 jar；本期不做"hybrid 子进程 JVM"之类的折中方案，直接移除该机制对应的入口代码，文档中明确 native 版本不支持运行期扩展。

### 3. 跨平台分发：本期不做

仅产出当前开发机平台的二进制。GitHub Actions 矩阵（macOS / Linux / Windows × x64 / arm64）作为后续独立任务推进。

## 三、关键改造点

### 视图静态化

| 操作 | 路径 |
|---|---|
| 删除依赖 | `soloncode-cli/build.gradle.kts:18` 的 `solon-view-thymeleaf` |
| 模板移位 | `src/main/resources/templates/chat.html` → `src/main/resources/static/index.html`；移除 `xmlns:th` 命名空间和所有 `${...}` 占位 |
| 删除控制器方法 | `portal/WebController.java:182-191` 的 `chat()` |
| 入口改为 302 重定向 | `solon-web-staticfiles` 不会把 `GET /` 当索引页，需保留一个最小 `@Mapping("/")` 控制器方法做 `ctx.redirect("/index.html")`（原生友好，无反射） |
| 新增 JSON meta 接口 | `GET /chat/meta` 返回 `{appTitle, appVersion, workspace, workname}`；前端启动后 fetch 一次回填页面标题 / 侧栏 |

### 弃用 `solon.extend`

| 操作 | 路径 |
|---|---|
| 删除系统属性设置 | `App.java:42` 的 `System.setProperty("solon.extend", ...)` |
| 字段清理 | `AgentProperties` 中 `userExtensions` 相关字段；如无其他调用点一并删除 |
| 文档同步 | README 与 `app.yml` 注释里出现的 extension 配置项都标注为 native 不支持 |

### Native 构建接入

| 操作 | 路径 |
|---|---|
| 根 build 加插件 | `build.gradle.kts` 引入 `org.graalvm.buildtools.native` |
| 子项目配置 | `soloncode-cli/build.gradle.kts` 增加 `graalvmNative { binaries { main { ... } } }`；`mainClass` 复用 `org.noear.solon.codecli.App` |
| 接入 Solon AOT | 加 `solon-aot` 处理器，扫描 `@Configuration` `@Inject` `@Mapping` `@Component`，自动产出 `META-INF/native-image/**` |
| 项目自身兜底 reflect-config | `src/main/resources/META-INF/native-image/org.noear/soloncode-cli/reflect-config.json`，至少登记：<br>· `AgentProperties` 及父类 `HarnessProperties` 全部字段（YAML bind 反射目标）<br>· `ChatConfig`（`/chat/models/add` JSON 反序列化目标）<br>· `ModelInfo`（`/chat/models/fetch` JSON 反序列化目标） |

## 四、提前识别的潜在阻塞点

| # | 风险点 | 现状判断 | 处置策略 |
|---|---|---|---|
| A | Solon AOT 处理器未接入 | 当前 build 无 AOT 步骤；项目自身的 Bean 元数据完全未生成 | 必须接入 `solon-aot` Gradle 任务，否则 native 下 Bean 装配全部失败 |
| B | YAML 配置绑定（`App.java:58` 的 `bindTo(c)`） | Solon 通过反射读写 `AgentProperties` Lombok 生成的 getter/setter | 在 reflect-config 中登记该 POJO 全部字段；snakeyaml 已被 Solon 处理 |
| C | JSON 取值 `ONode.ofJson(...).get(...)` | 全部走"取键 → getString"，不走 POJO 绑定 | 反射安全，无需特殊处理 |
| D | JLine 终端 backend | jar 内同时打包 ffm / jansi / jni / jna 四种 | native 仅保留 ffm（JDK 25 原生支持），其余 backend 通过 `exclude` 移除 |
| E | logback 配置 | 已使用，Solon 已注入 reflect-config | 配置文件用 `logback.xml`（禁止 groovy 配置），路径 `src/main/resources/logback.xml` |
| F | `solon-ai-harness` / `solon-ai-acp` / `solon-ai-skill-memory` | 5 个 LLM dialect 已自带 reflect-config；ACP / Skill 未观察到独立 native 配置 | PoC 阶段重点验证；如缺，向 Solon 上游反馈或本地补齐 |
| G | `solon-scheduling-simple` | `@EnableScheduling` 装饰 App；运行期扫描 `@Scheduled` | 接入 Solon AOT 后由其处理；PoC 验证 `LoopScheduler` 仍可工作 |
| H | `solon-web-sse`（Reactor Flux） | reactor-core 已有 native 配置 | PoC 验证 `/chat/events`、`/chat/input` 流式响应 |
| I | WebSocket（`/ws` `/acp`） | smart-http server 已带 native 配置 | PoC 验证 ws upgrade 在 native 下正常 |
| J | 进程启动浏览器（`Configurator.java:189-195`） | 使用 `ProcessBuilder` 调 `open` / `xdg-open` / `cmd` | native 下 `ProcessBuilder` 原生可用，无需特殊处理 |
| K | 资源目录 `static/` `templates/` | 通过 `solon-web-staticfiles` 提供 | Solon AOT 一般会自动产出 resource-config.json；PoC 阶段验证、必要时手补 |
| L | 打包任务 `releaseArchive`（`soloncode-cli/build.gradle.kts:59-81`） | 当前只打包 shadowJar | 新增 native binary 的打包路径；JVM 模式 shadowJar 保留不动 |

## 五、实施分阶段

**阶段 0 — 文档**：本文档（已完成）

**阶段 1 — 视图静态化**
- 删 thymeleaf 依赖、`chat.html` 改为静态资源、新增 `/chat/meta`
- JVM 模式下浏览器跑通 web 模式作为回归基线

**阶段 2 — 弃用 `solon.extend`**
- 删 `App.java:42` 的系统属性
- 清理 `AgentProperties` 对应字段及文档说明

**阶段 3 — Native 构建接入**
- 加 graalvm 插件、接入 solon-aot
- 先跑 `nativeRun`（开发模式）打通装配
- 再跑 `nativeCompile` 出二进制

**阶段 4 — 端到端验证**
- `./soloncode` 默认 CLI 交互（jline 终端）
- `./soloncode run "你好"` 单次任务模式
- `./soloncode web` 打开浏览器 → 发消息 → SSE 流回
- `./soloncode acp` stdio 协议握手
- `./soloncode serve` 同时起 web + acp + ws

## 六、如何验证

```bash
# JVM 模式回归（确保改造未破坏现有功能）
./gradlew :soloncode-cli:run --args="run '你好'"
./gradlew :soloncode-cli:run --args="web"

# Native 构建
./gradlew :soloncode-cli:nativeCompile
./soloncode-cli/build/native/nativeCompile/soloncode-cli run "你好"
./soloncode-cli/build/native/nativeCompile/soloncode-cli web

# 启动时间对比
time java -jar soloncode-cli/build/libs/soloncode-cli.jar run "hi"
time ./soloncode-cli/build/native/nativeCompile/soloncode-cli run "hi"
```

**通过标准**

- CLI 单次任务在 native 下冷启动 < 200ms
- Web 模式可在浏览器完整对话（含 SSE 流式渲染、附件上传、模型切换）
- ACP stdio 握手可被桌面端 / 测试客户端识别

## 七、不在本期范围（防 scope creep）

- GitHub Actions 多平台 matrix（macOS / Linux / Windows × x64 / arm64）
- Native 二进制的 macOS 代码签名 / notarization
- 用户扩展机制的 "hybrid 子进程 JVM" 方案
- `soloncode-desktop`（Tauri）侧的任何改动
