# Plan: Visual Feedback Refinement

## Phase 1: Game State & Events (Presenter)
Refine the game state to support the visual "flash" effect for errors.

- [x] Task: Update `GameEvent` to include a `ResetError` event or similar for clearing the flash state. 93a8bb4
- [x] Task: Update `GameState.State` to include an `isError` or `flashError` boolean flag. e85b614
- [x] Task: Write failing tests in `GamePresenterTest` for the error flash behavior (flash on incorrect key, auto-reset after delay if needed, or manual reset). 3ec14cc
- [x] Task: Implement error state logic in `GameScreenPresenter`. 284e37f
- [ ] Task: Conductor - User Manual Verification 'Phase 1: Game State & Events (Presenter)' (Protocol in workflow.md)

## Phase 2: UI Styling & Visuals (Mosaic)
Update the UI to use dimmed/bright text and implement the red flash effect.

- [ ] Task: Refactor `GameScreenUi` to render correct characters as "bright" and untyped characters as "dimmed".
- [ ] Task: Implement the "flash red" effect in `GameScreenUi` when the error flag is set in the state.
- [ ] Task: Verify the visual transitions manually and ensure they align with the product guidelines (Clean & Minimalist).
- [ ] Task: Conductor - User Manual Verification 'Phase 2: UI Styling & Visuals (Mosaic)' (Protocol in workflow.md)
