# Plan: Audio Feedback Implementation

## Phase 1: Research & Prototype
- [ ] **Task:** Research `javax.sound.sampled.Clip` for low-latency playback in a Kotlin JVM CLI app. <!-- linear-issue: TVELO-3 -->
- [ ] **Task:** Create a standalone script or test to play a `.wav` file asynchronously. <!-- linear-issue: TVELO-4 -->

## Phase 2: Core Implementation
- [ ] **Task:** Define `SoundPlayer` interface and `JvmSoundPlayer` implementation. <!-- linear-issue: TVELO-5 -->
- [ ] **Task:** Source or generate placeholder `.wav` files (click, error, completion).
- [ ] **Task:** Add `SoundPlayer` to the Metro dependency graph (`@AppScope`).

## Phase 3: Integration & UI
- [ ] **Task:** Integrate `SoundPlayer.playEffect()` into `GamePresenter` logic (on key press, on completion).
- [ ] **Task:** Add "Sound Effects" toggle to `SettingsRepository` and `SettingsScreen`.
- [ ] **Task:** Verify sound toggle functionality (mute/unmute).
