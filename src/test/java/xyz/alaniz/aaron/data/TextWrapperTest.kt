package xyz.alaniz.aaron.data

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TextWrapperTest {

    @Test
    fun `wrapText preserves short lines`() {
        val text = "Short line"
        val wrapped = TextWrapper.wrapText(text, maxLength = 80)
        assertEquals(listOf("Short line"), wrapped)
    }

    @Test
    fun `wrapText splits long lines at space`() {
        val text = "This is a very long line that should be split into multiple lines because it exceeds the limit."
        val wrapped = TextWrapper.wrapText(text, maxLength = 20)

        // "This is a very long " -> 20 chars exactly if including space?
        // "This is a very long" is 19 chars. " " is 20th.
        // lastIndexOf(' ', 20) on "This is a very long ..."
        // Index 19 is 'g'. Index 15 is ' '.
        // So it might split at "This is a very".

        assertTrue(wrapped.all { it.length <= 20 || !it.contains(" ") }) // If contains space, should be wrapped.

        // Check content reconstruction roughly
        assertEquals(text.replace("\n", " "), wrapped.joinToString(" "))
    }

    @Test
    fun `wrapText preserves existing newlines`() {
        val text = "Line 1\nLine 2"
        val wrapped = TextWrapper.wrapText(text, maxLength = 80)
        assertEquals(listOf("Line 1", "Line 2"), wrapped)
    }

    @Test
    fun `wrapText preserves indentation`() {
        val text = "    val x = 1"
        val wrapped = TextWrapper.wrapText(text, maxLength = 80)
        assertEquals(listOf("    val x = 1"), wrapped)
    }

    @Test
    fun `wrapText handles single word longer than limit`() {
        // "limit per line without wrapping mid word"
        // Our logic: if no space <= maxLength, find next space.
        val text = "Supercalifragilisticexpialidocious"
        val wrapped = TextWrapper.wrapText(text, maxLength = 10)
        assertEquals(listOf("Supercalifragilisticexpialidocious"), wrapped)
    }

    @Test
    fun `wrapText handles code snippet with indentation preservation`() {
        val code = """
        fun main() {
            println("Hello World")
        }
        """.trimIndent()
        // "fun main() {" -> 12 chars
        // "    println("Hello World")" -> 26 chars
        // "}" -> 1 char

        val wrapped = TextWrapper.wrapText(code, maxLength = 80)
        assertEquals(listOf("fun main() {", "    println(\"Hello World\")", "}"), wrapped)
    }
}
