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

## 2026-01-24 - C1 Control Character Injection
**Vulnerability:** `TextWrapper` sanitized C0 control characters and standard ANSI sequences but missed C1 control characters (`\u0080`-`\u009F`), which can initiate escape sequences in 8-bit terminals.
**Learning:** Standard ANSI regexes often overlook C1 controls (8-bit), focusing only on 7-bit ESC sequences, leaving a gap for terminal injection.
**Prevention:** Explicitly include the C1 control range `[\u0080-\u009F]` in sanitization regexes for TUI applications.
