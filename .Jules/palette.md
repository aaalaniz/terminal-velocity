# Palette's Journal

## 2024-05-22 - Terminal Navigation Patterns
**Learning:** Terminal apps often trap users in deep states without a "back" button, unlike web/mobile apps where the back button is omnipresent. Explicit "Esc" or "Back" navigation is critical.
**Action:** Always verify that every screen has a way to return to the previous screen without killing the app.

## 2024-05-22 - Scannable Key Bindings
**Learning:** In terminal UIs, key bindings are the primary interaction method. Uniformly dimmed text makes it hard to scan for "What can I do?".
**Action:** Highlight the key binding (e.g., Bold) and dim the description. This creates a clear visual hierarchy: `[Key]` (Action) vs `Description` (Intent).

## 2024-05-24 - Color for State Indication
**Learning:** In monochromatic or limited-color terminal UIs, relying solely on ascii characters like `[x]` vs `[ ]` for state can be visually flat.
**Action:** Use distinct colors (like Green for active/checked) to reinforce state, allowing users to scan for "Active" items without parsing individual characters.
