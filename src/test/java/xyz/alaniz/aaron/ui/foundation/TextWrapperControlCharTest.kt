package xyz.alaniz.aaron.ui.foundation

import kotlin.test.Test
import kotlin.test.assertEquals

class TextWrapperControlCharTest {

  @Test
  fun `wrap removes control characters`() {
    val input = "Hello\u0007 World\u0008!"
    val expected = listOf("Hello World!")
    val actual = TextWrapper.wrap(input)
    assertEquals(expected, actual)
  }

  @Test
  fun `wrap removes ansi and control characters correctly`() {
    // \u001B[31m is ANSI red. \u0007 is Bell.
    // If order is wrong, \u001B is stripped first, leaving [31m which is not stripped.
    val input = "Hello \u001b[31mWorld\u0007!"
    val expected = listOf("Hello World!")
    val actual = TextWrapper.wrap(input)
    assertEquals(expected, actual)
  }
}
