package xyz.alaniz.aaron.ui.game

import com.google.common.truth.Truth.assertThat
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import xyz.alaniz.aaron.data.WordRepository

class GamePresenterTest {
  private val repository =
      object : WordRepository {
        override suspend fun getPassage() = listOf("kotlin")
      }

  @Test
  fun `Auto-start transitions to Playing`() = runTest {
    val navigator = FakeNavigator(GameScreen)
    val presenter = GameScreenPresenter(navigator, repository)

    presenter.test {
      var state = awaitItem() as GameState.State

      // Wait for PLAYING (consumes Countdown states)
      while (state.status != GameStatus.PLAYING) {
        state = awaitItem() as GameState.State
      }

      assertThat(state.status).isEqualTo(GameStatus.PLAYING)
      assertThat(state.currentWord).isEqualTo("kotlin")
    }
  }

  @Test
  fun `typing correct characters updates userInput`() = runTest {
    val navigator = FakeNavigator(GameScreen)
    val presenter = GameScreenPresenter(navigator, repository)

    presenter.test {
      var state = awaitItem() as GameState.State

      // Wait for playing
      while (state.status != GameStatus.PLAYING || state.currentWord.isEmpty()) {
        state = awaitItem() as GameState.State
      }

      // Type 'k'
      state.eventSink(GameEvent.LetterTyped('k'))
      // Wait for 'k' in userInput
      while (state.userInput != "k" && !state.isError) {
        state = awaitItem() as GameState.State
      }
      assertThat(state.userInput).isEqualTo("k")
      assertThat(state.isError).isFalse()

      // Type 'o'
      state.eventSink(GameEvent.LetterTyped('o'))
      while (state.userInput != "ko" && !state.isError) {
        state = awaitItem() as GameState.State
      }
      assertThat(state.userInput).isEqualTo("ko")
    }
  }

  @Test
  fun `typing incorrect character sets isError`() = runTest {
    val navigator = FakeNavigator(GameScreen)
    val presenter = GameScreenPresenter(navigator, repository)

    presenter.test {
      var state = awaitItem() as GameState.State

      while (state.status != GameStatus.PLAYING || state.currentWord.isEmpty()) {
        state = awaitItem() as GameState.State
      }

      // Type 'z' (wrong)
      state.eventSink(GameEvent.LetterTyped('z'))
      while (!state.isError) {
        state = awaitItem() as GameState.State
      }
      assertThat(state.isError).isTrue()
      assertThat(state.userInput).isEmpty()
    }
  }

  @Test
  fun `completing the passage transitions to GAME_OVER`() = runTest {
    val navigator = FakeNavigator(GameScreen)
    val presenter = GameScreenPresenter(navigator, repository)

    presenter.test {
      var state = awaitItem() as GameState.State

      while (state.status != GameStatus.PLAYING || state.currentWord.isEmpty()) {
        state = awaitItem() as GameState.State
      }

      // Type "kotlin"
      "kotlin"
          .forEach { char ->
            state.eventSink(GameEvent.LetterTyped(char))
            // On last char, wait for GAME_OVER
            if (char == 'n') {
              while (state.status != GameStatus.GAME_OVER) {
                state = awaitItem() as GameState.State
              }
            } else {
              // Just wait for update
              val expectedLength = state.userInput.length + 1
              while (state.userInput.length < expectedLength && !state.isError) {
                state = awaitItem() as GameState.State
              }
            }
          }

      assertThat(state.status).isEqualTo(GameStatus.GAME_OVER)
      assertThat(state.score).isEqualTo(1)
    }
  }

  @Test
  fun `NewGame restarts the game and eventually enters Playing`() = runTest {
    val navigator = FakeNavigator(GameScreen)
    val presenter = GameScreenPresenter(navigator, repository)

    presenter.test {
      var state = awaitItem() as GameState.State

      // Wait for PLAYING
      while (state.status != GameStatus.PLAYING) {
        state = awaitItem() as GameState.State
      }

      // Type something
      state.eventSink(GameEvent.LetterTyped('k'))
      while (state.userInput != "k") {
        state = awaitItem() as GameState.State
      }

      // Trigger NewGame
      state.eventSink(GameEvent.NewGame)

      // It should eventually reach PLAYING (after Countdown)
      // We consume intermediate states (COUNTDOWN)
      state = awaitItem() as GameState.State
      while (state.status != GameStatus.PLAYING) {
        state = awaitItem() as GameState.State
      }

      assertThat(state.status).isEqualTo(GameStatus.PLAYING)
      assertThat(state.userInput).isEmpty()
      assertThat(state.score).isEqualTo(0)
    }
  }
}
