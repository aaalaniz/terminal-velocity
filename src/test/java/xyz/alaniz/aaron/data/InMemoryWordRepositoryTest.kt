package xyz.alaniz.aaron.data

import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class InMemoryWordRepositoryTest {

    private val expectedWords = setOf(
        "kotlin",
        "terminal",
        "velocity",
        "circuit",
        "mosaic",
        "metro",
        "function",
        "variable",
        "compile",
        "execute"
    )

    private val expectedPassage = listOf(
        "Kotlin is a modern programming language that makes developers happier.",
        "It is concise, safe, and interoperable with Java and other languages.",
        "Mosaic is a Kotlin library for building terminal user interfaces using Jetpack Compose.",
        "Circuit is a lightweight framework for building navigation and unidirectional data flow.",
        "Metro is a compile-time dependency injection library designed for Kotlin JVM projects.",
        "By combining these technologies, we can create powerful CLI applications with ease.",
        "This typing game is a demonstration of how these libraries work together in a real-world scenario."
    )

    @Test
    fun `nextWord returns a valid word from the repository`() {
        val repository = InMemoryWordRepository()
        val word = repository.nextWord()
        assertNotNull(word)
        assertContains(expectedWords, word, "The returned word should be in the known list of words")
    }

    @Test
    fun `getPassage returns the expected passage`() {
        val repository = InMemoryWordRepository()
        val passage = repository.getPassage()
        assertEquals(expectedPassage, passage, "The returned passage should match the expected hardcoded passage")
    }
}
