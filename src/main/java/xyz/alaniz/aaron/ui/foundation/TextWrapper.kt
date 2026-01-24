package xyz.alaniz.aaron.ui.foundation

object TextWrapper {
  private val ANSI_REGEX = Regex("\\u001B(?:[@-Z\\\\-_]|\\[[0-?]*[ -/]*[@-~])")
  // Sentinel: Added C1 control characters (\u0080-\u009F) to sanitization
  private val CONTROL_CHAR_REGEX =
      Regex("[\\u0000-\\u0008\\u000B\\u000C\\u000E-\\u001F\\u007F\\u0080-\\u009F]")

  fun wrap(text: String, width: Int = 80): List<String> {
    require(width > 0) { "Width must be > 0" }
    val lines = mutableListOf<String>()
    val sanitizedText = text.replace(ANSI_REGEX, "").replace(CONTROL_CHAR_REGEX, "")
    sanitizedText.lines().forEach { line ->
      if (line.length <= width) {
        lines.add(line)
      } else {
        var remaining = line
        while (remaining.isNotEmpty()) {
          if (remaining.length <= width) {
            lines.add(remaining)
            remaining = ""
          } else {
            var breakPoint = remaining.lastIndexOf(' ', width)
            if (breakPoint == -1) {
              // Force break
              breakPoint = width
              lines.add(remaining.take(breakPoint))
              remaining = remaining.drop(breakPoint)
            } else {
              lines.add(remaining.take(breakPoint))
              remaining = remaining.drop(breakPoint).trimStart()
            }
          }
        }
      }
    }
    return lines
  }
}
