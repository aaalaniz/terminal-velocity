# Specification: Multi-Line Passages and WPM Support

## Overview
This track expands the gameplay from single-word typing to full multi-line passages. It introduces scrolling text presentation, real-time WPM (Words Per Minute) calculation, and a comprehensive summary screen upon completion.

## Functional Requirements
- **Passage Support:** 
    - Replace the current random word generator with a sequence of words from a multi-line passage.
    - For this track, a single hardcoded passage (approx. 2-3 minutes of typing) will be used.
- **UI (Scrolling Window):**
    - Display a fixed window of lines (approx. 5-7 lines) from the passage.
    - As the user types, correctly completed lines should scroll out of view, and new lines should scroll in.
    - Maintain the "dimmed untyped / bright typed" visual style established in previous tracks.
- **Metrics Calculation:**
    - **WPM:** Calculate as `(Correct Characters / 5) / (Time in Minutes)`.
    - **Accuracy:** Calculate as `(Correct Characters / Total Keystrokes) * 100`.
- **Real-Time Display:** Show the current WPM on the game screen during play.
- **Summary Screen:** Upon completing the final word of the passage, display:
    - Final WPM.
    - Final Accuracy %.
    - Total Time Taken.
    - Option to restart the passage (Press 'Enter').

## Non-Functional Requirements
- **Precision:** Timer should start on the first correct keystroke.
- **Robustness:** Handle spaces and potential line breaks within the passage correctly.

## Acceptance Criteria
- [ ] User can type through a multi-line passage.
- [ ] The UI scrolls automatically as lines are completed.
- [ ] Real-time WPM is displayed and updated accurately.
- [ ] Summary screen shows correct WPM, Accuracy, and Time.
- [ ] Pressing 'Enter' on summary screen restarts the passage.

## Out of Scope
- Selection of multiple passages (executable will have one hardcoded passage for now).
- Code snippet passages (reserved for future track).
- Persistence of high scores.
