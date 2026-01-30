## 2026-01-19 - Infinite Loop in Text Wrapper
**Vulnerability:** Infinite loop in `TextWrapper.wrap` when `width` is <= 0.
**Learning:** Utility functions often assume valid positive inputs but fail catastrophically (DoS) when invariants are violated, especially in loop conditions.
**Prevention:** Always validate numeric inputs for geometric/layout algorithms with `require` or `check` at the start of the function.

## 2026-01-20 - Terminal Injection Prevention
**Vulnerability:** Potential for Terminal Injection (ANSI Escape Injection) if passage content contains malicious control characters.
**Learning:** TUI apps relying on string content must sanitize input to prevent UI spoofing or terminal state corruption.
**Prevention:** Strip ANSI escape sequences in display/wrapping layers using regex validation.

## 2026-01-22 - Unbounded Resource Loading (DoS)
**Vulnerability:** `MarkdownWordRepository` used `readText()` to load passage files, which reads the entire file into memory at once. A malicious or accidentally large file in `passages/` could cause an `OutOfMemoryError` (DoS).
**Learning:** Even "trusted" local resources should be treated with suspicion regarding size, as they can be modified or point to unexpected targets.
**Prevention:** Implement streaming reads with an explicit character/byte limit (`MAX_PASSAGE_SIZE`) for all file loading operations.

## 2026-01-24 - Incomplete ANSI Sanitization
**Vulnerability:** The regex used to strip ANSI escape sequences missed variable-length sequences like OSC (`ESC ] ... BEL/ST`), allowing potential terminal injection or display corruption.
**Learning:** Standard "ANSI regexes" often only cover CSI (`ESC [`) and simple Fe sequences, missing the more complex but powerful sequences defined in ECMA-48.
**Prevention:** Use a comprehensive regex that accounts for CSI, OSC, DCS, PM, and APC sequences, explicitly handling their terminators (`BEL` or `ST`).

## 2026-01-25 - Unbounded Regex Matching (ReDoS)
**Vulnerability:** The regex for variable-length ANSI escape sequences (`[\\s\\S]*?`) was unbounded, allowing a single malicious sequence to force the regex engine to scan the entire string, leading to DoS.
**Learning:** Regex quantifiers like `*` or `+` on "match anything" groups (`.` or `[\s\S]`) are dangerous on untrusted input, even with non-greedy modifiers, if the terminator is missing or distant.
**Prevention:** Always impose a hard length limit (e.g., `{0,N}`) on variable-length matches in regexes used for sanitization.
