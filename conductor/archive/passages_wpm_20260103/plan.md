# Plan: Multi-Line Passages and WPM Support

## Phase 1: Data & Domain Logic (Presenter)
Refactor the game logic to support passages and metrics.

- [x] Task: Update `WordRepository` to support full passages (e.g., `getPassage(): List<String>`). 95663de
- [x] Task: Implement WPM and Accuracy calculation logic in `GameScreenPresenter`.
- [x] Task: Update `GameState.State` to include `wpm`, `accuracy`, and `elapsedTime`.
- [x] Task: Update `GamePresenter` to handle passage traversal (sequentially iterating through lines/words).
- [x] Task: Write failing tests for WPM calculation and passage completion logic.
- [x] Task: Implement the core passage logic in the presenter.
- [ ] Task: Conductor - User Manual Verification 'Phase 1: Data & Domain Logic (Presenter)' (Protocol in workflow.md)

## Phase 2: UI (Mosaic) & Scrolling
Update the UI to render the scrolling passage and real-time metrics.

- [x] Task: Refactor `GameScreenUi` to render a window of lines (5-7 lines) from the passage.
- [x] Task: Implement the "scrolling" visual effect (advancing the window as lines are completed).
- [x] Task: Display real-time WPM on the game screen.
- [x] Task: Create and implement the "Summary Screen" (Game Over state) showing final metrics.
- [x] Task: Verify the full passage typing flow manually.
- [x] Task: Conductor - User Manual Verification 'Phase 2: UI (Mosaic) & Scrolling' (Protocol in workflow.md)
