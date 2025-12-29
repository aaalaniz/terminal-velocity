package xyz.alaniz.aaron.ui.title

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jakewharton.mosaic.ui.Text
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@Composable
@CircuitInject(screen = TitleScreen::class, scope = AppScope::class)
fun TitleScreenUi(titleScreenState: TitleScreenState, modifier: Modifier) {
    Text(value = "Title Screen")
}