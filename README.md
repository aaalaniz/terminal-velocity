# Terminal Velocity

A casual typing game played entirely within your terminal.

## 🚧 Work in Progress

> [!NOTE]
> This project is currently in early development. Features and gameplay mechanics are actively being built and refined.

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


## License

[MIT](./LICENSE)