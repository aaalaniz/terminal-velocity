# Specification: Audio Feedback

## Purpose
Enhance the typing experience with immediate auditory feedback. This feature serves a dual purpose: improving game feel and demonstrating asynchronous I/O and dependency injection in a Mosaic/Circuit application.

## Requirements
- **Sounds:**
    -   `keypress.wav`: Subtle click for correct keystrokes.
    -   `error.wav`: Distinct sound for incorrect input.
    -   `completion.wav`: Celebration sound for finishing a passage.
-   **Implementation:**
    -   Use `javax.sound.sampled` (standard JDK) for simplicity.
    -   Wrap audio logic in a `SoundPlayer` interface.
    -   Ensure sound playback is non-blocking (fire-and-forget).
-   **Settings:**
    -   Add a "Sound Effects" toggle to the Settings screen.
    -   Persist preference in `SettingsRepository`.
