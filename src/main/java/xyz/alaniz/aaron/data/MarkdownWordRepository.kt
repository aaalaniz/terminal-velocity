package xyz.alaniz.aaron.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlin.random.Random
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import xyz.alaniz.aaron.di.IoDispatcher
import xyz.alaniz.aaron.ui.foundation.TextWrapper

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class MarkdownWordRepository(
    private val settingsRepository: SettingsRepository,
    private val resourceReader: ResourceReader,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WordRepository {

  private val mutex = Mutex()
  private var index: List<PassageMetadata> = emptyList()
  @Volatile private var cachedSettings: Settings? = null
  @Volatile private var cachedCandidates: List<PassageMetadata>? = null

  private val indexFile = "passages.index"
  private val passagesDir = "passages/"
  private val MAX_PASSAGE_SIZE = 100_000

  override suspend fun getPassage(): List<String> {
    ensureIndexLoaded()

    val settings = settingsRepository.settings.value
    val candidates = filterPassages(settings)

    if (candidates.isEmpty()) {
      // Fallback: try to find any prose if specific request failed
      val proseFallback = index.filter { it.tags.contains("prose") }
      if (proseFallback.isNotEmpty()) {
        val fallback = proseFallback.random(Random)
        return loadPassageContent(fallback.filename)
      }
      // Ultimate fallback
      return listOf("Error: No passages found.")
    }

    val selected = candidates.random(Random)
    return loadPassageContent(selected.filename)
  }

  private suspend fun ensureIndexLoaded() {
    if (index.isNotEmpty()) return

    mutex.withLock {
      if (index.isNotEmpty()) return@withLock

      try {
        withContext(ioDispatcher) {
          val indexStream = resourceReader.open(indexFile)
          val loadedIndex = mutableListOf<PassageMetadata>()

          indexStream.bufferedReader().useLines { lines ->
            for (line in lines) {
              if (line.isBlank()) continue
              val parts = line.split(",").map { it.trim() }
              if (parts.isNotEmpty()) {
                val filename = parts[0]
                // Security: Prevent path traversal by ignoring malformed filenames
                if (filename.contains("..") ||
                    filename.startsWith("/") ||
                    filename.startsWith("\\")) {
                  continue
                }

                val tags = parts.drop(1).toSet()
                val fullPath = passagesDir + filename
                loadedIndex.add(PassageMetadata(filename = fullPath, tags = tags))
              }
            }
          }
          index = loadedIndex
        }
      } catch (e: Exception) {
        // If index fails to load, we can't do much.
        // In a TUI app, we avoid printing stack traces to stderr to prevent UI corruption
        // and information leakage.
      }
    }
  }

  private fun filterPassages(settings: Settings): List<PassageMetadata> {
    val currentCachedSettings = cachedSettings
    if (currentCachedSettings == settings) {
      cachedCandidates?.let {
        return it
      }
    }

    val snippetSettings = settings.codeSnippetSettings

    // Logic:
    // Disabled -> only prose
    // Enabled(onlyCode=false) -> prose + code(selected languages)
    // Enabled(onlyCode=true) -> code(selected languages)

    val result =
        when (snippetSettings) {
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
    cachedCandidates = result
    cachedSettings = settings
    return result
  }

  private suspend fun loadPassageContent(filename: String): List<String> {
    return try {
      withContext(ioDispatcher) {
        val stream = resourceReader.open(filename)
        val content =
            stream.bufferedReader().use { reader ->
              val builder = StringBuilder()
              val buffer = CharArray(8192)
              var totalRead = 0
              var n: Int
              while (reader.read(buffer).also { n = it } != -1) {
                totalRead += n
                // Security: Prevent Denial of Service by limiting passage size
                if (totalRead > MAX_PASSAGE_SIZE) {
                  return@withContext listOf("Error: Passage too large: $filename")
                }
                builder.append(buffer, 0, n)
              }
              builder.toString()
            }
        // No parsing needed, content is raw text
        TextWrapper.wrap(content)
      }
    } catch (e: Exception) {
      listOf("Error: Could not load $filename")
    }
  }
}
