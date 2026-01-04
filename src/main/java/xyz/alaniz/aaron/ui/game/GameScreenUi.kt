package xyz.alaniz.aaron.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.jakewharton.mosaic.layout.onKeyEvent
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Row
import com.jakewharton.mosaic.ui.Text
import com.jakewharton.mosaic.ui.TextStyle
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import kotlinx.coroutines.delay
import xyz.alaniz.aaron.ui.foundation.KeyEvents.Enter

@Composable
@CircuitInject(screen = GameScreen::class, scope = AppScope::class)
fun GameScreenUi(state: GameState, modifier: androidx.compose.ui.Modifier) {
    if (state is GameState.State) {
        if (state.isError) {
            LaunchedEffect(state.isError) {
                delay(150)
                state.eventSink(GameEvent.ClearError)
            }
        }

        Column(modifier = Modifier.onKeyEvent { keyEvent ->
            when (keyEvent) {
                Enter -> {
                    if (state.status == GameStatus.WAITING) {
                        state.eventSink(GameEvent.GameStarted)
                    } else if (state.status == GameStatus.GAME_OVER) {
                        state.eventSink(GameEvent.GameReset)
                    }
                    true
                }
                else -> {
                    if (state.status == GameStatus.PLAYING) {
                        val key = keyEvent.key
                        if (key != null && key.length == 1) {
                            state.eventSink(GameEvent.LetterTyped(key[0]))
                            true
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }
            }
        }) {
            Text("Score: ${state.score}")
            Text("")

            when (state.status) {
                GameStatus.WAITING -> {
                    Text("Press 'Enter' to Start")
                }
                GameStatus.PLAYING -> {
                    Row {
                        // Correctly typed letters: Bright (Default)
                        Text(state.userInput)
                        // Remaining letters: Dimmed or current char Red if error
                        val remaining = state.currentWord.drop(state.userInput.length)
                        if (remaining.isNotEmpty()) {
                            if (state.isError) {
                                // Flash only the first remaining character red
                                Text(remaining.take(1), color = Color.Red)
                                Text(remaining.drop(1), textStyle = TextStyle.Dim)
                            } else {
                                Text(remaining, textStyle = TextStyle.Dim)
                            }
                        }
                    }
                }
                GameStatus.GAME_OVER -> {
                    Text("GAME OVER!", color = Color.Red)
                    Text("Score: ${state.score}")
                    Text("Press 'Enter' to Reset")
                }
            }
        }
    }
}