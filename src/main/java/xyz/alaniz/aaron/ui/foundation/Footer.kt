package xyz.alaniz.aaron.ui.foundation

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.layout.width
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Row
import com.jakewharton.mosaic.ui.Spacer
import com.jakewharton.mosaic.ui.Text
import com.jakewharton.mosaic.ui.TextStyle

data class FooterOption(val key: String, val description: String)

@Composable
fun Footer(options: List<FooterOption>, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        options.forEachIndexed { index, option ->
            if (index > 0) {
                 Spacer(Modifier.width(2))
            }
            Text(value = "[${option.key}] ", textStyle = TextStyle.Bold)
            Text(value = option.description, textStyle = TextStyle.Dim)
        }
    }
}
