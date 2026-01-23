package xyz.alaniz.aaron.ui.foundation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TextWrapperTest {

  @Test
  fun `wrap does not wrap short lines`() {
    val input = "Short line"
    val expected = listOf("Short line")
    assertEquals(expected, TextWrapper.wrap(input, 20))
  }

  @Test
  fun `wrap splits long lines at space`() {
    val input = "This is a long line that should be wrapped"
    val expected = listOf("This is a long line", "that should be", "wrapped")
    // "This is a long line" is 19 chars.
    // "that should be" is 14 chars.
    assertEquals(expected, TextWrapper.wrap(input, 20))
  }

  @Test
  fun `wrap preserves existing newlines`() {
    val input = "Line 1\nLine 2"
    val expected = listOf("Line 1", "Line 2")
    assertEquals(expected, TextWrapper.wrap(input, 20))
  }

  @Test
  fun `wrap handles multiple wraps`() {
    val input = "One two three four five six seven"
    // Width 10
    // "One two" (7)
    // "three four" (10)
    // "five six" (8)
    // "seven" (5)
    val expected = listOf("One two", "three four", "five six", "seven")
    assertEquals(expected, TextWrapper.wrap(input, 10))
  }

  @Test
  fun `wrap forces break if no space found`() {
    val input = "abcdefghijklmnopqrstuvwxyz"
    val expected = listOf("abcdefghij", "klmnopqrst", "uvwxyz")
    assertEquals(expected, TextWrapper.wrap(input, 10))
  }

  @Test
  fun `wrap handles mixed explicit newlines and wrapping`() {
    val input = "Line one is long enough to wrap\nLine two is short"
    // Width 15
    // "Line one is" (11)
    // "long enough to" (14)
    // "wrap" (4)
    // "Line two is" (11)
    // "short" (5)
    val expected = listOf("Line one is", "long enough to", "wrap", "Line two is", "short")
    assertEquals(expected, TextWrapper.wrap(input, 15))
  }
}
