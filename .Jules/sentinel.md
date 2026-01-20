## 2026-01-19 - Infinite Loop in Text Wrapper
**Vulnerability:** Infinite loop in `TextWrapper.wrap` when `width` is <= 0.
**Learning:** Utility functions often assume valid positive inputs but fail catastrophically (DoS) when invariants are violated, especially in loop conditions.
**Prevention:** Always validate numeric inputs for geometric/layout algorithms with `require` or `check` at the start of the function.

## 2026-01-20 - Terminal Injection Prevention
**Vulnerability:** Potential for Terminal Injection (ANSI Escape Injection) if passage content contains malicious control characters.
**Learning:** TUI apps relying on string content must sanitize input to prevent UI spoofing or terminal state corruption.
**Prevention:** Strip ANSI escape sequences in display/wrapping layers using regex validation.
