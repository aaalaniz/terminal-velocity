# Terminal Velocity - Agent Context

## Project Overview
**Terminal Velocity** is a casual typing game played in the terminal. It is a Kotlin JVM application designed to run on macOS, Linux, and Windows.

## Tech Stack
- **Language:** Kotlin (JVM)
- **UI Framework:** [Mosaic](https://github.com/JakeWharton/mosaic) (Jetpack Compose for Terminal)
- **Architecture:** [Circuit](https://github.com/slackhq/circuit) (Unidirectional Data Flow, Navigation)
- **Dependency Injection:** [Metro](https://github.com/ZacSweers/metro)

## Development Workflow

### Build & Run
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

## Architecture & Conventions

### UI (Mosaic)
- All UI is built using Composable functions.
- The entry point is `runMosaic` in `main.kt`.
- UI components reside in `src/main/java/xyz/alaniz/aaron/ui/`.
- **Pattern:** Use a `Presenter` to manage state and a `Ui` composable to render it (Circuit pattern).

### Navigation (Circuit)
- Navigation is handled by Circuit.
- Screens are defined as data objects/classes implementing `Screen`.
- The `CircuitApp` composable sets up the `CircuitCompositionLocals` and `NavigableCircuitContent`.

### Dependency Injection (Metro)
- The dependency graph is managed by Metro.
- Use `@AppScope` for singleton dependencies.
- The graph is created in `main.kt` via `createGraphFactory`.

## File Structure Key Points
- `src/main/java/xyz/alaniz/aaron/main.kt`: Application entry point.
- `src/main/java/xyz/alaniz/aaron/ui/`: Contains feature-based UI packages (e.g., `game`, `settings`, `title`).
- `build.gradle.kts`: Project build configuration. Dependencies are managed via version catalog (`gradle/libs.versions.toml`).
