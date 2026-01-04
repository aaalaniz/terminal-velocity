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
        val eventSink: (GameEvent) -> Unit
    ) : GameState
}

enum class GameStatus {
    WAITING,
    PLAYING,
    GAME_OVER
}

sealed interface GameEvent : CircuitUiEvent {
    data class LetterTyped(val char: Char) : GameEvent
    data object GameStarted : GameEvent
    data object GameReset : GameEvent
    data object ClearError : GameEvent
}