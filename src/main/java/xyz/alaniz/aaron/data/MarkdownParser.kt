package xyz.alaniz.aaron.data

object MarkdownParser {
  data class Result(val tags: Set<String>, val content: String)

  fun parse(text: String): Result {
    val lines = text.lines()
    if (lines.isEmpty() || lines[0].trim() != "---") {
      return Result(emptySet(), text)
    }

    // Find the closing "---"
    // We start searching from index 1
    var endDelimiterIndex = -1
    for (i in 1 until lines.size) {
      if (lines[i].trim() == "---") {
        endDelimiterIndex = i
        break
      }
    }

    if (endDelimiterIndex == -1) {
      // No closing delimiter found, treat all as content
      return Result(emptySet(), text)
    }

    val frontMatter = lines.subList(1, endDelimiterIndex)
    // Content starts after the delimiter
    val contentLines = lines.drop(endDelimiterIndex + 1)
    val content = contentLines.joinToString("\n").trim()

    val tags = parseTags(frontMatter)

    return Result(tags, content)
  }

  private fun parseTags(lines: List<String>): Set<String> {
    val tags = mutableSetOf<String>()
    for (line in lines) {
      val trimmed = line.trim()
      if (trimmed.startsWith("tags:")) {
        val valuePart = trimmed.removePrefix("tags:").trim()
        if (valuePart.startsWith("[") && valuePart.endsWith("]")) {
          val inner = valuePart.drop(1).dropLast(1)
          tags.addAll(inner.split(",").map { it.trim() }.filter { it.isNotEmpty() })
        } else {
          tags.addAll(valuePart.split(",").map { it.trim() }.filter { it.isNotEmpty() })
        }
      }
    }
    return tags
  }
}
