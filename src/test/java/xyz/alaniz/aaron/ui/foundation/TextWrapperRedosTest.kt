package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TextWrapperRedosTest {

  @Test
  fun `wrap removes variable length sequence within limit`() {
    val content = "a".repeat(4000)
    val input = "Start \u001b]0;$content\u0007 End"
    // Use large width to avoid wrapping artifacts
    val actual = TextWrapper.wrap(input, width = 10000)
    val expected = listOf("Start  End")
    assertThat(actual).isEqualTo(expected)
  }

  @Test
  fun `wrap does not match variable length sequence exceeding limit`() {
    val content = "a".repeat(5000)
    val input = "Start \u001b]0;$content\u0007 End"
    // Use large width so we get a single line output (mostly)
    val actual = TextWrapper.wrap(input, width = 10000)
    // If limit enforced: ESC and BEL removed, content remains.
    val expected = listOf("Start ]0;$content End")
    assertThat(actual).isEqualTo(expected)
  }
}
