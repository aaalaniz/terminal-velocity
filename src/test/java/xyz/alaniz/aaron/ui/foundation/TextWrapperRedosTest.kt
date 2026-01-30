package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TextWrapperRedosTest {

  @Test
  fun `wrap handles very long escape sequences`() {
    // 5000 characters of 'a'
    val longContent = "a".repeat(5000)
    // OSC sequence: ESC ] 0 ; <content> BEL
    val input = "\u001b]0;$longContent\u0007"

    val result = TextWrapper.wrap(input)

    // The regex limit (4096) prevents the full sequence from matching as ANSI.
    // Fallback sanitization removes ESC (\u001b) and BEL (\u0007).
    // The remaining content ("]0;" + 5000 'a's) is treated as plain text.
    // TextWrapper wraps text, so we expect a list of strings.
    // Since "]" and "0;" and "a"s are not spaces, it might get wrapped if it exceeds width.
    // But here we just check that the content is present, not empty.

    val joinedResult = result.joinToString("")
    assertThat(joinedResult).contains(longContent)
    assertThat(joinedResult).startsWith("]0;")
    assertThat(joinedResult).doesNotContain("\u001b")
    assertThat(joinedResult).doesNotContain("\u0007")
  }
}
