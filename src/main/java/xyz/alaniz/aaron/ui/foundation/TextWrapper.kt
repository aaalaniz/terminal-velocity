package xyz.alaniz.aaron.ui.foundation

object TextWrapper {
  fun wrap(text: String, width: Int = 80): List<String> {
    require(width > 0) { "Width must be > 0" }
    val lines = mutableListOf<String>()
    text.lines().forEach { line ->
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
