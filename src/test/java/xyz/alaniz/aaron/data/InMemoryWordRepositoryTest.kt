package xyz.alaniz.aaron.data

import kotlin.test.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.jupiter.api.Test

class InMemoryWordRepositoryTest {

  private class FakeSettingsRepository(initialSettings: Settings = Settings()) :
      SettingsRepository {
    private val _settings = MutableStateFlow(initialSettings)
    override val settings: StateFlow<Settings> = _settings.asStateFlow()

    override fun updateSettings(transform: (Settings) -> Settings) {
      _settings.value = transform(_settings.value)
    }
  }

  @Test
  fun `getPassage returns a valid passage`() {
    val repository = InMemoryWordRepository(FakeSettingsRepository())
    val passage = repository.getPassage()

    assertTrue(passage.isNotEmpty(), "Passage should not be empty")
    // Check wrapping roughly. Note: some force breaks might be exactly 80.
    assertTrue(passage.all { it.length <= 80 }, "Lines should be wrapped to 80 chars")
  }

  @Test
  fun `getPassage works when code snippets are enabled`() {
    val settings =
        Settings(
            codeSnippetSettings =
                CodeSnippetSettings(enabled = true, selectedLanguages = setOf(Language.KOTLIN)))
    val repository = InMemoryWordRepository(FakeSettingsRepository(settings))
    val passage = repository.getPassage()
    assertTrue(passage.isNotEmpty())
    assertTrue(passage.all { it.length <= 80 }, "Lines should be wrapped to 80 chars")
  }
}
