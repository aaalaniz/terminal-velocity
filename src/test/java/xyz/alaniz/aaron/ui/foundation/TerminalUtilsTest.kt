package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class TerminalUtilsTest {
  private val outContent = ByteArrayOutputStream()
  private val originalOut = System.out

  @BeforeTest
  fun setUpStreams() {
    System.setOut(PrintStream(outContent))
  }

  @AfterTest
  fun restoreStreams() {
    System.setOut(originalOut)
  }

  @Test
  fun clearScreen_printsAnsiEscapeSequence() {
    TerminalUtils.clearScreen()
    assertThat(outContent.toString()).isEqualTo("\u001b[H\u001b[2J")
  }
}
