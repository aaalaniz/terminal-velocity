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

  // Lookbehind for .!? followed by whitespace or end of string.
  private val SENTENCE_SPLIT_REGEX = Regex("(?<=[.!?])\\s+")

  fun sanitize(text: String): String {
    // Sanitize control characters and expand tabs to spaces (prevent TUI spoofing)
    return text.replace(SANITIZATION_REGEX) { result -> if (result.value == "\t") "  " else "" }
  }

  fun sentenceSplit(text: String): List<String> {
    val sanitized = sanitize(text)
    // Split by sentence terminators followed by space.
    // Filter empty strings in case of trailing spaces.
    return sanitized.split(SENTENCE_SPLIT_REGEX).filter { it.isNotEmpty() }
  }

  /**
   * wraps text for display purposes, preserving all characters. Splits into lines of length <=
   * width. Breaks on whitespace if possible, otherwise forces break. Does NOT trim lines or remove
   * characters (except potentially splitting space to next line? No, must preserve).
   */
  fun displayWrap(text: String, width: Int): List<String> {
    require(width > 0) { "Width must be > 0" }
    val lines = mutableListOf<String>()
    val len = text.length
    var start = 0

    while (start < len) {
      val remainingLength = len - start
      if (remainingLength <= width) {
        // If remaining fits, check for internal newlines
        val newlineIndex = text.indexOf('\n', start)
        if (newlineIndex != -1) {
          val splitIndex = newlineIndex + 1
          lines.add(text.substring(start, splitIndex))
          start = splitIndex
        } else {
          lines.add(text.substring(start))
          start = len
        }
      } else {
        // Try to break at a space within limits
        var splitIndex = -1
        val limit = start + width

        val newlineIndex = text.indexOf('\n', start)
        if (newlineIndex != -1 && newlineIndex < limit) {
          splitIndex = newlineIndex + 1
        } else {
          val lastSpace = text.lastIndexOf(' ', limit - 1)
          if (lastSpace >= start) {
            splitIndex = lastSpace + 1
          }
        }

        if (splitIndex == -1 || splitIndex <= start) {
          // No good break point found, forced break
          splitIndex = start + width
        }

        lines.add(text.substring(start, splitIndex))
        start = splitIndex
      }
    }
    return lines
  }

  fun wrap(text: String, width: Int = 80): List<String> {
    require(width > 0) { "Width must be > 0" }
    val lines = mutableListOf<String>()
    val sanitizedText = sanitize(text)

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
