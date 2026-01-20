package xyz.alaniz.aaron.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.security.SecureRandom
import kotlin.random.asKotlinRandom
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import xyz.alaniz.aaron.di.IoDispatcher
import xyz.alaniz.aaron.ui.foundation.TextWrapper

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class MarkdownWordRepository
@Inject
constructor(
    private val settingsRepository: SettingsRepository,
    private val resourceReader: ResourceReader,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
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

      try {
        withContext(ioDispatcher) {
          val indexStream = resourceReader.open(indexFile)
          val lines = indexStream.bufferedReader().useLines { it.toList() }

          val loadedIndex = mutableListOf<PassageMetadata>()
          for (line in lines) {
            if (line.isBlank()) continue
            val parts = line.split(",").map { it.trim() }
            if (parts.isNotEmpty()) {
              val filename = parts[0]
              val tags = parts.drop(1).toSet()
              val fullPath = passagesDir + filename
              loadedIndex.add(PassageMetadata(filename = fullPath, tags = tags))
            }
          }
          index = loadedIndex
        }
      } catch (e: Exception) {
        // If index fails to load, we can't do much.
        // In a real app we might want to log this.
        e.printStackTrace()
      }
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

  private suspend fun loadPassageContent(filename: String): List<String> {
    return try {
      withContext(ioDispatcher) {
        val stream = resourceReader.open(filename)
        val content = stream.bufferedReader().use { it.readText() }
        // No parsing needed, content is raw text
        TextWrapper.wrap(content)
      }
    } catch (e: Exception) {
      listOf("Error: Could not load $filename")
    }
  }
}
