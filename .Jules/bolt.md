## 2024-05-23 - TextWrapper Regex Optimization
**Learning:** Combining multiple regex replacements into a single `Regex` with alternation `|` prevents multiple passes over the string and reduces intermediate string allocations.
**Action:** When seeing `text.replace(A, "").replace(B, "")`, consider `text.replace(Regex("A|B"), "")`.

## 2024-05-24 - Mosaic Node Reduction
**Learning:** In Mosaic TUI, replacing a `Column` of repeated single-character `Text` nodes with a single multi-line `Text` node significantly reduces the composition tree size without altering the visual output.
**Action:** Look for loop-based `Text` repetition in Mosaic layouts and replace with joined strings where possible.

## 2024-05-24 - Efficient String Processing
**Learning:** Using `lineSequence()` instead of `lines()` prevents allocating a potentially large list of strings when iterating over lines, reducing memory pressure.
**Action:** Prefer `text.lineSequence().forEach` over `text.lines().forEach` for read-only iteration.
