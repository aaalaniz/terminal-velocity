package xyz.alaniz.aaron.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import xyz.alaniz.aaron.data.WordRepository

@AssistedInject
class GameScreenPresenter (
    @Assisted private val navigator: Navigator,
    private val repository: WordRepository
) : Presenter<GameState> {

    @AssistedFactory
    @CircuitInject(GameScreen::class, AppScope::class)
    fun interface Factory {
        fun create(
            navigator: Navigator
        ): GameScreenPresenter
    }

    @Composable
    override fun present(): GameState {
        var status by remember { mutableStateOf(GameStatus.WAITING) }
        var currentWord by remember { mutableStateOf("") }
        var userInput by remember { mutableStateOf("") }
        var score by remember { mutableStateOf(0) }
        var isError by remember { mutableStateOf(false) }

        return GameState.State(
            currentWord = currentWord,
            userInput = userInput,
            score = score,
            status = status,
            isError = isError,
            eventSink = { event ->
                when (event) {
                    GameEvent.GameStarted -> {
                        status = GameStatus.PLAYING
                        currentWord = repository.nextWord()
                        userInput = ""
                        isError = false
                    }
                    GameEvent.GameReset -> {
                         status = GameStatus.WAITING
                         score = 0
                         currentWord = ""
                         userInput = ""
                         isError = false
                    }
                    is GameEvent.LetterTyped -> {
                        if (status == GameStatus.PLAYING) {
                            val nextCharIndex = userInput.length
                            if (nextCharIndex < currentWord.length) {
                                if (currentWord[nextCharIndex] == event.char) {
                                    userInput += event.char
                                    isError = false
                                    
                                    if (userInput == currentWord) {
                                        score++
                                        currentWord = repository.nextWord()
                                        userInput = ""
                                    }
                                } else {
                                    isError = true
                                }
                            }
                        }
                    }
                    GameEvent.ClearError -> {
                        isError = false
                    }
                }
            }
        )
    }
}