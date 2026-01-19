package xyz.alaniz.aaron.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import xyz.alaniz.aaron.data.WordRepository

@AssistedInject
class GameScreenPresenter(
    @Assisted private val navigator: Navigator,
    private val repository: WordRepository
) : Presenter<GameState> {

  @AssistedFactory
  @CircuitInject(GameScreen::class, AppScope::class)
  fun interface Factory {
    fun create(navigator: Navigator): GameScreenPresenter
  }

  @Composable
  override fun present(): GameState {
    var status by remember { mutableStateOf(GameStatus.WAITING) }
    var currentWord by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }
    var score by remember { mutableIntStateOf(0) }
    var isError by remember { mutableStateOf(false) }

    var passage by remember { mutableStateOf(emptyList<String>()) }
    var currentLineIndex by remember { mutableIntStateOf(0) }
    var totalCorrectChars by remember { mutableIntStateOf(0) }
    var totalKeystrokes by remember { mutableIntStateOf(0) }
    var startTime by remember { mutableLongStateOf(0L) }
    var elapsedTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
      if (passage.isEmpty()) {
        passage = repository.getPassage()
      }
    }

    fun calculateWpm(): Double {
      if (startTime == 0L) return 0.0
      val minutes = (elapsedTime / 1000.0) / 60.0
      if (minutes <= 0) return 0.0
      return (totalCorrectChars / 5.0) / minutes
    }

    fun calculateAccuracy(): Double {
      if (totalKeystrokes == 0) return 100.0
      return (totalCorrectChars.toDouble() / totalKeystrokes.toDouble()) * 100.0
    }

    fun resetGameStats() {
      status = GameStatus.PLAYING
      currentLineIndex = 0
      currentWord = passage.getOrElse(0) { "" }
      userInput = currentWord.takeWhile { it.isWhitespace() }
      isError = false
      score = 0
      totalCorrectChars = 0
      totalKeystrokes = 0
      startTime = 0L
      elapsedTime = 0L
    }

    return GameState.State(
        currentWord = currentWord,
        userInput = userInput,
        score = score,
        status = status,
        isError = isError,
        wpm = calculateWpm(),
        accuracy = calculateAccuracy(),
        elapsedTime = elapsedTime,
        passage = passage,
        currentLineIndex = currentLineIndex,
        eventSink = { event ->
          Snapshot.withMutableSnapshot {
            when (event) {
              GameEvent.StartGame -> {
                if (passage.isEmpty()) {
                  passage = repository.getPassage()
                }
                resetGameStats()
              }
              GameEvent.RetryGame -> {
                // Keep the same passage
                resetGameStats()
              }
              GameEvent.NewGame -> {
                passage = repository.getPassage()
                resetGameStats()
              }
              GameEvent.ReturnToMenu -> {
                navigator.pop()
              }
              is GameEvent.LetterTyped -> {
                if (status == GameStatus.PLAYING) {
                  val now = System.currentTimeMillis()
                  if (startTime == 0L) {
                    startTime = now
                  }
                  elapsedTime = now - startTime
                  totalKeystrokes++

                  val nextCharIndex = userInput.length
                  if (nextCharIndex < currentWord.length) {
                    if (currentWord[nextCharIndex] == event.char) {
                      userInput += event.char
                      isError = false
                      totalCorrectChars++

                      if (userInput == currentWord) {
                        score++
                        currentLineIndex++
                        if (currentLineIndex < passage.size) {
                          currentWord = passage[currentLineIndex]
                          userInput = currentWord.takeWhile { it.isWhitespace() }
                        } else {
                          status = GameStatus.GAME_OVER
                        }
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
        })
  }
}
