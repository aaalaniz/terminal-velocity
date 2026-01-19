package xyz.alaniz.aaron.ui.game

import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import xyz.alaniz.aaron.data.WordRepository

class PassageGamePresenterTest {
  private val passage = listOf("First line.", "Second line.")
  private val repository =
      object : WordRepository {
        override fun getPassage() = passage
      }

  @Test
  fun `Countdown picks the first line of the passage`() = runTest {
    val navigator = FakeNavigator(GameScreen)
    val presenter = GameScreenPresenter(navigator, repository)

    presenter.test {
      var state = awaitItem() as GameState.State
      // Wait for countdown to finish
      while (state.status != GameStatus.PLAYING || state.currentWord.isEmpty()) {
        state = awaitItem() as GameState.State
      }

      assertEquals("First line.", state.currentWord)
    }
  }

  @Test
  fun `Completing a line picks the next line of the passage`() = runTest {
    val navigator = FakeNavigator(GameScreen)
    val presenter = GameScreenPresenter(navigator, repository)

    presenter.test {
      var state = awaitItem() as GameState.State

      while (state.status != GameStatus.PLAYING || state.currentWord != "First line.") {
        state = awaitItem() as GameState.State
      }

      // Type the first line
      "First line."
          .forEach { char ->
            state.eventSink(GameEvent.LetterTyped(char))

            // Wait for either the word to update or the final state
            if (char == '.') {
              while (state.currentWord != "Second line.") {
                state = awaitItem() as GameState.State
              }
            } else {
              // Just wait for some change
              val prevInput = state.userInput
              while (state.userInput == prevInput && !state.isError) {
                state = awaitItem() as GameState.State
              }
            }
          }

      assertEquals("Second line.", state.currentWord)
      assertEquals("", state.userInput)
    }
  }
}
