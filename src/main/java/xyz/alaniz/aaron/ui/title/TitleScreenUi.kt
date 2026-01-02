package xyz.alaniz.aaron.ui.title

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.layout.KeyEvent
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

private val Enter = KeyEvent("Enter")

private val ArrowUp = KeyEvent("ArrowUp")
private val k = KeyEvent("k")
private val K = KeyEvent("K")

private val ArrowDown = KeyEvent("ArrowDown")
private val j = KeyEvent("j")
private val J = KeyEvent("J")

@Composable
@CircuitInject(screen = TitleScreen::class, scope = AppScope::class)
fun TitleScreenUi(titleScreenState: TitleScreenState, modifier: androidx.compose.ui.Modifier) {
    val modifier = Modifier

    Column(modifier = modifier.onTitleScreenKeyEvent(titleScreenState)) {
        Text(value = "Terminal Velocity")
        Spacer(modifier.height(2))

        titleScreenState.selectableOptions.forEachIndexed { index, option ->
            val underlineStyle = if (index == titleScreenState.selectedTitleScreenOptionIndex) {
                UnderlineStyle.Straight
            } else {
                UnderlineStyle.None
            }
            val textStyle = if (index != titleScreenState.selectedTitleScreenOptionIndex) TextStyle.Dim else TextStyle.Unspecified

            Text(value = option.label, underlineStyle = underlineStyle, textStyle = textStyle)
        }
    }
}

private fun Modifier.onTitleScreenKeyEvent(titleScreenState: TitleScreenState): Modifier {
    return this.onKeyEvent { keyEvent ->
        when (keyEvent) {
            ArrowDown, J, j -> {
                titleScreenState.onEvent(TitleScreenEvent.NextTitleOption)
                return@onKeyEvent true
            }
            ArrowUp, K, k -> {
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
            else -> return@onKeyEvent false
        }
    }
}
