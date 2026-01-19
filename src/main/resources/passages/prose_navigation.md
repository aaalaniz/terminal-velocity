---
tags: [prose, education]
---
Navigation in applications involves moving between different screens or states. In a terminal application, this might mean switching between different views or menus. Circuit is a library that provides a pattern for navigation and state management. It uses a unidirectional data flow architecture, which makes state changes predictable. Presenters in Circuit are responsible for producing the state for a given screen. The UI then renders this state, and user events are sent back to the presenter. This separation of logic and presentation makes the codebase easier to maintain.
