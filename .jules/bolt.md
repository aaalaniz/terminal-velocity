## 2024-05-22 - [TextWrapper Sanitization Trade-off]
**Learning:** The `SANITIZATION_REGEX` in `TextWrapper` includes components that can match across lines (e.g., `FE_VAR_REGEX` with `[\s\S]*?`). Optimization to process text line-by-line (to save memory) changes behavior for these multi-line sequences.
**Action:** When optimizing text processing in this codebase, explicitly document assumptions about multi-line regex matches. We traded strict multi-line ANSI support for significant memory savings, assuming trusted/simple inputs.
