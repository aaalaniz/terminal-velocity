package xyz.alaniz.aaron.ui.game

import androidx.compose.ui.Modifier as ComposeModifier
import com.jakewharton.mosaic.testing.runMosaicTest
import kotlin.test.Test
import kotlin.test.assertTrue
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

      assertTrue(
          snapshot.contains(expectedFilled),
          "Snapshot should contain 12 filled blocks: $expectedFilled\nSnapshot:\n$snapshot")
      assertTrue(
          snapshot.contains(expectedEmpty),
          "Snapshot should contain 8 empty blocks: $expectedEmpty\nSnapshot:\n$snapshot")
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
      assertTrue(
          snapshot.contains("·"),
          "Snapshot should contain middle dot '·' for space cursor.\nSnapshot:\n$snapshot")
    }
  }
}
