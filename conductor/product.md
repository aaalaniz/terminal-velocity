# Initial Concept
This is a casual game intended to be primarily for educational purposes. The audience is primarily developers interested in seeing how to build a terminal app using circuit, mosaic, and metro. I think this could be used to improve typing skills, but I do not intend to make this a widely used game to learn how to type. You marked the project as brownfield, but the actual mechanics of the game have not actually been built. I'd say the technology choices are set, but the actual game play is very green

# Product Guide - Terminal Velocity

## Vision
Terminal Velocity is a casual typing game built as an educational showcase for modern Kotlin development in the terminal. It serves as a practical example of how to combine **Circuit**, **Mosaic**, and **Metro** to create a robust, maintainable, and visually appealing CLI application.

## Target Audience
The primary audience consists of **developers** interested in:
- Exploring terminal UI development with Jetpack Compose (via Mosaic).
- Implementing Unidirectional Data Flow (UDF) and structured navigation (via Circuit).
- Managing dependency injection in a non-Android JVM environment (via Metro).

While the game improves typing skills, its success is measured by the clarity and quality of its architectural demonstration rather than its popularity as a commercial typing tool.

## Core Educational Objectives
The project emphasizes three main pillars:
1.  **Circuit (Navigation & UDF):** Demonstrating clear separation between business logic (Presenters) and UI (Ui) using a single source of truth for state.
2.  **Mosaic (Declarative UI):** Showcasing how to build responsive and interactive terminal layouts using declarative components.
3.  **Metro (Dependency Injection):** Illustrating efficient graph management and scoping for a standalone JVM application.

## Initial Feature Scope (MVP)
To demonstrate these patterns effectively, the initial focus is on:
- **Core Game Mechanics:** A real-time typing loop that handles user input, tracks progress, and provides immediate visual feedback in the terminal.
