package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TextWrapperC1ControlTest {

  @Test
  fun `wrap removes C1 control characters`() {
    // \u0080 is Padding Character (PAD)
    // \u009B is Control Sequence Introducer (CSI)
    val input = "C1\u0080Control\u009BChar"
    val expected = listOf("C1ControlChar")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }

  @Test
  fun `wrap removes range of C1 control characters`() {
    // Testing boundary and middle characters of C1 range (0x80 - 0x9F)
    val input = "\u0080Start\u008FMiddle\u009FEnd"
    val expected = listOf("StartMiddleEnd")
    assertThat(TextWrapper.wrap(input)).isEqualTo(expected)
  }
}
