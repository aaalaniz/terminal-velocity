# Plan: Core Typing Game Mechanics

## Phase 1: Game State & Logic (Presenter)
This phase focuses on the business logic of the game, implemented as a Circuit Presenter. No UI will be built in this phase.

- [x] Task: Create `GameEvent` and `GameState` sealed classes/interfaces to define the contract. 812adc9
- [x] Task: Implement `WordRepository` interface and a simple in-memory implementation with a list of words. 85f4d3c
- [x] Task: Implement `GamePresenter` logic: c49206b
    - Handle `GameStarted`: Set state to PLAYING, pick initial word.
    - Handle `LetterTyped`: Check against `currentWord`.
        - If correct: append to `userInput`.
        - If word complete: increment score, clear `userInput`, pick new `currentWord`.
        - If incorrect: Handle error state (optional for MVP, or just ignore).
    - Handle `GameReset`: Reset score and state.
- [ ] Task: Write comprehensive unit tests for `GamePresenter` covering all state transitions and edge cases.
- [ ] Task: Conductor - User Manual Verification 'Phase 1: Game State & Logic (Presenter)' (Protocol in workflow.md)

## Phase 2: Terminal UI (Mosaic) & Integration
This phase builds the visual representation and connects it to the logic.

- [ ] Task: Create a basic `GameScreen` Circuit screen definition.
- [ ] Task: Implement `GameUi` Composable using Mosaic.
    - Display `score`.
    - Render `currentWord` and `userInput` with visual distinction (e.g., Green for typed, Gray for remaining).
- [ ] Task: Wire up keyboard input in `GameUi` to emit `LetterTyped` events.
- [ ] Task: Connect `GamePresenter` and `GameUi` in the main application graph.
- [ ] Task: Conductor - User Manual Verification 'Phase 2: Terminal UI (Mosaic) & Integration' (Protocol in workflow.md)
