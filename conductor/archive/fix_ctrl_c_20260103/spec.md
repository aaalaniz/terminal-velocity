# Specification: Fix Ctrl+C Exit Behavior

## Overview
Currently, the game screen captures all keyboard input to drive the gameplay loop, effectively swallowing the `Ctrl+C` interrupt signal. This prevents the application from terminating when the user expects standard terminal exit behavior. This track will ensure that `Ctrl+C` results in an immediate application exit.

## Functional Requirements
- **Input Handling:** Detect the `Ctrl+C` key combination within the `GameScreenUi` event handler.
- **Application Exit:** Upon detecting `Ctrl+C`, the application must terminate immediately, restoring the terminal to its previous state.

## Non-Functional Requirements
- **Standard Convention:** Adhere to standard CLI behavior for `SIGINT` / `Ctrl+C`.
- **Clean Shutdown:** Ensure Mosaic/runMosaic handles the shutdown cleanly (standard behavior when the process exits).

## Acceptance Criteria
- [ ] While in the Game Screen, pressing `Ctrl+C` terminates the application process.
- [ ] The exit happens immediately upon the key combination being pressed.

## Out of Scope
- Navigating to the Title Screen (replaced by immediate exit).
- Any "Save and Quit" functionality.
