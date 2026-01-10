package xyz.alaniz.aaron.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.jakewharton.mosaic.layout.height
import com.jakewharton.mosaic.layout.onKeyEvent
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Box
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Row
import com.jakewharton.mosaic.ui.Text
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Spacer
import com.jakewharton.mosaic.ui.TextStyle
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import xyz.alaniz.aaron.ui.foundation.Footer
import xyz.alaniz.aaron.ui.foundation.FooterOption
import xyz.alaniz.aaron.ui.foundation.KeyEvents.ArrowDown
import xyz.alaniz.aaron.ui.foundation.KeyEvents.ArrowUp
import xyz.alaniz.aaron.ui.foundation.KeyEvents.Enter
import xyz.alaniz.aaron.ui.foundation.KeyEvents.Esc
import xyz.alaniz.aaron.ui.foundation.KeyEvents.Space
import xyz.alaniz.aaron.ui.foundation.KeyEvents.b
import xyz.alaniz.aaron.ui.foundation.KeyEvents.j
import xyz.alaniz.aaron.ui.foundation.KeyEvents.k

@Composable
@CircuitInject(screen = SettingsScreen::class, scope = AppScope::class)
fun SettingsScreenUi(settingsScreenState: SettingsScreenState, modifier: androidx.compose.ui.Modifier) {
    val modifier = Modifier

    Box(modifier = modifier.onSettingsScreenKeyEvent(settingsScreenState)) {
        Column {
            Text("Settings")
            Spacer(Modifier.height(1))

            settingsScreenState.items.forEachIndexed { index, item ->
                val isFocused = index == settingsScreenState.focusedIndex
                val prefix = if (isFocused) "> " else "  "
                val checkbox = if (item.isChecked) "[x]" else "[ ]"
                val indent = "  ".repeat(item.indentLevel)

                Row {
                    Text(prefix)
                    Text(indent)
                    Text(checkbox)
                    Text(" ")
                    Text(
                        item.label,
                        color = if (isFocused) Color.Green else Color.White,
                        textStyle = if (isFocused) TextStyle.Bold else TextStyle.Unspecified
                    )
                }
            }
            Spacer(Modifier.height(1))
            Footer(
                options = listOf(
                    FooterOption("↑/↓", "Navigate"),
                    FooterOption("Enter", "Select"),
                    FooterOption("Esc", "Back"),
                )
            )
        }
    }
}

private fun Modifier.onSettingsScreenKeyEvent(settingsScreenState: SettingsScreenState): Modifier {
    return this.onKeyEvent { keyEvent ->
        when (keyEvent) {
            Esc, b -> {
                settingsScreenState.onEvent(SettingsScreenEvent.OnBack)
                return@onKeyEvent true
            }
            ArrowUp, k -> {
                settingsScreenState.onEvent(SettingsScreenEvent.MoveFocusUp)
                return@onKeyEvent true
            }
            ArrowDown, j -> {
                settingsScreenState.onEvent(SettingsScreenEvent.MoveFocusDown)
                return@onKeyEvent true
            }
            Enter, Space -> {
                settingsScreenState.onEvent(SettingsScreenEvent.ToggleCurrentItem)
                return@onKeyEvent true
            }
            else -> return@onKeyEvent false
        }
    }
}
