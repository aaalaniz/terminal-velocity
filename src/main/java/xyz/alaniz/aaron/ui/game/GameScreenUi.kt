package xyz.alaniz.aaron.ui.game

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Row
import com.jakewharton.mosaic.ui.Text
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@Composable
@CircuitInject(screen = GameScreen::class, scope = AppScope::class)
fun GameScreenUi(state: GameState, modifier: androidx.compose.ui.Modifier) {
    if (state is GameState.State) {
        Column {
            Text("Score: ${state.score}")
            Text("")

            when (state.status) {
                GameStatus.WAITING -> {
                    Text("Press 'Enter' to Start")
                }
                GameStatus.PLAYING -> {
                    Row {
                        Text(state.userInput, color = Color.Green)
                        Text(state.currentWord.drop(state.userInput.length))
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