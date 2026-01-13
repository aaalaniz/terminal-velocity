package xyz.alaniz.aaron.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.jakewharton.mosaic.layout.height
import com.jakewharton.mosaic.layout.onKeyEvent
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Row
import com.jakewharton.mosaic.ui.Spacer
import com.jakewharton.mosaic.ui.Text
import com.jakewharton.mosaic.ui.TextStyle
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import kotlinx.coroutines.delay
import xyz.alaniz.aaron.ui.foundation.Footer
import xyz.alaniz.aaron.ui.foundation.FooterOption
import xyz.alaniz.aaron.ui.foundation.KeyEvents.CtrlC
import xyz.alaniz.aaron.ui.foundation.KeyEvents.Enter
import xyz.alaniz.aaron.ui.foundation.KeyEvents.Esc
import xyz.alaniz.aaron.ui.foundation.KeyEvents.R
import xyz.alaniz.aaron.ui.foundation.KeyEvents.r
import kotlin.system.exitProcess

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
                CtrlC -> {
                    exitProcess(0)
                }
                Enter -> {
                    if (state.status == GameStatus.WAITING) {
                        state.eventSink(GameEvent.StartGame)
                    } else if (state.status == GameStatus.GAME_OVER) {
                        state.eventSink(GameEvent.NewGame)
                    }
                    true
                }
                Esc -> {
                    state.eventSink(GameEvent.ReturnToMenu)
                    true
                }
                else -> {
                    if (state.status == GameStatus.PLAYING) {
                        val key = keyEvent.key
                        if (key.length == 1) {
                            state.eventSink(GameEvent.LetterTyped(key[0]))
                            true
                        } else {
                            false
                        }
                    } else if (state.status == GameStatus.GAME_OVER) {
                        if (keyEvent == r || keyEvent == R) {
                            state.eventSink(GameEvent.RetryGame)
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
            if (state.status == GameStatus.WAITING) {
                Text("Terminal Velocity")
                Spacer(Modifier.height(1))
                Footer(
                    options = listOf(
                        FooterOption("Enter", "Start"),
                        FooterOption("Esc", "Back"),
                    )
                )
            } else if (state.status == GameStatus.PLAYING) {
                Text("WPM: ${state.wpm.toInt()} | Accuracy: ${state.accuracy.toInt()}%")
                Spacer(Modifier.height(1))

                // Show a window of 5 lines
                val windowSize = 5
                val halfWindow = windowSize / 2
                val startIndex = (state.currentLineIndex - halfWindow).coerceAtLeast(0)
                val endIndex = (startIndex + windowSize).coerceAtMost(state.passage.size)

                for (i in startIndex until endIndex) {
                    if (i == state.currentLineIndex) {
                        Row {
                            Text(state.userInput)
                            val remaining = state.currentWord.drop(state.userInput.length)
                            if (remaining.isNotEmpty()) {
                                if (state.isError) {
                                    val errorChar = remaining.take(1)
                                    if (errorChar == " ") {
                                        Text("_", color = Color.Red)
                                    } else {
                                        Text(errorChar, color = Color.Red)
                                    }
                                    Text(remaining.drop(1), textStyle = TextStyle.Dim)
                                } else {
                                    Text(remaining, textStyle = TextStyle.Dim)
                                }
                            }
                        }
                    } else if (i < state.currentLineIndex) {
                        // Completed lines
                        Text(state.passage[i])
                    } else {
                        // Future lines
                        Text(state.passage[i], textStyle = TextStyle.Dim)
                    }
                }
                Spacer(Modifier.height(1))
                Footer(
                    options = listOf(
                        FooterOption("Ctrl-C", "Quit"),
                        FooterOption("Esc", "Menu"),
                    )
                )
            } else if (state.status == GameStatus.GAME_OVER) {
                val totalSeconds = state.elapsedTime / 1000
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60

                Text("Passage Complete!", color = Color.Green)
                Text("")
                Text("Final WPM: ${state.wpm.toInt()}")
                Text("Accuracy: ${state.accuracy.toInt()}%")
                Text("Time: ${minutes}m ${seconds}s")
                Spacer(Modifier.height(1))
                Footer(
                    options = listOf(
                        FooterOption("Enter", "New Passage"),
                        FooterOption("R", "Retry"),
                        FooterOption("Esc", "Menu"),
                    )
                )
            }
        }
    }
}
