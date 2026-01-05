# Plan: Improve Unit Test Coverage (80%)

## Phase 1: Foundation & Data Layer [checkpoint: 3424284]
This phase focuses on ensuring the core utilities and data management logic are robustly tested and removing non-meaningful tests.

- [x] Task: Audit and Cleanup existing tests for `data` and `ui.foundation` packages (Removed silly tests)
- [x] Task: Implement unit tests for `KeyEvents.kt` (Red/Green/Refactor)
- [x] Task: Implement unit tests for `MosaicNavDecoration.kt` (Reviewed: logic is trivial/UI-bound, inheritance test removed as silly)
- [x] Task: Improve unit tests for `WordRepository` and `DataModule` (Red/Green/Refactor)
- [x] Task: Conductor - User Manual Verification 'Phase 1: Foundation & Data Layer' (Protocol in workflow.md)

## Phase 2: Title & Settings Screens
Focus on the simpler Circuit presenters and their UI states.

- [ ] Task: Audit and Cleanup existing tests for `ui.title` and `ui.settings`
- [ ] Task: Implement unit tests for `TitleScreenPresenter` (Red/Green/Refactor)
- [ ] Task: Implement unit tests for `SettingsScreenPresenter` (Red/Green/Refactor)
- [ ] Task: Conductor - User Manual Verification 'Phase 2: Title & Settings Screens' (Protocol in workflow.md)

## Phase 3: Game Screen (Core Logic)
The most complex part of the application, focusing on the real-time typing loop and game state.

- [ ] Task: Audit and Cleanup existing tests for `ui.game`
- [ ] Task: Implement comprehensive unit tests for `GameScreenPresenter` (Red/Green/Refactor)
    - [ ] Sub-task: Test initial state
    - [ ] Sub-task: Test typing events (correct/incorrect characters)
    - [ ] Sub-task: Test timer/progress updates
    - [ ] Sub-task: Test game completion/reset
- [ ] Task: Conductor - User Manual Verification 'Phase 3: Game Screen (Core Logic)' (Protocol in workflow.md)

## Phase 4: Final Coverage Verification & Cleanup
Ensuring the 80% target is met and any remaining "silly" tests are addressed.

- [ ] Task: Run final coverage audit and identify any remaining gaps
- [ ] Task: Address any uncovered edge cases in any module
- [ ] Task: Remove any remaining non-functional or redundant tests
- [ ] Task: Conductor - User Manual Verification 'Phase 4: Final Coverage Verification & Cleanup' (Protocol in workflow.md)
