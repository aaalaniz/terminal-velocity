## 2024-05-23 - Previewing Content
**Learning:** Providing a preview of the upcoming task (the text passage) in the "Waiting" state helps set user expectations and reduces cognitive load before the timer starts.
**Action:** Always consider what information the user needs *before* committing to an action (like starting a game).

## 2024-05-23 - Testing LaunchedEffects
**Learning:** When using `LaunchedEffect` in Circuit presenters to trigger initial data loading, unit tests using `turbine` must handle the initial state emission properly. The `presenter.test` block might need `cancelAndIgnoreRemainingEvents()` if the test only asserts on the initial state but the effect triggers a subsequent emission.
**Action:** Be mindful of side effects in presenters and how they impact the stream of state emissions in tests.
