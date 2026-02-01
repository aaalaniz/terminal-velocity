package xyz.alaniz.aaron.ui.game

import androidx.compose.ui.Modifier as ComposeModifier
import com.google.common.truth.Truth.assertThat
import com.jakewharton.mosaic.testing.runMosaicTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class GameScreenUiRenderingTest {
  @Test
  fun testProgressBarRendering() = runTest {
    runMosaicTest {
      val passage = listOf("hello world")
      val currentWord = "hello"
      val userInput = "hel" // 3 chars typed
      // lineProgress = 3 / 5 = 0.6
      // totalProgress = (0 + 0.6) / 1 = 0.6
      // barWidth = 20
      // filledCount = 0.6 * 20 = 12
      // emptyCount = 20 - 12 = 8

      val state =
          GameState.State(
              currentWord = currentWord,
              userInput = userInput,
              score = 0,
              status = GameStatus.PLAYING,
              isError = false,
              wpm = 0.0,
              accuracy = 100.0,
              elapsedTime = 0,
              passage = passage,
              currentLineIndex = 0,
              countdownStage = 0,
              eventSink = {})

      setContent { GameScreenUi(state, ComposeModifier) }

      val snapshot = awaitSnapshot()
      // filledCount = 12, emptyCount = 8
      val expectedFilled = "█".repeat(12)
      val expectedEmpty = "░".repeat(8)

      assertThat(snapshot).contains(expectedFilled)
      assertThat(snapshot).contains(expectedEmpty)
    }
  }

  @Test
  fun testSpaceCursorRendering() = runTest {
    runMosaicTest {
      val passage = listOf("hello world")
      val currentWord = "hello world"
      val userInput = "hello" // 5 chars typed. Next is space.

      val state =
          GameState.State(
              currentWord = currentWord,
              userInput = userInput,
              score = 0,
              status = GameStatus.PLAYING,
              isError = false,
              wpm = 0.0,
              accuracy = 100.0,
              elapsedTime = 0,
              passage = passage,
              currentLineIndex = 0,
              countdownStage = 0,
              eventSink = {})

      setContent { GameScreenUi(state, ComposeModifier) }

      val snapshot = awaitSnapshot()

      // We expect the space cursor to be rendered as a middle dot '·'
      assertThat(snapshot).contains("·")
    }
  }

  @Test
  fun testCompletedLinesRendering() = runTest {
    runMosaicTest {
      val passage = listOf("Completed 1", "Completed 2", "Current", "Future 1", "Future 2")
      val currentWord = "Current"
      val userInput = ""
      val currentLineIndex = 2 // 0 and 1 are completed

      val state =
          GameState.State(
              currentWord = currentWord,
              userInput = userInput,
              score = 0,
              status = GameStatus.PLAYING,
              isError = false,
              wpm = 0.0,
              accuracy = 100.0,
              elapsedTime = 0,
              passage = passage,
              currentLineIndex = currentLineIndex,
              countdownStage = 0,
              eventSink = {})

      setContent { GameScreenUi(state, ComposeModifier) }

      val snapshot = awaitSnapshot()

      // Check for completed lines
      assertThat(snapshot).contains("Completed 1")
      assertThat(snapshot).contains("Completed 2")
      // Check for current line
      assertThat(snapshot).contains("Current")
      // Check for future lines
      assertThat(snapshot).contains("Future 1")
      assertThat(snapshot).contains("Future 2")
    }
  }

  @Test
  fun testBordersRendering() = runTest {
    runMosaicTest {
      val passage = listOf("Line 1", "Line 2", "Line 3", "Line 4", "Line 5")
      val currentWord = "Line 1"
      val userInput = ""

      val state =
          GameState.State(
              currentWord = currentWord,
              userInput = userInput,
              score = 0,
              status = GameStatus.PLAYING,
              isError = false,
              wpm = 0.0,
              accuracy = 100.0,
              elapsedTime = 0,
              passage = passage,
              currentLineIndex = 0,
              countdownStage = 0,
              eventSink = {})

      setContent { GameScreenUi(state, ComposeModifier) }

      val snapshot = awaitSnapshot()

      // Check for borders
      // Use unicode escapes to avoid encoding issues
      assertThat(snapshot).contains("\u250F") // ┏
      assertThat(snapshot).contains("\u2513") // ┓
      assertThat(snapshot).contains("\u2517") // ┗
      assertThat(snapshot).contains("\u251B") // ┛
      assertThat(snapshot).contains("\u2503") // ┃

      // Check for titles
      assertThat(snapshot).contains("Stats")
      assertThat(snapshot).contains("Passage")

      // Check content is inside
      assertThat(snapshot).contains("Line 1")
    }
  }
}
