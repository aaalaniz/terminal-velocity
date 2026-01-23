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

## 2026-01-24 - Incomplete Terminal Injection Sanitization
**Vulnerability:** `TextWrapper` stripped ANSI codes but allowed other control characters (e.g., Bell `\a`, Backspace `\b`). This allowed "weak" terminal injection (beep spam, text overwriting) even after ANSI sanitization.
**Learning:** Stripping ANSI codes (`\u001B...`) is necessary but not sufficient for TUI security; all non-printable control characters (ASCII 0-31, 127) must be sanitized unless explicitly needed (like newline).
**Prevention:** Use a comprehensive regex to strip all unwanted control characters (`[\x00-\x1F\x7F]`) in addition to ANSI sequences.
