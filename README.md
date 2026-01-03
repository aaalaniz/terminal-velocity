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

## 🚀 Getting Started

### Prerequisites

*   JDK 11 or higher
*   Kotlin 2.3+

### Building the Project

To build the application and create the distribution files, run:

```bash
./gradlew installDist
```

### Running the Game

Once built, you can launch the game executable directly:

```bash
./build/install/terminal-velocity/bin/terminal-velocity
```

## License

[MIT](./LICENSE)