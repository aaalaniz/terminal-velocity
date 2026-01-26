## 2024-05-23 - TextWrapper Regex Optimization
**Learning:** Combining multiple regex replacements into a single `Regex` with alternation `|` prevents multiple passes over the string and reduces intermediate string allocations.
**Action:** When seeing `text.replace(A, "").replace(B, "")`, consider `text.replace(Regex("A|B"), "")`.
