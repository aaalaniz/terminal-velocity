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
- **Policy:** For objects controlled by the app, contribute the instance directly from the class declaration using `@ContributesBinding`. Only use modules for objects not controlled by the app (e.g. `IO dispatcher`).

### Testing (Mosaic & Circuit)
- **Unit Tests:** Use standard [Kotlin Test](https://kotlinlang.org/api/latest/kotlin.test/) for assertions.
- **Presenter Tests:** Use `circuit-test` to test Presenter logic in isolation.
- **Integration Tests:** Use `mosaic-testing` for end-to-end UI verification.
    - `runMosaicTest`: Sets up the test environment.
    - `awaitSnapshot()`: Verifies rendered output.
    - `sendKeyEvent()`: Simulates user interaction.

## File Structure Key Points
- `src/main/java/xyz/alaniz/aaron/main.kt`: Application entry point.
- `src/main/java/xyz/alaniz/aaron/ui/`: Contains feature-based UI packages (e.g., `game`, `settings`, `title`).
- `build.gradle.kts`: Project build configuration. Dependencies are managed via version catalog (`gradle/libs.versions.toml`).

## Content Management (Passages)

The game's typing passages are stored as external resources to support lazy loading and easy expansion.

- **Location:** `src/main/resources/passages/`
- **Format:** Pure Markdown (`.md`) files containing only the raw text content. No front matter is used.
- **Index:** `src/main/resources/passages.index`
    - This is a CSV file that maps filenames to metadata tags.
    - **Format:** `filename.md, tag1, tag2, ...`
    - **Example:** `code_kotlin_1.md, code, kotlin`
    - **Tags:**
        - `prose`: For standard text passages.
        - `code`: For code snippets.
        - `[language]`: (e.g., `kotlin`, `java`) Required if the `code` tag is present.

### How to Add a New Passage
1.  **Create the File:** Add a new `.md` file in `src/main/resources/passages/` containing the text or code snippet.
2.  **Update the Index:** Add a new line to `src/main/resources/passages.index` with the relative filename and appropriate tags.
    - *Example:* `my_new_passage.md, prose, education`

### Maintenance
- **Lazy Loading:** The `MarkdownWordRepository` reads the index file on the first request. Actual content files are only loaded from disk when selected for gameplay.
- **Filtering:** The repository uses the tags in the index to filter candidates based on user settings (e.g., "Only Code Snippets").

## Code Style & Formatting
- This project uses `ktfmt` for code formatting.
- **Before submitting changes:**
  - Run `./gradlew ktfmtFormat` to ensure all code is properly formatted.
  - Verify with `./gradlew ktfmtCheck`.

## Workflow & Concurrency
- **Sync with Upstream:**
  - Run `scripts/sync-upstream.sh` frequently (e.g., before starting new work or before submitting) to ensure your working branch is up-to-date with `origin/main`.
  - **Important:** If the script fails, it indicates a merge conflict. You must **stop working immediately** and notify the user that intervention is required. Do not attempt to resolve complex git conflicts autonomously unless explicitly instructed.
