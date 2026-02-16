# Terminal Velocity

A casual typing game played entirely within your terminal.

## 🚧 Work in Progress

> [!NOTE]
> This project is currently in early development. Features and gameplay mechanics are actively being built and refined.

## 🚀 Getting Started

To play the game, you can install the latest version directly from your terminal:

```bash
curl -fsSL https://github.com/aaalaniz/terminal-velocity/releases/latest/download/install.sh | bash
```

### Manual Installation

You can also run the install script directly from the repository:

```bash
./install.sh
```

> **Note:** The `install.sh` script downloads and installs the latest *released* version from GitHub, not the local source code.

Alternatively, you can download standalone executables for Linux, macOS, and Windows from the [Releases](https://github.com/aaalaniz/terminal-velocity/releases) page.

## 🛠️ Built With

This project is built in Kotlin using: 

*   **[Mosaic](https://github.com/JakeWharton/mosaic):** For building the terminal UI with Jetpack Compose.
*   **[Circuit](https://github.com/slackhq/circuit):** For navigation and unidirectional data flow architecture.
*   **[Metro](https://github.com/ZacSweers/metro):** For dependency injection.

## Development Workflow

- **Build & Install Distribution:**
  ```bash
  ./gradlew installDist
  ```
- **Run Application:**
  ```bash
  ./build/install/terminal-velocity/bin/terminal-velocity
  ```
- **Run Tests:**
  ```bash
  ./gradlew test
  ```
  This runs all unit and integration tests (including Mosaic integration tests).
- **Check Formatting:**
  ```bash
  ./gradlew ktfmtCheck
  ```
- **Apply Formatting:**
  ```bash
  ./gradlew ktfmtFormat
  ```
- **Install Pre-commit Hook:**
  ```bash
  ./scripts/install-git-hooks.sh
  ```
- **Continuous Integration:** See [CI.md](./CI.md) for details on our automated pipeline.

## 🛠️ Contributing

To contribute to Terminal Velocity, ensure you have the following setup:

- **Gemini CLI Conductor Extension:** This project uses the [Conductor extension](https://github.com/gemini-cli-extensions/conductor) for track and task management. Install it via:
  ```bash
  gemini extension install conductor
  ```
- **Linear Integration:** We use Linear for visual project management. For local agents to assist with issue syncing, configure the Linear MCP in your `.gemini/settings.json`.

## License

[MIT](./LICENSE)