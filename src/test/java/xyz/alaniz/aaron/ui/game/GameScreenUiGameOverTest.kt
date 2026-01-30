package xyz.alaniz.aaron.ui.game

import androidx.compose.ui.Modifier as ComposeModifier
import com.google.common.truth.Truth.assertThat
import com.jakewharton.mosaic.testing.runMosaicTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class GameScreenUiGameOverTest {
  @Test
  fun testGameOverResultsBoxRendering() = runTest {
    runMosaicTest {
      val passage = listOf("hello world")
      val currentWord = "hello"
      val userInput = "hello"

      val state =
          GameState.State(
              currentWord = currentWord,
              userInput = userInput,
              score = 0,
              status = GameStatus.GAME_OVER,
              isError = false,
              wpm = 60.0,
              accuracy = 98.5,
              elapsedTime = 65000, // 1m 5s
              passage = passage,
              currentLineIndex = 0,
              countdownStage = 0,
              eventSink = {})

      setContent { GameScreenUi(state, ComposeModifier) }

      val snapshot = awaitSnapshot()

      // Check for content
      assertThat(snapshot).contains("Final WPM")
      assertThat(snapshot).contains("60")
      assertThat(snapshot).contains("Accuracy")
      assertThat(snapshot).contains("98%")
      assertThat(snapshot).contains("Time")
      assertThat(snapshot).contains("1m 5s")

      // Check for new "Results" box components (should fail initially)
      assertThat(snapshot).contains("Results")
      assertThat(snapshot).contains("\u250F") // ┏
      assertThat(snapshot).contains("\u2513") // ┓
      assertThat(snapshot).contains("\u2517") // ┗
      assertThat(snapshot).contains("\u251B") // ┛
    }
  }
}
