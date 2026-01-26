package xyz.alaniz.aaron.ui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jakewharton.mosaic.layout.width
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Box
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Row
import com.jakewharton.mosaic.ui.Text

@Composable
fun BorderedTitledContent(
    title: String,
    width: Int,
    height: Int,
    color: Color = Color.Cyan,
    content: @Composable () -> Unit
) {
  require(width >= title.length + 6) { "Width must be large enough to contain title and borders" }
  require(height > 0) { "Height must be > 0" }

  val topBorder =
      remember(title, width) {
        buildString {
          append("┏━ ")
          append(title)
          append(" ")
          val remaining = width - length - 1
          if (remaining > 0) {
            append("━".repeat(remaining))
          }
          append("┓")
        }
      }

  val bottomBorder =
      remember(width) {
        buildString {
          append("┗")
          append("━".repeat(width - 2))
          append("┛")
        }
      }

  val verticalBorder = remember(height) { List(height) { "┃" }.joinToString("\n") }

  Column {
    Text(topBorder, color = color)
    Row {
      Text(verticalBorder, color = color)
      Box(modifier = Modifier.width(width - 2)) { content() }
      Text(verticalBorder, color = color)
    }
    Text(bottomBorder, color = color)
  }
}
