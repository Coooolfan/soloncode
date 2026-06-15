<div align="center">
<h1>SolonCode</h1>
<p>基于 <a href="https://github.com/opensolon/solon-ai">Solon AI</a> 与 Java 实现的开源编码智能体（支持 Java8 到 Java26 环境启动）</p>
<p>Fork 版本：0.1.3 · 已跟踪上游基线：v2026.6.16</p>
<img height="260" src="SHOW.png" />
<img height="260" src="SHOW2.png" />
</div>

<div align="center">

[English](../README.md) | [日本語](README.ja.md) | [한국어](README.ko.md) | [Deutsch](README.de.md) | [Français](README.fr.md) | [Español](README.es.md) | [Italiano](README.it.md)

[Русский](README.ru.md) | [العربية](README.ar.md) | [Português (BR)](README.br.md) | [ไทย](README.th.md) | [Tiếng Việt](README.vi.md) | [Polski](README.pl.md)

[বাংলা](README.bn.md) | [Bosanski](README.bs.md) | [Dansk](README.da.md) | [Ελληνικά](README.gr.md) | [Norsk](README.no.md) | [Türkçe](README.tr.md) | [Українська](README.uk.md)

</div>

## 关于此 Fork

此 fork 跟踪上游 [OpenSolon/SolonCode](https://github.com/opensolon/soloncode)，同时保留以 Gradle 与 GraalVM Native Image 为核心的构建和运行时方向。

与上游的主要区别：

* **Gradle 优先的构建体系**：JVM 子模块和示例扩展使用 Gradle 9.4 维护，以 JVM 25 作为编译、运行时目标。
* **GraalVM Native Image 支持**：CLI 维护 native-image 构建配置、反射/资源元数据。
* **发布包与 CI 自动化**：CLI 发布包以 `soloncode-cli/release` 为基础，包含 `config.yml`、`AGENTS.md`、`skills/` 与安装脚本。
* **静态 Web UI 打包**：CLI Web UI 以静态资源打包，不依赖 Thymeleaf。
* **移除 Extension 加载**：本 fork 有意禁用 `solon.extend` 用户扩展加载路径。
* **Fork 独立版本线**：本分支保留 `0.1.3` 版本线，并禁用上游更新检查。
* **文档布局调整**：英文主 README 放在仓库根目录，多语言 README 与截图保留在 `docs/` 下。

我们接受 PR 和 Issues。但此 fork 不保证修复、不承诺持续维护。

## 安装与配置

安装：

```bash
# Mac / Linux / Harmony PC:
curl -fsSL https://solon.noear.org/soloncode/setup.sh | bash

# Windows (PowerShell):
irm https://solon.noear.org/soloncode/setup.ps1 | iex
```

修改配置：

* 安装后的目录：`~/soloncode/bin/`
* 找到 `~/soloncode/config.yml` 配置文件，主要修改 `models` 配置
* `models` 配置项可参考：[《模型配置与请求选项》](https://solon.noear.org/article/1087)

## 运行

在任意工作区目录运行 `soloncode` 进入 CLI 交互，或运行 `soloncode web 0` 进入 Web 交互。

```bash
demo@MacBook-Pro ~ % soloncode
SolonCode 0.1.3 PID-74080 Model:deepseek-v4-flash
/path/demo
Tips: (esc) interrupt | /(tab) command | $(tab) skill | @(tab) agent

User
>
```

```bash
demo@MacBook-Pro ~ % soloncode web 0
SolonCode 0.1.3 PID-73617 Model:deepseek-v4-flash
/path/demo
2026-05-20 09:35
Web interface: http://localhost:50488/
```

## 文档

更多配置说明请查看 [官方文档](https://solon.noear.org/article/soloncode)。

## 参与贡献

如有兴趣贡献代码，请在提交 PR 前阅读 [贡献指南](https://solon.noear.org/article/623)。

## 常见问题：和 Claude Code 有什么不同？

功能上很相似，关键差异：

* 采用 Java 实现，100% 开源。兼容毕昇 JDK与鸿蒙 PC。
* 纯中文提示词驱动与构建。
* 不绑定特定模型提供商，按需配置模型。
* 支持终端 CLI、浏览器 Web UI、桌面 IDE UI、Web 与 ACP 远程通讯。
