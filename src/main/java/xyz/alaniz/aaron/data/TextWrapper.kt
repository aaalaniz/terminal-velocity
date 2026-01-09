package xyz.alaniz.aaron.data

object TextWrapper {
    fun wrapText(text: String, maxLength: Int = 80): List<String> {
        val result = mutableListOf<String>()
        val paragraphs = text.split("\n")

        for (paragraph in paragraphs) {
            if (paragraph.length <= maxLength) {
                result.add(paragraph)
            } else {
                var remaining = paragraph
                while (remaining.length > maxLength) {
                    var breakIndex = remaining.lastIndexOf(' ', maxLength)
                    if (breakIndex == -1) {
                        // No space found within limit.
                        // We must either break mid-word or overflow.
                        // "without wrapping mid word" -> overflow?
                        // But wait, if we have a super long word, we might have to break it or find the *next* space?
                        // Finding the next space means the line exceeds maxLength.
                        // Finding the previous space (what we did) means the line is <= maxLength.

                        // If no space in first maxLength chars, let's look for the first space *after* maxLength
                        breakIndex = remaining.indexOf(' ', maxLength)
                        if (breakIndex == -1) {
                            // No spaces at all remaining. Keep the whole thing.
                            breakIndex = remaining.length
                        }
                    }

                    val line = remaining.substring(0, breakIndex)
                    result.add(line)
                    remaining = remaining.substring(breakIndex).trimStart() // remove leading space for next line
                }
                if (remaining.isNotEmpty()) {
                    result.add(remaining)
                }
            }
        }
        return result
    }
}
