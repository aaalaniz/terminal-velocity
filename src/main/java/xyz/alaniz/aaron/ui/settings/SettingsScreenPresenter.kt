package xyz.alaniz.aaron.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import xyz.alaniz.aaron.data.CodeSnippetSettings
import xyz.alaniz.aaron.data.Language
import xyz.alaniz.aaron.data.SettingsRepository

sealed interface SettingsScreenEvent {
  data object OnBack : SettingsScreenEvent

  data object MoveFocusUp : SettingsScreenEvent

  data object MoveFocusDown : SettingsScreenEvent

  data object ToggleCurrentItem : SettingsScreenEvent
}

data class SettingsUiItem(
    val id: String,
    val label: String,
    val isChecked: Boolean,
    val isEnabled: Boolean,
    val indentLevel: Int = 0
)

data class SettingsScreenState(
    val items: List<SettingsUiItem>,
    val focusedIndex: Int,
    val onEvent: (SettingsScreenEvent) -> Unit
) : CircuitUiState

@AssistedInject
class SettingsScreenPresenter(
    @Assisted private val navigator: Navigator,
    private val settingsRepository: SettingsRepository
) : Presenter<SettingsScreenState> {

  @AssistedFactory
  @CircuitInject(SettingsScreen::class, AppScope::class)
  fun interface Factory {
    fun create(navigator: Navigator): SettingsScreenPresenter
  }

  @Composable
  override fun present(): SettingsScreenState {
    val settings by settingsRepository.settings.collectAsState()
    var focusedIndex by remember { mutableIntStateOf(0) }

    val snippetSettings = settings.codeSnippetSettings
    val isSnippetsEnabled = snippetSettings is CodeSnippetSettings.Enabled

    // Construct the list of UI items based on current settings
    val items = buildList {
      // Group: Code Snippets
      add(
          SettingsUiItem(
              id = "code_snippets_enabled",
              label = "Enable Code Snippets",
              isChecked = isSnippetsEnabled,
              isEnabled = true,
              indentLevel = 0))

      if (snippetSettings is CodeSnippetSettings.Enabled) {
        add(
            SettingsUiItem(
                id = "code_snippets_only",
                label = "Only Code Snippets",
                isChecked = snippetSettings.onlyCodeSnippets,
                isEnabled = true,
                indentLevel = 1))

        Language.entries.forEach { language ->
          add(
              SettingsUiItem(
                  id = "language_${language.name}",
                  label = language.displayName,
                  isChecked = snippetSettings.selectedLanguages.contains(language),
                  isEnabled = true,
                  indentLevel = 1))
        }
      }
    }

    // Ensure focusedIndex is within bounds if items list shrinks (e.g. toggling off)
    if (focusedIndex >= items.size) {
      focusedIndex = (items.size - 1).coerceAtLeast(0)
    }

    return SettingsScreenState(items = items, focusedIndex = focusedIndex) { event ->
      when (event) {
        SettingsScreenEvent.OnBack -> navigator.pop()
        SettingsScreenEvent.MoveFocusUp -> {
          val prev = focusedIndex - 1
          focusedIndex = if (prev < 0) items.lastIndex else prev
        }
        SettingsScreenEvent.MoveFocusDown -> {
          val next = focusedIndex + 1
          focusedIndex = if (next > items.lastIndex) 0 else next
        }
        SettingsScreenEvent.ToggleCurrentItem -> {
          val currentItem = items.getOrNull(focusedIndex) ?: return@SettingsScreenState

          if (currentItem.id == "code_snippets_enabled") {
            settingsRepository.updateSettings { old ->
              val newSettings =
                  if (old.codeSnippetSettings is CodeSnippetSettings.Enabled) {
                    CodeSnippetSettings.Disabled
                  } else {
                    CodeSnippetSettings.Enabled()
                  }
              old.copy(codeSnippetSettings = newSettings)
            }
          } else if (currentItem.id == "code_snippets_only") {
            settingsRepository.updateSettings { old ->
              val currentSettings = old.codeSnippetSettings
              if (currentSettings is CodeSnippetSettings.Enabled) {
                old.copy(
                    codeSnippetSettings =
                        currentSettings.copy(onlyCodeSnippets = !currentSettings.onlyCodeSnippets))
              } else {
                old
              }
            }
          } else if (currentItem.id.startsWith("language_")) {
            val languageName = currentItem.id.removePrefix("language_")
            val language = Language.entries.find { it.name == languageName }
            if (language != null) {
              settingsRepository.updateSettings { old ->
                val currentSettings = old.codeSnippetSettings
                if (currentSettings is CodeSnippetSettings.Enabled) {
                  val currentLanguages = currentSettings.selectedLanguages.toMutableSet()
                  if (currentLanguages.contains(language)) {
                    currentLanguages.remove(language)
                  } else {
                    currentLanguages.add(language)
                  }
                  old.copy(
                      codeSnippetSettings =
                          currentSettings.copy(selectedLanguages = currentLanguages))
                } else {
                  old
                }
              }
            }
          }
        }
      }
    }
  }
}
