# Tech Stack - Terminal Velocity

## Core Technologies
- **Language:** [Kotlin](https://kotlinlang.org/) (JVM) - Chosen for its modern features, safety, and excellent interoperability with the Java ecosystem.
- **UI Framework:** [Mosaic](https://github.com/JakeWharton/mosaic) - A Jetpack Compose-based library for building powerful and interactive terminal user interfaces.
- **Architecture:** [Circuit](https://github.com/slackhq/circuit) - A lightweight framework for building navigation and unidirectional data flow (UDF) in Kotlin applications.
- **Dependency Injection:** [Metro](https://github.com/ZacSweers/metro) - A compile-time dependency injection library designed for speed and simplicity in Kotlin JVM projects.

## Tooling & Infrastructure
- **Build System:** Gradle (Kotlin DSL) - Provides a flexible and powerful build automation environment.
- **Static Analysis:** Kotlin Symbol Processing (KSP) - Used for efficient code generation, specifically for Circuit and Metro.
- **Distribution:** Gradle `application` plugin - Facilitates the creation of standalone executable distributions for macOS, Linux, and Windows.
- **Testing:** [JUnit 5](https://junit.org/junit5/) and [Kotlin Test](https://kotlinlang.org/api/latest/kotlin.test/) - For unit and integration testing.
