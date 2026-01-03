# Plan: Core Typing Game Mechanics

## Phase 1: Game State & Logic (Presenter) [checkpoint: facb305]
This phase focuses on the business logic of the game, implemented as a Circuit Presenter. No UI will be built in this phase.

- [x] Task: Create `GameEvent` and `GameState` sealed classes/interfaces to define the contract. 812adc9
- [x] Task: Implement `WordRepository` interface and a simple in-memory implementation with a list of words. 85f4d3c
- [x] Task: Implement `GamePresenter` logic: c49206b
- [x] Task: Write comprehensive unit tests for `GamePresenter` covering all state transitions and edge cases. c49206b
- [x] Task: Conductor - User Manual Verification 'Phase 1: Game State & Logic (Presenter)' (Protocol in workflow.md) facb305

## Phase 2: Terminal UI (Mosaic) & Integration
This phase builds the visual representation and connects it to the logic.

- [x] Task: Create a basic `GameScreen` Circuit screen definition. bbf13e0
- [x] Task: Implement `GameUi` Composable using Mosaic. d372143
    - Display `score`.
    - Render `currentWord` and `userInput` with visual distinction (e.g., Green for typed, Gray for remaining).
- [ ] Task: Wire up keyboard input in `GameUi` to emit `LetterTyped` events.
- [ ] Task: Connect `GamePresenter` and `GameUi` in the main application graph.
- [ ] Task: Conductor - User Manual Verification 'Phase 2: Terminal UI (Mosaic) & Integration' (Protocol in workflow.md)
