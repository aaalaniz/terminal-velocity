package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TextWrapperRedosTest {

  @Test
  fun `wrap limits variable length ansi sequences`() {
    // Generate a sequence longer than 4096 characters
    // \u001bP is DCS (Device Control String)
    val longContent = "a".repeat(5000)
    val input = "Start \u001bP$longContent\u001b\\ End"

    // Use Int.MAX_VALUE to prevent wrapping logic from splitting the string,
    // allowing us to assert on the sanitization result easily.
    val actual = TextWrapper.wrap(input, Int.MAX_VALUE)

    // With fix, the DCS won't match as a whole.
    // The first \u001b (ESC) will be stripped by control char regex.
    // The P and content will remain.
    // The terminator \u001b\\ (ESC \) is a valid ANSI sequence (ST) on its own, so it is stripped.
    val expected = "Start P$longContent End"
    assertThat(actual).containsExactly(expected)
  }

  @Test
  fun `wrap allows variable length ansi sequences within limit`() {
    // Content exactly 4096 chars
    val content = "a".repeat(4096)
    val input = "Start \u001bP$content\u001b\\ End"

    val actual = TextWrapper.wrap(input, Int.MAX_VALUE)

    // Should be fully stripped
    assertThat(actual).containsExactly("Start  End")
  }
}
