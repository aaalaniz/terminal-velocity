package xyz.alaniz.aaron.ui.foundation

object TextWrapper {
  private val ANSI_REGEX = Regex("\\u001B(?:[@-Z\\\\-_]|\\[[0-?]*[ -/]*[@-~])")
  // Include C0 (0x00-0x1F excluding tabs/newlines) and C1 (0x80-0x9F) control characters
  private val CONTROL_CHAR_REGEX =
      Regex("[\\u0000-\\u0008\\u000B\\u000C\\u000E-\\u001F\\u007F\\u0080-\\u009F]")
  // Combine regexes to allow single-pass replacement. ANSI must be first to correctly consume
  // escape sequences that start with control characters (like ESC).
  private val SANITIZATION_REGEX =
      Regex("(${ANSI_REGEX.pattern})|(${CONTROL_CHAR_REGEX.pattern})|(\\t)")

  fun wrap(text: String, width: Int = 80): List<String> {
    require(width > 0) { "Width must be > 0" }
    val lines = mutableListOf<String>()
    // Sanitize control characters and expand tabs to spaces (prevent TUI spoofing)
    // Optimize: Process line by line to avoid allocating a full sanitized copy of text
    text.lineSequence().forEach { rawLine ->
      val line =
          rawLine.replace(SANITIZATION_REGEX) { result -> if (result.value == "\t") "  " else "" }
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
