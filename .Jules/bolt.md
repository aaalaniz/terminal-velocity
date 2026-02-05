## 2024-05-23 - TextWrapper Regex Optimization
**Learning:** Combining multiple regex replacements into a single `Regex` with alternation `|` prevents multiple passes over the string and reduces intermediate string allocations.
**Action:** When seeing `text.replace(A, "").replace(B, "")`, consider `text.replace(Regex("A|B"), "")`.

## 2024-05-24 - Mosaic Node Reduction
**Learning:** In Mosaic TUI, replacing a `Column` of repeated single-character `Text` nodes with a single multi-line `Text` node significantly reduces the composition tree size without altering the visual output.
**Action:** Look for loop-based `Text` repetition in Mosaic layouts and replace with joined strings where possible.

## 2024-05-24 - Efficient String Processing
**Learning:** Using `lineSequence()` instead of `lines()` prevents allocating a potentially large list of strings when iterating over lines, reducing memory pressure.
**Action:** Prefer `text.lineSequence().forEach` over `text.lines().forEach` for read-only iteration.

## 2026-01-31 - Sequence Overhead on Small Strings
**Learning:** Replacing `split()` with `splitToSequence()` or manual parsing on small strings (e.g., < 100 chars, 3 parts) may introduce overhead (Sequence object creation, wrapper objects) that outweighs the benefit of avoiding intermediate list allocation. Simple `split()` can be faster for small, low-cardinality splits.
**Action:** Profile before replacing `split()` with sequences for short lines. The main win comes from streaming the *lines* themselves, not necessarily the intra-line parsing.

## 2024-05-24 - Mosaic Text Node Memoization
**Learning:** In Mosaic (and Compose), constructing strings for `Text` nodes inside a frequently recomposed scope (like a game loop or typing input) creates significant allocation pressure.
**Action:** Use `remember` to cache string construction (e.g., `joinToString`) for static parts of the UI (like completed or future text blocks) that only change when state thresholds are crossed, rather than on every frame.

## 2026-02-05 - String.lastIndexOf Performance Trap
**Learning:** `String.lastIndexOf(char, fromIndex)` searches backwards from `fromIndex` to the *beginning of the string* (index 0), not to a specific lower bound. This can lead to O(N^2) complexity when processing strings in chunks if the character is not found, as it repeatedly rescans the prefix.
**Action:** When searching for a character within a specific range (start to end), use a manual loop `for (i in end downTo start)` or `lastIndexOf` only if you know the character exists in the range.
