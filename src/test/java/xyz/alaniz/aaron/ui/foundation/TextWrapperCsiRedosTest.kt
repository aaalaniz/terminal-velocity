package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TextWrapperCsiRedosTest {

  @Test
  fun `wrap limits csi ansi sequence matching to 1024 chars`() {
    // Construct a CSI sequence longer than 1024 chars
    // CSI is ESC [ ... @
    // We'll put 1025 '0's.
    val payload = "0".repeat(1025)
    val input = "\u001B[$payload@" + "Content"

    // If unbounded, it matches CSI and is removed -> result ["Content"]
    // If bounded to 1024, it fails match.
    // \u001B -> removed (control char)
    // [ -> kept
    // payload -> kept
    // @ -> kept
    // Content -> kept
    // Result -> "[" + payload + "@" + "Content"

    val expectedText = "[$payload@" + "Content"

    val result = TextWrapper.wrap(input, Int.MAX_VALUE)

    assertThat(result).containsExactly(expectedText)
  }
}
