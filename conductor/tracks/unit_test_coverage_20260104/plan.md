# Plan: Improve Unit Test Coverage (80%)

## Phase 1: Foundation & Data Layer [checkpoint: 3424284]
This phase focuses on ensuring the core utilities and data management logic are robustly tested and removing non-meaningful tests.

- [x] Task: Audit and Cleanup existing tests for `data` and `ui.foundation` packages (Removed silly tests)
- [x] Task: Implement unit tests for `KeyEvents.kt` (Red/Green/Refactor)
- [x] Task: Implement unit tests for `MosaicNavDecoration.kt` (Reviewed: logic is trivial/UI-bound, inheritance test removed as silly)
- [x] Task: Improve unit tests for `WordRepository` and `DataModule` (Red/Green/Refactor)
- [x] Task: Conductor - User Manual Verification 'Phase 1: Foundation & Data Layer' (Protocol in workflow.md)

## Phase 2: Title & Settings Screens [checkpoint: 63f0e25]
Focus on the simpler Circuit presenters and their UI states.

- [x] Task: Audit and Cleanup existing tests for `ui.title` and `ui.settings` (No existing tests found)
- [x] Task: Implement unit tests for `TitleScreenPresenter` (Red/Green/Refactor)
- [x] Task: Implement unit tests for `SettingsScreenPresenter` (Red/Green/Refactor)
- [x] Task: Conductor - User Manual Verification 'Phase 2: Title & Settings Screens' (Protocol in workflow.md)

## Phase 3: Game Screen (Core Logic)
The most complex part of the application, focusing on the real-time typing loop and game state.

- [x] Task: Audit and Cleanup existing tests for `ui.game` (Rewrote GamePresenterTest and PassageGamePresenterTest)
- [x] Task: Implement comprehensive unit tests for `GameScreenPresenter` (Red/Green/Refactor)
    - [x] Sub-task: Test initial state
    - [x] Sub-task: Test typing events (correct/incorrect characters)
    - [x] Sub-task: Test timer/progress updates
    - [x] Sub-task: Test game completion/reset
- [x] Task: Conductor - User Manual Verification 'Phase 3: Game Screen (Core Logic)' (Protocol in workflow.md)

## Phase 4: Final Coverage Verification & Cleanup
Ensuring the 80% target is met and any remaining "silly" tests are addressed.

- [ ] Task: Run final coverage audit and identify any remaining gaps
- [ ] Task: Address any uncovered edge cases in any module
- [ ] Task: Remove any remaining non-functional or redundant tests
- [ ] Task: Conductor - User Manual Verification 'Phase 4: Final Coverage Verification & Cleanup' (Protocol in workflow.md)
