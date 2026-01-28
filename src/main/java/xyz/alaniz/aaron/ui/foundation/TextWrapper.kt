package xyz.alaniz.aaron.ui.foundation

object TextWrapper {
  // CSI: Control Sequence Introducer (ESC [ ...)
  private const val CSI_REGEX = "\\[[0-?]*[ -/]*[@-~]"
  // Variable length Fe: OSC (ESC ]), DCS (ESC P), PM (ESC ^), APC (ESC _)
  // Terminated by BEL or ST (ESC \)
  private const val FE_VAR_REGEX = "[P\\]^_][\\s\\S]*?(?:\\u0007|\\u001B\\\\)"
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
    // Optimize: Process line by line to avoid allocating a full sanitized string copy.
    // This relies on the assumption that valid sanitization targets (ANSI codes, control chars)
    // do not span across lines in the input text for this application's use case.
    text.lineSequence().forEach { rawLine ->
      // Sanitize control characters and expand tabs to spaces (prevent TUI spoofing)
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
