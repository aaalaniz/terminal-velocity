package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TextWrapperRedosTest {

  @Test
  fun `wrap handles large ansi escape sequences by falling back to stripping ESC`() {
    // Construct a large valid OSC sequence: ESC ] 0 ; <5000 chars> BEL
    // Limit is 4096. So 5000 should exceed it.
    val largeContent = "a".repeat(5000)
    val input = "Start \u001b]0;$largeContent\u0007 End"
    val actual = TextWrapper.wrap(input)
    val actualJoined = actual.joinToString("")

    // Security check: ESC must be stripped to prevent ANSI injection
    assertThat(actualJoined).doesNotContain("\u001b")

    // Content check: Since regex limit prevented ANSI stripping, content remains.
    // We check for a substring of content to avoid issues with wrapping/splitting logic in assertions.
    assertThat(actualJoined).contains("aaaaa")
  }

  @Test
  fun `wrap handles large unterminated sequences safely`() {
    val largeContent = "a".repeat(5000)
    val input = "Start \u001b]$largeContent End"
    val actual = TextWrapper.wrap(input)
    val actualJoined = actual.joinToString("")

    assertThat(actualJoined).contains("aaaaa")
    assertThat(actualJoined).doesNotContain("\u001b")
  }
}
