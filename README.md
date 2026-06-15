<div align="center">
<h1>SolonCode</h1>
<p>An open-source coding agent built with <a href="https://github.com/opensolon/solon-ai">Solon AI</a> and Java (supports Java8 to Java26 runtime environments)</p>
<p>Fork Version: 0.1.3 · Upstream baseline: v2026.6.16</p>
<img height="260" src="docs/SHOW.png" />
<img height="260" src="docs/SHOW2.png" />
</div>

<div align="center">

[中文](docs/README.zh.md) | [日本語](docs/README.ja.md) | [한국어](docs/README.ko.md) | [Deutsch](docs/README.de.md) | [Français](docs/README.fr.md) | [Español](docs/README.es.md) | [Italiano](docs/README.it.md)

[Русский](docs/README.ru.md) | [العربية](docs/README.ar.md) | [Português (BR)](docs/README.br.md) | [ไทย](docs/README.th.md) | [Tiếng Việt](docs/README.vi.md) | [Polski](docs/README.pl.md)

[বাংলা](docs/README.bn.md) | [Bosanski](docs/README.bs.md) | [Dansk](docs/README.da.md) | [Ελληνικά](docs/README.gr.md) | [Norsk](docs/README.no.md) | [Türkçe](docs/README.tr.md) | [Українська](docs/README.uk.md)

</div>

## About This Fork

This fork tracks the upstream [OpenSolon/SolonCode](https://github.com/opensolon/soloncode) project while keeping a Gradle and GraalVM Native Image focused build/runtime direction.

Main differences from upstream:

* **Gradle-first build**: JVM modules and the extension demo are maintained with Gradle 9.4, using JVM 25 as the compile-time and runtime target.
* **GraalVM Native Image support**: native-image build wiring plus reflection/resource metadata are maintained for the CLI.
* **Release packaging and CI**: release archives are based on `soloncode-cli/release` and include `config.yml`, `AGENTS.md`, `skills/`, and install scripts.
* **Static Web UI packaging**: the CLI Web UI is packaged as static resources and does not depend on Thymeleaf.
* **Extension loading removed**: the `solon.extend` user extension loading path is intentionally disabled in this fork.
* **Fork-specific versioning**: this branch keeps its `0.1.3` version line and disables upstream update checks.
* **Documentation layout**: the English README stays at the repository root, while localized READMEs and screenshots are kept under `docs/`.

PRs and Issues are accepted, but this fork does not guarantee fixes or commit to ongoing maintenance.

## Installation and Configuration

Installation:

```bash
# Mac / Linux / Harmony PC:
curl -fsSL https://solon.noear.org/soloncode/setup.sh | bash

# Windows (PowerShell):
irm https://solon.noear.org/soloncode/setup.ps1 | iex
```

Configuration:

* Installation directory: `~/soloncode/bin/`
* Locate `~/soloncode/config.yml` and configure `models`
* For model options, see [Model Configuration and Request Options](https://solon.noear.org/article/1087)

## Running

Run `soloncode` for CLI interactive mode or `soloncode web 0` for Web interactive mode from any workspace directory.

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

## Documentation

For more configuration details, visit the [Official Documentation](https://solon.noear.org/article/soloncode).

## Contributing

If you're interested in contributing code, read the [Contributing Docs](https://solon.noear.org/article/623) before submitting a PR.

## FAQ: What's the difference from Claude Code?

They are functionally similar, with key differences:

* Built with Java, 100% open-source. Compatible with BiSheng JDK (Huawei) and Harmony PC.
* Pure Chinese prompt-driven development and construction.
* Provider-agnostic. Configure models as needed.
* Supports terminal CLI, browser Web UI, desktop IDE UI, Web, and ACP remote communication.
