package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TextWrapperControlCharTest {

  @Test
  fun `wrap removes bell character`() {
    val input = "Ding\u0007Dong"
    val expected = listOf("DingDong")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }

  @Test
  fun `wrap removes backspace character`() {
    val input = "Back\u0008Space"
    val expected = listOf("BackSpace")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }

  @Test
  fun `wrap removes delete character`() {
    val input = "Del\u007Fete"
    val expected = listOf("Delete")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }

  @Test
  fun `wrap removes vertical tab`() {
    val input = "Vertical\u000BTab"
    val expected = listOf("VerticalTab")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }

  @Test
  fun `wrap removes null character`() {
    val input = "Null\u0000Char"
    val expected = listOf("NullChar")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }

  @Test
  fun `wrap preserves tab character`() {
    // Note: TextWrapper doesn't expand tabs, just preserves them in the string.
    val input = "Tab\tChar"
    val expected = listOf("Tab\tChar")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }

  @Test
  fun `wrap preserves carriage return`() {
    // Depending on how wrap handles split lines, it might split on CR or LF or CRLF.
    // The current implementation uses `line.lines()` which handles CR, LF, CRLF.
    // However, `TextWrapper.wrap` logic iterates over `sanitizedText.lines()`.
    // Kotlin's `String.lines()` splits by \n, \r, \r\n.
    // So if we have "Line1\rLine2", it will be ["Line1", "Line2"].

    val input = "Line1\rLine2"
    val expected = listOf("Line1", "Line2")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }

  @Test
  fun `wrap preserves newline`() {
    val input = "Line1\nLine2"
    val expected = listOf("Line1", "Line2")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }

  @Test
  fun `wrap removes multiple control characters`() {
    val input = "\u0001Start\u0002 \u0003End\u0004"
    val expected = listOf("Start End")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }

  @Test
  fun `wrap removes mixed ansi and control characters`() {
    val input = "Color\u001B[31mRed\u001B[0m\u0007Bell"
    val expected = listOf("ColorRedBell")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }
}
