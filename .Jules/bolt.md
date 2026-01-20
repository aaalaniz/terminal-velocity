## 2025-05-18 - Unnecessary SecureRandom in Casual Games
**Learning:** Using `java.security.SecureRandom` for non-critical game mechanics (like selecting a random passage) adds unnecessary initialization overhead and potential entropy blocking.
**Action:** Use `kotlin.random.Random` (or `java.util.Random`) for casual randomization unless cryptographic security is explicitly required.
