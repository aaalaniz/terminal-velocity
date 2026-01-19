package xyz.alaniz.aaron.data

import java.io.ByteArrayInputStream
import java.io.InputStream
import kotlin.test.assertEquals
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

    override fun open(path: String): InputStream? {
      return files[path]?.let { ByteArrayInputStream(it.toByteArray()) }
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

    assertEquals(listOf("This is a test passage."), passage)
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
      assertEquals(listOf("fun main() {}"), passage)
    }
  }
}
