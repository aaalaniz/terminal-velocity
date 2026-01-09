package xyz.alaniz.aaron.ui.game

import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import xyz.alaniz.aaron.data.WordRepository
import kotlin.test.assertEquals

class PassageGamePresenterTest {
    private val passage = listOf("First line.", "Second line.")
    private val repository = object : WordRepository {
        override fun getPassage() = passage
    }

    @Test
    fun `StartGame picks the first line of the passage`() = runTest {
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, repository)
        
        presenter.test {
            val waitingState = awaitItem() as GameState.State
            waitingState.eventSink(GameEvent.StartGame)
            
            var state = awaitItem() as GameState.State
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
            val waitingState = awaitItem() as GameState.State
            waitingState.eventSink(GameEvent.StartGame)
            
            var state = awaitItem() as GameState.State
            while (state.status != GameStatus.PLAYING || state.currentWord != "First line.") {
                state = awaitItem() as GameState.State
            }
            
            // Type the first line
            "First line.".forEach { char ->
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

    @Test
    fun `Completing a line auto-advances indentation`() = runTest {
        val indentedPassage = listOf("Line 1", "  Line 2")
        val indentedRepository = object : WordRepository {
            override fun getPassage() = indentedPassage
        }
        val navigator = FakeNavigator(GameScreen)
        val presenter = GameScreenPresenter(navigator, indentedRepository)

        presenter.test {
            val waitingState = awaitItem() as GameState.State
            waitingState.eventSink(GameEvent.StartGame)

            var state = awaitItem() as GameState.State
            while (state.status != GameStatus.PLAYING || state.currentWord != "Line 1") {
                state = awaitItem() as GameState.State
            }

            // Type the first line
            "Line 1".forEach { char ->
                state.eventSink(GameEvent.LetterTyped(char))

                // Wait for state update if not last char
                if (char != '1') {
                    // This wait is tricky because we need to wait for a specific change or just consume
                    // But here we can rely on the final check
                }
            }

            // After typing "Line 1", it should transition to "  Line 2" and auto-fill "  "
            // We need to consume events until we see the new word
            while (state.currentWord != "  Line 2") {
                state = awaitItem() as GameState.State
            }

            assertEquals("  Line 2", state.currentWord)
            assertEquals("  ", state.userInput)
        }
    }
}