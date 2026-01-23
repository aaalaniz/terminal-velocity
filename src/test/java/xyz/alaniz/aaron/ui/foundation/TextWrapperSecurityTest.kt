package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import kotlin.test.assertFailsWith

class TextWrapperSecurityTest {

  @Test
  fun `wrap throws exception for zero width`() {
    assertFailsWith<IllegalArgumentException> { TextWrapper.wrap("Test", 0) }
  }

  @Test
  fun `wrap throws exception for negative width`() {
    assertFailsWith<IllegalArgumentException> { TextWrapper.wrap("Test", -1) }
  }

  @Test
  fun `wrap removes ansi escape sequences`() {
    val input = "Hello \u001b[31mWorld\u001b[0m"
    val expected = listOf("Hello World")
    val actual = TextWrapper.wrap(input)
    assertThat(actual).isEqualTo(expected)
  }
}
