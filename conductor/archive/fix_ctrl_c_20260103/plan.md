# Plan: Fix Ctrl+C Exit Behavior

## Phase 1: Input Handling (UI) [checkpoint: ceb0e74]
Ensure `Ctrl+C` is detected and results in application exit.

- [x] Task: Update `GameScreenUi` keyboard event handler to detect `Ctrl+C`. df4a83f
- [x] Task: Implement immediate application exit logic (e.g., `exitProcess(0)` or similar) when `Ctrl+C` is detected. df4a83f
- [x] Task: Verify the fix manually by launching the app and pressing `Ctrl+C` in the game screen. df4a83f
- [x] Task: Conductor - User Manual Verification 'Phase 1: Input Handling (UI)' (Protocol in workflow.md) ceb0e74
