package xyz.alaniz.aaron.ui.settings

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.layout.onKeyEvent
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.layout.height
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Spacer
import com.jakewharton.mosaic.ui.Text
import com.jakewharton.mosaic.ui.TextStyle
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import xyz.alaniz.aaron.ui.foundation.KeyEvents.b

@Composable
@CircuitInject(screen = SettingsScreen::class, scope = AppScope::class)
fun SettingsScreenUi(settingsScreenState: SettingsScreenState, modifier: androidx.compose.ui.Modifier) {
    val modifier = Modifier

    Column(modifier = modifier.onSettingsScreenKeyEvent(settingsScreenState)) {
        Text("Settings screen")
        Spacer(Modifier.height(1))
        Text("[b] Back", textStyle = TextStyle.Dim)
    }
}

private fun Modifier.onSettingsScreenKeyEvent(settingsScreenState: SettingsScreenState): Modifier {
    return this.onKeyEvent { keyEvent ->
        when (keyEvent) {
            b -> {
                settingsScreenState.onEvent(SettingsScreenEvent.OnBack)
                return@onKeyEvent true
            }
            else -> return@onKeyEvent false
        }
    }
}