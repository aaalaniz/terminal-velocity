package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TextWrapperRedosTest {

  @Test
  fun `wrap removes short variable length ansi sequence`() {
    val input = "\u001B]0;Short Title\u0007Content"
    // The ANSI sequence is \u001B]0;Short Title\u0007.
    // It should be removed.
    // "Content" remains.
    val expected = listOf("Content")
    assertThat(TextWrapper.wrap(input, 80)).isEqualTo(expected)
  }

  @Test
  fun `wrap limits ansi sequence matching to 4096 chars`() {
    // Construct a sequence slightly longer than 4096 chars
    val payload = "A".repeat(4097)
    val input = "\u001B]$payload\u0007Content"

    // With unbounded regex, this matches as one ANSI sequence and is removed.
    // Result: "Content"

    // With bounded regex {0,4096}, it fails to match as ANSI.
    // \u001B is matched as control char -> removed.
    // ] is text.
    // payload is text.
    // \u0007 is control char -> removed.
    // Result: "]" + payload + "Content"

    // We expect the fix to result in the payload being visible (sanitized).
    val expectedText = "]$payload" + "Content"

    // Since the payload is long, it will be wrapped.
    // The wrap width is Int.MAX_VALUE to avoid testing the wrapping logic itself, just the
    // sanitization.
    // Wait, wrap takes an int.

    val result = TextWrapper.wrap(input, Int.MAX_VALUE)

    // If the regex is unbounded, result is ["Content"]
    // If bounded, result is [expectedText]

    assertThat(result).containsExactly(expectedText)
  }

  @Test
  fun `wrap limits csi sequence matching`() {
    val params = "0".repeat(1025)
    val input = "\u001B[$params@Content"
    val result = TextWrapper.wrap(input, Int.MAX_VALUE)
    val expectedText = "[$params@Content"
    assertThat(result).containsExactly(expectedText)
  }
}
