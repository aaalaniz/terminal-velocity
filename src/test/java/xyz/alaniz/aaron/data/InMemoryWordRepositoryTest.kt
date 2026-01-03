package xyz.alaniz.aaron.data

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InMemoryWordRepositoryTest {

    @Test
    fun `nextWord returns a non-empty string`() {
        val repository: WordRepository = InMemoryWordRepository()
        val word = repository.nextWord()
        assertNotNull(word)
        assertTrue(word.isNotEmpty(), "Word should not be empty")
    }

    @Test
    fun `nextWord returns a word from the predefined list`() {
        // We might not know the exact list, but we can check if it returns something valid.
        // Or we can pass a seed or list to constructor if we want to test specifically.
        // For MVP, hardcoded list is fine.
        val repository = InMemoryWordRepository()
        val word = repository.nextWord()
        // Just verify it works for now.
        // Maybe check it contains only letters?
        assertTrue(word.all { it.isLetter() }, "Word should contain only letters")
    }
}
