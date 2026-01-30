package xyz.alaniz.aaron.ui.game

import androidx.compose.ui.Modifier as ComposeModifier
import com.google.common.truth.Truth.assertThat
import com.jakewharton.mosaic.testing.runMosaicTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class GameScreenUiPerformanceTest {
  @Test
  fun testMultipleLinesRendering() = runTest {
    runMosaicTest {
      val passage =
          listOf("Line 1 (Completed)", "Line 2 (Current)", "Line 3 (Future)", "Line 4 (Future)")
      val currentWord = "Line 2 (Current)"
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
              currentLineIndex = 1, // Line 2 is current
              countdownStage = 0,
              eventSink = {})

      setContent { GameScreenUi(state, ComposeModifier) }

      val snapshot = awaitSnapshot()

      // Verify all lines are present in the output
      assertThat(snapshot).contains("Line 1 (Completed)")
      assertThat(snapshot).contains("Line 2 (Current)")
      assertThat(snapshot).contains("Line 3 (Future)")
      assertThat(snapshot).contains("Line 4 (Future)")
    }
  }
}
