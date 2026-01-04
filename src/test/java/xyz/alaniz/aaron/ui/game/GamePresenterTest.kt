package xyz.alaniz.aaron.ui.game

import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import xyz.alaniz.aaron.data.WordRepository
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GamePresenterTest {
    private val repository = object : WordRepository {
        // Simple consistent sequence or just always returns "kotlin"
        override fun nextWord() = "kotlin"
    }

    @Test
    fun `initial state is Waiting`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            val initialState = awaitItem()
            assertTrue(initialState is GameState.State)
            assertEquals(GameStatus.WAITING, initialState.status)
        }
    }

    @Test
    fun `GameStarted event transitions to Playing and picks word`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            val initialState = awaitItem() as GameState.State
            initialState.eventSink(GameEvent.GameStarted)
            
            var playingState = awaitItem() as GameState.State
            // Handle potential partial emissions
            while (playingState.currentWord.isEmpty() && playingState.status == GameStatus.PLAYING) {
                playingState = awaitItem() as GameState.State
            }
            
            assertEquals(GameStatus.PLAYING, playingState.status)
            assertEquals("kotlin", playingState.currentWord)
            assertEquals("", playingState.userInput)
        }
    }

    @Test
    fun `LetterTyped event updates userInput if correct`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            var state = awaitItem() as GameState.State
            state.eventSink(GameEvent.GameStarted)
            
            // Wait for playing state
            while (state.status != GameStatus.PLAYING || state.currentWord.isEmpty()) {
                state = awaitItem() as GameState.State
            }
            
            // Type 'k' (correct)
            state.eventSink(GameEvent.LetterTyped('k'))
            
            // Expect update
            var typedState = awaitItem() as GameState.State
            while (typedState.userInput.isEmpty()) {
                typedState = awaitItem() as GameState.State
            }
            
            assertEquals("k", typedState.userInput)
        }
    }

    @Test
    fun `LetterTyped event ignores incorrect input`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            var state = awaitItem() as GameState.State
            state.eventSink(GameEvent.GameStarted)
            
             // Wait for playing state
            while (state.status != GameStatus.PLAYING || state.currentWord.isEmpty()) {
                state = awaitItem() as GameState.State
            }
            
            // Type 'z' (incorrect, expected 'k')
            state.eventSink(GameEvent.LetterTyped('z'))
            
            // Should not emit new state
            expectNoEvents()
        }
    }

    @Test
    fun `LetterTyped event triggers error state on incorrect input`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            var state = awaitItem() as GameState.State
            state.eventSink(GameEvent.GameStarted)
            
             // Wait for playing state
            while (state.status != GameStatus.PLAYING || state.currentWord.isEmpty()) {
                state = awaitItem() as GameState.State
            }
            
            // Type 'z' (incorrect)
            state.eventSink(GameEvent.LetterTyped('z'))
            
            // Expect error state
            var errorState = awaitItem() as GameState.State
            while (!errorState.isError) {
                errorState = awaitItem() as GameState.State
            }
            
            assertEquals(true, errorState.isError)
        }
    }

    @Test
    fun `ClearError event resets error state`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            var state = awaitItem() as GameState.State
            state.eventSink(GameEvent.GameStarted)
            
             // Wait for playing state
            while (state.status != GameStatus.PLAYING || state.currentWord.isEmpty()) {
                state = awaitItem() as GameState.State
            }
            
            // Type 'z' (incorrect)
            state.eventSink(GameEvent.LetterTyped('z'))
            
            // Wait for error
            var errorState = awaitItem() as GameState.State
            while (!errorState.isError) {
                errorState = awaitItem() as GameState.State
            }
            
            // Clear error
            errorState.eventSink(GameEvent.ClearError)
            
            // Expect no error
            var clearState = awaitItem() as GameState.State
            while (clearState.isError) {
                clearState = awaitItem() as GameState.State
            }
            assertEquals(false, clearState.isError)
        }
    }

    @Test
    fun `Completing a word increments score and resets input`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            var state = awaitItem() as GameState.State
            state.eventSink(GameEvent.GameStarted)
            
             // Wait for playing state
            while (state.status != GameStatus.PLAYING || state.currentWord.isEmpty()) {
                state = awaitItem() as GameState.State
            }
            
            // word is "kotlin"
            "kotlin".forEach { char ->
                state.eventSink(GameEvent.LetterTyped(char))
                state = awaitItem() as GameState.State
                
                if (char == 'n') {
                     // Wait for score update and input reset (might be separate emissions)
                     while (state.score == 0 || state.userInput.isNotEmpty()) {
                         state = awaitItem() as GameState.State
                     }
                }
            }
            
            assertEquals(1, state.score)
            assertEquals("", state.userInput)
            assertEquals("kotlin", state.currentWord)
        }
    }

    @Test
    fun `GameReset resets state`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            var state = awaitItem() as GameState.State
            state.eventSink(GameEvent.GameStarted)
            
             // Wait for playing state
            while (state.status != GameStatus.PLAYING || state.currentWord.isEmpty()) {
                state = awaitItem() as GameState.State
            }
            
            state.eventSink(GameEvent.GameReset)
            var resetState = awaitItem() as GameState.State
            
            // Wait for full reset
            while (resetState.status != GameStatus.WAITING || resetState.currentWord.isNotEmpty()) {
                 resetState = awaitItem() as GameState.State
            }
            
            assertEquals(GameStatus.WAITING, resetState.status)
            assertEquals(0, resetState.score)
            assertEquals("", resetState.currentWord)
        }
    }
}