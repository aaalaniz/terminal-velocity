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

  @Test
  fun `wrap removes osc escape sequences`() {
    val input = "Start \u001b]0;Title\u0007 End"
    val actual = TextWrapper.wrap(input)
    val expected = listOf("Start  End")
    assertThat(actual).isEqualTo(expected)
  }

  @Test
  fun `wrap removes osc escape sequences with ST terminator`() {
    val input = "Start \u001b]0;Title\u001b\\ End"
    val actual = TextWrapper.wrap(input)
    val expected = listOf("Start  End")
    assertThat(actual).isEqualTo(expected)
  }

  @Test
  fun `wrap removes mixed sequences`() {
    val input = "Start \u001b[31mRed\u001b[0m \u001b]0;Title\u0007 End"
    val actual = TextWrapper.wrap(input)
    val expected = listOf("Start Red  End")
    assertThat(actual).isEqualTo(expected)
  }
}
