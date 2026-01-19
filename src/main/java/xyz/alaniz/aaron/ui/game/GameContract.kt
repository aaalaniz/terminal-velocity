package xyz.alaniz.aaron.ui.game

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

sealed interface GameState : CircuitUiState {
  data class State(
      val currentWord: String,
      val userInput: String,
      val score: Int,
      val status: GameStatus,
      val isError: Boolean = false,
      val wpm: Double = 0.0,
      val accuracy: Double = 100.0,
      val elapsedTime: Long = 0,
      val passage: List<String> = emptyList(),
      val currentLineIndex: Int = 0,
      val countdownStage: Int = 0,
      val eventSink: (GameEvent) -> Unit
  ) : GameState
}

enum class GameStatus {
  COUNTDOWN,
  PLAYING,
  GAME_OVER
}

sealed interface GameEvent : CircuitUiEvent {
  data class LetterTyped(val char: Char) : GameEvent

  data object StartGame : GameEvent

  data object RetryGame : GameEvent

  data object NewGame : GameEvent

  data object ReturnToMenu : GameEvent

  data object ClearError : GameEvent
}
