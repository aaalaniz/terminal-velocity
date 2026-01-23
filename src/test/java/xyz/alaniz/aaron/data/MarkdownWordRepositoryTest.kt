package xyz.alaniz.aaron.data

import com.google.common.truth.Truth.assertThat
import java.io.ByteArrayInputStream
import java.io.InputStream
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class MarkdownWordRepositoryTest {

  private class FakeSettingsRepository(initialSettings: Settings = Settings()) :
      SettingsRepository {
    private val _settings = MutableStateFlow(initialSettings)
    override val settings: StateFlow<Settings> = _settings.asStateFlow()

    override fun updateSettings(transform: (Settings) -> Settings) {
      _settings.value = transform(_settings.value)
    }
  }

  private class FakeResourceReader : ResourceReader {
    val files = mutableMapOf<String, String>()

    override fun open(path: String): InputStream {
      return files[path]?.let { ByteArrayInputStream(it.toByteArray()) }
          ?: throw IllegalArgumentException("Resource not found: $path")
    }
  }

  @Test
  fun `getPassage returns a valid passage from prose`() = runTest {
    val reader = FakeResourceReader()
    val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    // CSV format index
    reader.files["passages.index"] = "prose_test.md,prose"
    // Raw content
    reader.files["passages/prose_test.md"] = "This is a test passage."

    val repository = MarkdownWordRepository(FakeSettingsRepository(), reader, testDispatcher)
    val passage = repository.getPassage()

    assertThat(passage).isEqualTo(listOf("This is a test passage."))
  }

  @Test
  fun `getPassage filters for code`() = runTest {
    val reader = FakeResourceReader()
    val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    // CSV format index
    reader.files["passages.index"] =
        """
        prose_test.md,prose
        code_kotlin.md,code,kotlin
        """
            .trimIndent()

    reader.files["passages/prose_test.md"] = "Prose content."
    reader.files["passages/code_kotlin.md"] = "fun main() {}"

    val settings =
        Settings(
            codeSnippetSettings =
                CodeSnippetSettings.Enabled(
                    onlyCodeSnippets = true, selectedLanguages = setOf(Language.KOTLIN)))

    val repository =
        MarkdownWordRepository(FakeSettingsRepository(settings), reader, testDispatcher)

    // Should always return kotlin code
    repeat(5) {
      val passage = repository.getPassage()
      assertThat(passage).isEqualTo(listOf("fun main() {}"))
    }
  }

  @Test
  fun `ensureIndexLoaded ignores filenames with path traversal`() = runTest {
    val reader = FakeResourceReader()
    val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    // CSV format index with path traversal attempt
    reader.files["passages.index"] =
        """
        ../../secrets.txt,prose
        valid_passage.md,prose
        """
            .trimIndent()

    reader.files["passages/valid_passage.md"] = "Safe content."
    // We expect the repo to NOT ask for "passages/../../secrets.txt"
    reader.files["passages/../../secrets.txt"] = "SECRET"

    val repository = MarkdownWordRepository(FakeSettingsRepository(), reader, testDispatcher)

    // Verify that the repository didn't try to load the traversal path
    // If the fix works, we should NEVER see "SECRET".
    repeat(20) {
      val result = repository.getPassage()
      assertThat(result).containsExactly("Safe content.")
    }
  }

  @Test
  fun `getPassage handles excessively large files securely`() = runTest {
    val reader = FakeResourceReader()
    val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    reader.files["passages.index"] = "large_passage.md,prose"

    // Create a string larger than limit (100,000 chars)
    val largeContent = "a".repeat(100_001)
    reader.files["passages/large_passage.md"] = largeContent

    val repository = MarkdownWordRepository(FakeSettingsRepository(), reader, testDispatcher)

    val result = repository.getPassage()
    assertThat(result).containsExactly("Error: Passage too large: passages/large_passage.md")
  }
}
