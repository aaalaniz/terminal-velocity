package xyz.alaniz.aaron.ui.settings

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.ui.Text
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@Composable
@CircuitInject(screen = SettingsScreen::class, scope = AppScope::class)
fun SettingsScreenUi(settingsScreenState: SettingsScreenState, modifier: androidx.compose.ui.Modifier) {
    Text("Settings screen")
}