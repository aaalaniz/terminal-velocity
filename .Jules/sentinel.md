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

## 2026-02-14 - Unbounded ANSI Regex
**Vulnerability:** Regex for variable-length ANSI sequences was unbounded (`*?`), allowing DoS via long inputs.
**Learning:** Even non-greedy quantifiers can be dangerous if the terminator is missing or far away in a large input.
**Prevention:** Always bound regex quantifiers for untrusted input parsing, e.g., `{0,N}?`.

## 2026-02-15 - GitHub API Rate Limits in Installers
**Vulnerability:** `install.sh` used unauthenticated GitHub API calls, subject to strict IP-based rate limits (60/hr), creating a DoS risk for installation.
**Learning:** Distribution scripts should not rely on unauthenticated APIs that have low rate limits.
**Prevention:** Use `curl -I` and `Location` header redirection to resolve release tags, as this uses standard web request limits.
