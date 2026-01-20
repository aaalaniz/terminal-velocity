package xyz.alaniz.aaron.ui.foundation

import java.util.concurrent.TimeUnit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout

class TextWrapperSecurityTest {

  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS)
  fun `wrap throws exception for zero width`() {
    assertThrows(IllegalArgumentException::class.java) { TextWrapper.wrap("Test", 0) }
  }

  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS)
  fun `wrap throws exception for negative width`() {
    assertThrows(IllegalArgumentException::class.java) { TextWrapper.wrap("Test", -1) }
  }

  @Test
  fun `wrap removes ansi escape sequences`() {
    val input = "Hello \u001b[31mWorld\u001b[0m"
    val expected = listOf("Hello World")
    val actual = TextWrapper.wrap(input)
    assertEquals(expected, actual)
  }
}
