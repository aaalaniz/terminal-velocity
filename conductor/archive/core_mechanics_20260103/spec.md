# Specification: Core Typing Game Mechanics

## Goal
Implement the fundamental gameplay loop of Terminal Velocity, where players type words displayed on the screen. This includes generating random words, capturing real-time user input, validating correctness, and tracking basic game state (score, active word).

## Core Components

### 1. Game State Management (Circuit Presenter)
- **State:**
    - `currentWord`: The target word the user needs to type.
    - `userInput`: The characters the user has typed so far.
    - `score`: Current player score.
    - `gameState`: Enum (WAITING, PLAYING, GAME_OVER).
- **Events:**
    - `LetterTyped(char)`: Triggered when user types a character.
    - `GameStarted`: Triggered when the user starts the game.
    - `GameReset`: Triggered to reset the game state.

### 2. Word Generation
- A simple mechanism to supply random words for the game.
- Ideally backed by a repository or a hardcoded list for the MVP.

### 3. Terminal UI (Mosaic)
- **Display:**
    - Show the `currentWord` clearly.
    - Visually distinguish between correctly typed characters, incorrectly typed characters, and remaining characters.
    - Display current score.
- **Input:**
    - Capture keyboard events to drive the `LetterTyped` event.

## Technical Considerations
- **Unidirectional Data Flow:** The Presenter should hold the state and expose it to the UI. The UI should only emit events.
- **Testability:** The Presenter logic (state transitions, scoring, input validation) must be unit-tested thoroughly without UI dependencies.
- **Mosaic Interaction:** Use Mosaic's key event handling to capture input without blocking the main render loop.

## Acceptance Criteria
- [ ] User can start a game.
- [ ] A random word is displayed.
- [ ] Typing the correct letters advances the input cursor.
- [ ] Typing an incorrect letter provides visual feedback (e.g., color change).
- [ ] Completing a word increments the score and spawns a new word.
- [ ] Basic unit tests cover the game logic (Presenter).
