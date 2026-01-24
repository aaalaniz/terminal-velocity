package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TextWrapperC1ControlTest {

  @Test
  fun `wrap removes C1 control characters`() {
    // \u009B is CSI (Control Sequence Introducer)
    val input = "Test\u009BInjection"
    val expected = listOf("TestInjection")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }

  @Test
  fun `wrap removes multiple C1 control characters`() {
    // \u0080 (Padding Character) and \u009F (Application Program Command)
    val input = "\u0080Start\u009FEnd"
    val expected = listOf("StartEnd")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }
}
