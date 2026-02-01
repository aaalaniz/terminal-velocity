package xyz.alaniz.aaron.ui.foundation

object TextWrapper {
  // CSI: Control Sequence Introducer (ESC [ ...)
  private const val CSI_REGEX = "\\[[0-?]*[ -/]*[@-~]"
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
    sanitizedText.lineSequence().forEach { line ->
      if (line.length <= width) {
        val trimmed = line.trimEnd()
        if (trimmed.isNotEmpty()) {
          lines.add(trimmed)
        }
      } else {
        var remaining = line
        while (remaining.isNotEmpty()) {
          if (remaining.length <= width) {
            val trimmed = remaining.trimEnd()
            if (trimmed.isNotEmpty()) {
              lines.add(trimmed)
            }
            remaining = ""
          } else {
            var breakPoint = remaining.lastIndexOf(' ', width)
            if (breakPoint == -1) {
              // Force break
              breakPoint = width
              val chunk = remaining.take(breakPoint)
              val trimmed = chunk.trimEnd()
              if (trimmed.isNotEmpty()) {
                lines.add(trimmed)
              }
              remaining = remaining.drop(breakPoint)
            } else {
              val chunk = remaining.take(breakPoint)
              val trimmed = chunk.trimEnd()
              if (trimmed.isNotEmpty()) {
                lines.add(trimmed)
              }
              remaining = remaining.drop(breakPoint).trimStart()
            }
          }
        }
      }
    }
    return lines
  }
}
