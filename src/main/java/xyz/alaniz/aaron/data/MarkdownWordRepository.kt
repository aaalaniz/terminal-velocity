package xyz.alaniz.aaron.data

import java.security.SecureRandom
import kotlin.random.asKotlinRandom
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import xyz.alaniz.aaron.ui.foundation.TextWrapper

class MarkdownWordRepository(
    private val settingsRepository: SettingsRepository,
    private val resourceReader: ResourceReader
) : WordRepository {

  private val secureRandom = SecureRandom().asKotlinRandom()
  private val mutex = Mutex()
  private var index: List<PassageMetadata> = emptyList()

  private val indexFile = "passages.index"
  private val passagesDir = "passages/"

  override suspend fun getPassage(): List<String> {
    ensureIndexLoaded()

    val settings = settingsRepository.settings.value
    val candidates = filterPassages(settings)

    if (candidates.isEmpty()) {
      // Fallback: try to find any prose if specific request failed
      val proseFallback = index.filter { it.tags.contains("prose") }
      if (proseFallback.isNotEmpty()) {
        val fallback = proseFallback.random(secureRandom)
        return loadPassageContent(fallback.filename)
      }
      // Ultimate fallback
      return listOf("Error: No passages found.")
    }

    val selected = candidates.random(secureRandom)
    return loadPassageContent(selected.filename)
  }

  private suspend fun ensureIndexLoaded() {
    if (index.isNotEmpty()) return

    mutex.withLock {
      if (index.isNotEmpty()) return@withLock

      val indexStream = resourceReader.open(indexFile) ?: return@withLock
      val filenames = indexStream.bufferedReader().useLines { it.toList() }

      val loadedIndex = mutableListOf<PassageMetadata>()
      for (filename in filenames) {
        if (filename.isBlank()) continue
        val fullPath = passagesDir + filename.trim()
        val fileStream = resourceReader.open(fullPath)
        if (fileStream != null) {
          val content = fileStream.bufferedReader().use { it.readText() }
          val result = MarkdownParser.parse(content)
          loadedIndex.add(PassageMetadata(filename = fullPath, tags = result.tags))
        }
      }
      index = loadedIndex
    }
  }

  private fun filterPassages(settings: Settings): List<PassageMetadata> {
    val snippetSettings = settings.codeSnippetSettings

    // Logic:
    // Disabled -> only prose
    // Enabled(onlyCode=false) -> prose + code(selected languages)
    // Enabled(onlyCode=true) -> code(selected languages)

    return when (snippetSettings) {
      is CodeSnippetSettings.Disabled -> {
        index.filter { it.tags.contains("prose") }
      }
      is CodeSnippetSettings.Enabled -> {
        val selectedLanguages =
            snippetSettings.selectedLanguages.map { it.name.lowercase() }.toSet()

        index.filter { metadata ->
          val isProse = metadata.tags.contains("prose")
          val isCode = metadata.tags.contains("code")

          // Check if code matches selected language
          // We assume language tag is present if code is present.
          // E.g. tags: [code, kotlin]
          val matchesLanguage =
              if (isCode) {
                metadata.tags.any { it in selectedLanguages }
              } else {
                false
              }

          if (snippetSettings.onlyCodeSnippets) {
            isCode && matchesLanguage
          } else {
            isProse || (isCode && matchesLanguage)
          }
        }
      }
    }
  }

  private fun loadPassageContent(filename: String): List<String> {
    val stream = resourceReader.open(filename) ?: return listOf("Error: Could not load $filename")
    val content = stream.bufferedReader().use { it.readText() }
    val result = MarkdownParser.parse(content)
    return TextWrapper.wrap(result.content)
  }
}
