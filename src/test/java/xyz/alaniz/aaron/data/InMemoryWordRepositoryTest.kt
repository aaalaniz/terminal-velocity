package xyz.alaniz.aaron.data

import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InMemoryWordRepositoryTest {

    // We can't easily test for random selection of 20 items without possibly being flaky,
    // but we can verify it returns *a* passage and it is non-empty.

    @Test
    fun `getPassage returns a valid passage`() {
        val repository = InMemoryWordRepository()
        val passage = repository.getPassage()

        assertNotNull(passage)
        assertTrue(passage.isNotEmpty(), "Passage should not be empty")
        assertTrue(passage.all { it.isNotEmpty() }, "Lines in passage should not be empty")
    }
}
