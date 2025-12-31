package xyz.alaniz.aaron.ui.title

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.layout.height
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Spacer
import com.jakewharton.mosaic.ui.Text
import com.jakewharton.mosaic.ui.UnderlineStyle
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@Composable
@CircuitInject(screen = TitleScreen::class, scope = AppScope::class)
fun TitleScreenUi(titleScreenState: TitleScreenState, modifier: androidx.compose.ui.Modifier) {
    val modifier = Modifier
    Column {
        Text(value = "Terminal Velocity")
        Spacer(modifier.height(2))
        Text(value = "Start game", underlineStyle = UnderlineStyle.Straight)
        Text(value = "Options")
    }
}
