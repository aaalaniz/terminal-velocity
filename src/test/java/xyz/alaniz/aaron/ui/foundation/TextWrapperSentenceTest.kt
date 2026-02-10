package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TextWrapperSentenceTest {

  @Test
  fun sentenceSplit_basicProse() {
    val input = "Hello world. This is a test. Another sentence!"
    val expected = listOf("Hello world.", "This is a test.", "Another sentence!")
    assertThat(TextWrapper.sentenceSplit(input)).isEqualTo(expected)
  }

  @Test
  fun sentenceSplit_trailingSpace() {
    val input = "Sentence one. Sentence two. "
    val expected = listOf("Sentence one.", "Sentence two.")
    assertThat(TextWrapper.sentenceSplit(input)).isEqualTo(expected)
  }

  @Test
  fun sentenceSplit_multipleSpaces() {
    val input = "Sentence one.   Sentence two."
    val expected = listOf("Sentence one.", "Sentence two.")
    assertThat(TextWrapper.sentenceSplit(input)).isEqualTo(expected)
  }

  @Test
  fun sentenceSplit_questionAndExclamation() {
    val input = "What? No! Yes."
    val expected = listOf("What?", "No!", "Yes.")
    assertThat(TextWrapper.sentenceSplit(input)).isEqualTo(expected)
  }

  @Test
  fun sentenceSplit_noDelimiters() {
    val input = "This is just one long sentence without delimiters"
    val expected = listOf("This is just one long sentence without delimiters")
    assertThat(TextWrapper.sentenceSplit(input)).isEqualTo(expected)
  }

  @Test
  fun displayWrap_simple() {
    val input = "Hello world"
    val lines = TextWrapper.displayWrap(input, 5)
    assertThat(lines).containsExactly("Hello", " ", "world")
    assertThat(lines.joinToString("")).isEqualTo(input)
  }

  @Test
  fun displayWrap_preservesSpaces() {
    val input = "A long sentence with many spaces."
    val lines = TextWrapper.displayWrap(input, 10)
    assertThat(lines.joinToString("")).isEqualTo(input)
  }

  @Test
  fun displayWrap_newlines() {
    val input = "Line1\nLine2"
    val lines = TextWrapper.displayWrap(input, 50)
    println("displayWrap_newlines result: $lines")
    // Should split at newline
    assertThat(lines).containsExactly("Line1\n", "Line2")
    assertThat(lines.joinToString("")).isEqualTo(input)
  }

  @Test
  fun displayWrap_longWord() {
    val input = "Supercalifragilisticexpialidocious"
    val lines = TextWrapper.displayWrap(input, 10)
    assertThat(lines.joinToString("")).isEqualTo(input)
    lines.forEach { assertThat(it.length).isAtMost(10) }
  }
}
