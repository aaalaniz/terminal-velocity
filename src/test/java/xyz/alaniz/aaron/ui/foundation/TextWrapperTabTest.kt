package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TextWrapperTabTest {

  @Test
  fun `wrap expands tabs to spaces`() {
    val input = "Tab\tChar"
    // Expecting 2 spaces per tab
    val expected = listOf("Tab  Char")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }

  @Test
  fun `wrap expands multiple tabs`() {
    val input = "A\t\tB"
    // Expecting 4 spaces total
    val expected = listOf("A    B")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }
}
