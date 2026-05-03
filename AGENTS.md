SolonCode：基于 Solon AI Harness 的代码智能体 CLI 与桌面端项目

# 项目结构

此文件夹为项目根目录。使用 Gradle 管理 JVM 子工程，并包含 Tauri 桌面端工程。

# 项目概述

- **CLI**: 位于 `./soloncode-cli` 文件夹。Solon, Gradle, Java, JVM 25, GraalVM Native Image。
  - 除非用户要求，每次更改此子工程后应当执行 `./gradlew :soloncode-cli:compileJava` 以确保编译通过。
  - 如果更改发布包、native-image 配置或运行时启动逻辑，应当优先执行 `JAVA_HOME=/Users/yang/Library/Java/JavaVirtualMachines/graalvm-jdk-25/Contents/Home ./gradlew :soloncode-cli:nativeCompile` 验证原生构建。
- **桌面端**: 位于 `./soloncode-desktop` 文件夹。React, TypeScript, Vite, Tauri。
  - 除非用户要求，每次更改此子工程后应当执行 `npm run build` 以确保前端编译通过。
- **示例扩展**: 位于 `./examples/extension_demo` 文件夹，用于演示 SolonCode 扩展能力。

# 规范

- **创建 release note**: 回顾与上个 tag 相比的变更并撰写更新日志。具体参阅 `./skills/release_note_creator/SKILL.md`
- **发布包内容**: CLI 发布包以 `soloncode-cli/release` 为基础，包含 `config.yml`、`AGENTS.md`、`skills/` 与安装脚本。
