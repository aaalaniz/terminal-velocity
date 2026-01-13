package xyz.alaniz.aaron.ui.title

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.layout.height
import com.jakewharton.mosaic.layout.onKeyEvent
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Spacer
import com.jakewharton.mosaic.ui.Text
import com.jakewharton.mosaic.ui.TextStyle
import com.jakewharton.mosaic.ui.UnderlineStyle
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import xyz.alaniz.aaron.ui.foundation.Footer
import xyz.alaniz.aaron.ui.foundation.FooterOption
import xyz.alaniz.aaron.ui.foundation.KeyEvents.ArrowDown
import xyz.alaniz.aaron.ui.foundation.KeyEvents.ArrowUp
import xyz.alaniz.aaron.ui.foundation.KeyEvents.Enter
import xyz.alaniz.aaron.ui.foundation.KeyEvents.Esc
import xyz.alaniz.aaron.ui.foundation.KeyEvents.j
import xyz.alaniz.aaron.ui.foundation.KeyEvents.k

@Composable
@CircuitInject(screen = TitleScreen::class, scope = AppScope::class)
fun TitleScreenUi(titleScreenState: TitleScreenState, modifier: androidx.compose.ui.Modifier) {
    val modifier = Modifier

    Column(modifier = modifier.onTitleScreenKeyEvent(titleScreenState)) {
        Text(value = "Terminal Velocity")
        Spacer(Modifier.height(2))

        titleScreenState.selectableOptions.forEachIndexed { index, option ->
            val underlineStyle = if (index == titleScreenState.selectedTitleScreenOptionIndex) {
                UnderlineStyle.Straight
            } else {
                UnderlineStyle.None
            }
            val textStyle = if (index != titleScreenState.selectedTitleScreenOptionIndex) TextStyle.Dim else TextStyle.Unspecified

            val prefix = if (index == titleScreenState.selectedTitleScreenOptionIndex) "> " else "  "
            Text(value = prefix + option.label, underlineStyle = underlineStyle, textStyle = textStyle)
        }

        Spacer(Modifier.height(2))
        Footer(
            options = listOf(
                FooterOption("↑/↓", "Navigate"),
                FooterOption("Enter", "Select"),
                FooterOption("Esc", "Quit"),
            )
        )
    }
}

private fun Modifier.onTitleScreenKeyEvent(titleScreenState: TitleScreenState): Modifier {
    return this.onKeyEvent { keyEvent ->
        when (keyEvent) {
            ArrowDown, j -> {
                titleScreenState.onEvent(TitleScreenEvent.NextTitleOption)
                return@onKeyEvent true
            }
            ArrowUp, k -> {
                titleScreenState.onEvent(TitleScreenEvent.PreviousTitleOption)
                return@onKeyEvent true
            }
            Enter -> {
                val selectedTitleOption = titleScreenState
                    .selectableOptions[titleScreenState.selectedTitleScreenOptionIndex]
                titleScreenState.onEvent(TitleScreenEvent.TitleOptionSelected(
                    titleScreenOption = selectedTitleOption))
                return@onKeyEvent true
            }
            Esc -> {
                titleScreenState.onEvent(TitleScreenEvent.Quit)
                return@onKeyEvent true
            }
            else -> return@onKeyEvent false
        }
    }
}