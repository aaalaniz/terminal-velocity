# Specification: Visual Feedback Refinement

## Overview
This track refines the visual feedback of the typing game. Instead of highlighting correct input in green, the game will present the target word in a dimmed state. As the user types correctly, the characters will transition from dimmed to the default (bright) text color. Incorrect input will trigger a brief visual flash (red) to alert the user.

## Functional Requirements
- **Initial State:** The entire target word is displayed using a "dimmed" visual style.
- **Correct Input:** When a user types a correct character, that character's style transitions from "dimmed" to "default/bright".
- **Incorrect Input:** When a user types an incorrect character, the target word (or specific character) should flash red briefly before returning to its previous state. The cursor does not advance.
- **Input Reset:** Upon completing a word and spawning a new one, the new word starts in the fully dimmed state.

## Non-Functional Requirements
- **Performance:** Visual transitions (flashing) must be responsive and not introduce lag in the typing loop.
- **Consistency:** Maintain the clean and minimalist aesthetic defined in the product guidelines.

## Acceptance Criteria
- [ ] Target word is displayed in dimmed text initially.
- [ ] Correctly typed letters change from dimmed to bright text.
- [ ] Incorrect characters trigger a brief red flash.
- [ ] Word completion resets the visual state for the next word.
- [ ] Unit tests for `GamePresenter` are updated to reflect any state changes needed for the flash effect (if state-driven).

## Out of Scope
- Transitioning to passages/multi-word paragraphs (kept for a future track).
- WPM calculation (kept for a future track).
