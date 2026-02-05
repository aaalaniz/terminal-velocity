package xyz.alaniz.aaron.ui.foundation

object TextWrapper {
  // CSI: Control Sequence Introducer (ESC [ ...)
  private const val CSI_REGEX = "\\[[0-?]{0,1024}[ -/]{0,1024}[@-~]"
  // Variable length Fe: OSC (ESC ]), DCS (ESC P), PM (ESC ^), APC (ESC _)
  // Terminated by BEL or ST (ESC \)
  private const val FE_VAR_REGEX = "[P\\]^_][\\s\\S]{0,4096}?(?:\\u0007|\\u001B\\\\)"
  // Single char Fe: @-Z excluding [, P, ], ^, _
  private const val FE_SINGLE_REGEX = "[@-OQ-Z\\\\]"

  private val ANSI_REGEX = Regex("\\u001B(?:$CSI_REGEX|$FE_VAR_REGEX|$FE_SINGLE_REGEX)")
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
    val sanitizedText =
        text.replace(SANITIZATION_REGEX) { result -> if (result.value == "\t") "  " else "" }

    // Optimization: Use lineSequence to process lines lazily, and index-based slicing
    // to avoid O(N^2) string copying behavior for long lines.
    sanitizedText.lineSequence().forEach { line ->
      val len = line.length
      if (len <= width) {
        val trimmed = line.trimEnd()
        if (trimmed.isNotEmpty()) {
          lines.add(trimmed)
        }
      } else {
        var start = 0
        while (start < len) {
          val remainingLength = len - start
          if (remainingLength <= width) {
            val trimmed = line.substring(start).trimEnd()
            if (trimmed.isNotEmpty()) {
              lines.add(trimmed)
            }
            start = len // Done
          } else {
            // Find last space within the width limit
            var breakPoint = -1
            val searchLimit = start + width
            val candidateBreak = line.lastIndexOf(' ', searchLimit)

            if (candidateBreak >= start) {
              breakPoint = candidateBreak
            }

            if (breakPoint == -1) {
              // Force break at width
              val end = start + width
              val chunk = line.substring(start, end).trimEnd()
              if (chunk.isNotEmpty()) {
                lines.add(chunk)
              }
              start += width
            } else {
              // Break at space
              val chunk = line.substring(start, breakPoint).trimEnd()
              if (chunk.isNotEmpty()) {
                lines.add(chunk)
              }
              // Skip the break space and any subsequent leading spaces for the next line
              // roughly equivalent to remaining.drop(breakPoint).trimStart()
              // drop(breakPoint) effectively keeps the char at breakPoint (if we took up to
              // breakPoint)
              // wait: original code: remaining.take(breakPoint) -> excludes breakPoint char
              // remaining.drop(breakPoint) -> includes breakPoint char
              // trimStart -> removes leading spaces

              start = breakPoint
              while (start < len && line[start] == ' ') {
                start++
              }
            }
          }
        }
      }
    }
    return lines
  }
}
