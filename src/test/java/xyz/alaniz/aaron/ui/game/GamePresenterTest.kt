package xyz.alaniz.aaron.ui.game

import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import xyz.alaniz.aaron.data.WordRepository
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GamePresenterTest {
    private val repository = object : WordRepository {
        override fun getPassage() = listOf("kotlin")
    }

    @Test
    fun `initial state is Waiting`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            val state = awaitItem() as GameState.State
            assertEquals(GameStatus.WAITING, state.status)
            assertEquals("", state.currentWord)
        }
    }

    @Test
    fun `StartGame transitions to Playing and loads passage`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            var state = awaitItem() as GameState.State
            state.eventSink(GameEvent.StartGame)
            
            // Consume states until PLAYING
            while (state.status != GameStatus.PLAYING) {
                state = awaitItem() as GameState.State
            }
            
            assertEquals(GameStatus.PLAYING, state.status)
            assertEquals("kotlin", state.currentWord)
            assertEquals(listOf("kotlin"), state.passage)
        }
    }

    @Test
    fun `typing correct characters updates userInput`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            var state = awaitItem() as GameState.State
            state.eventSink(GameEvent.StartGame)
            
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
            assertEquals("k", state.userInput)
            assertFalse(state.isError)
            
            // Type 'o'
            state.eventSink(GameEvent.LetterTyped('o'))
            while (state.userInput != "ko" && !state.isError) {
                state = awaitItem() as GameState.State
            }
            assertEquals("ko", state.userInput)
        }
    }

    @Test
    fun `typing incorrect character sets isError`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            var state = awaitItem() as GameState.State
            state.eventSink(GameEvent.StartGame)
            
            while (state.status != GameStatus.PLAYING || state.currentWord.isEmpty()) {
                state = awaitItem() as GameState.State
            }
            
            // Type 'z' (wrong)
            state.eventSink(GameEvent.LetterTyped('z'))
            while (!state.isError) {
                state = awaitItem() as GameState.State
            }
            assertTrue(state.isError)
            assertEquals("", state.userInput)
        }
    }

    @Test
    fun `completing the passage transitions to GAME_OVER`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            var state = awaitItem() as GameState.State
            state.eventSink(GameEvent.StartGame)
            
            while (state.status != GameStatus.PLAYING || state.currentWord.isEmpty()) {
                state = awaitItem() as GameState.State
            }
            
            // Type "kotlin"
            "kotlin".forEach { char ->
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
            
            assertEquals(GameStatus.GAME_OVER, state.status)
            assertEquals(1, state.score)
        }
    }

    @Test
    fun `NewGame starts a new game with a passage`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            var state = awaitItem() as GameState.State
            state.eventSink(GameEvent.StartGame)
            
            while (state.status != GameStatus.PLAYING || state.currentWord.isEmpty()) {
                state = awaitItem() as GameState.State
            }
            
            // Just ensure we are in a state that is conceptually "before" NewGame.
            // In the presenter, NewGame just resets everything and fetches a passage.
            // It doesn't strictly check if we are in GAME_OVER.

            state.eventSink(GameEvent.NewGame)

            // Wait for some change. Since we are already PLAYING and have "kotlin",
            // NewGame re-fetches "kotlin" (since mocked repo always returns it)
            // and resets score/input.
            // The status remains PLAYING.
            // The only thing that definitely changes (if we had typed something) is userInput and score.
            // But we haven't typed anything here.

            // Let's modify the test to type something first so we can see the reset.
            state.eventSink(GameEvent.LetterTyped('k'))
            while(state.userInput != "k") {
                state = awaitItem() as GameState.State
            }
            
            // Now trigger NewGame
            state.eventSink(GameEvent.NewGame)

            // Wait for reset
            while (state.userInput.isNotEmpty()) {
                 state = awaitItem() as GameState.State
            }

            assertEquals(GameStatus.PLAYING, state.status)
            assertEquals(0, state.score)
            assertEquals("kotlin", state.currentWord)
            assertEquals(listOf("kotlin"), state.passage)
        }
    }
}
