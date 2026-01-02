package xyz.alaniz.aaron.ui.game

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.ui.Text
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@Composable
@CircuitInject(screen = GameScreen::class, scope = AppScope::class)
fun GameScreenUi(gameScreenState: GameScreenState, modifier: androidx.compose.ui.Modifier) {
    Text("Game screen")
}