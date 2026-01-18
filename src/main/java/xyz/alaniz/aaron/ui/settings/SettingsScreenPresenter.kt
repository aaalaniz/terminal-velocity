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

    // Construct the list of UI items based on current settings
    val items = buildList {
      // Group: Code Snippets
      add(
          SettingsUiItem(
              id = "code_snippets_enabled",
              label = "Enable Code Snippets",
              isChecked = settings.codeSnippetSettings.enabled,
              isEnabled = true,
              indentLevel = 0))

      if (settings.codeSnippetSettings.enabled) {
        add(
            SettingsUiItem(
                id = "code_snippets_only",
                label = "Only Code Snippets",
                isChecked = settings.codeSnippetSettings.onlyCodeSnippets,
                isEnabled = true,
                indentLevel = 1))

        Language.entries.forEach { language ->
          add(
              SettingsUiItem(
                  id = "language_${language.name}",
                  label = language.displayName,
                  isChecked = settings.codeSnippetSettings.selectedLanguages.contains(language),
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
          focusedIndex = (focusedIndex - 1).coerceAtLeast(0)
        }
        SettingsScreenEvent.MoveFocusDown -> {
          focusedIndex = (focusedIndex + 1).coerceAtMost(items.lastIndex)
        }
        SettingsScreenEvent.ToggleCurrentItem -> {
          val currentItem = items.getOrNull(focusedIndex) ?: return@SettingsScreenState

          if (currentItem.id == "code_snippets_enabled") {
            settingsRepository.updateSettings { old ->
              old.copy(
                  codeSnippetSettings =
                      old.codeSnippetSettings.copy(enabled = !old.codeSnippetSettings.enabled))
            }
          } else if (currentItem.id == "code_snippets_only") {
            settingsRepository.updateSettings { old ->
              old.copy(
                  codeSnippetSettings =
                      old.codeSnippetSettings.copy(
                          onlyCodeSnippets = !old.codeSnippetSettings.onlyCodeSnippets))
            }
          } else if (currentItem.id.startsWith("language_")) {
            val languageName = currentItem.id.removePrefix("language_")
            val language = Language.entries.find { it.name == languageName }
            if (language != null) {
              settingsRepository.updateSettings { old ->
                val currentLanguages = old.codeSnippetSettings.selectedLanguages.toMutableSet()
                if (currentLanguages.contains(language)) {
                  currentLanguages.remove(language)
                } else {
                  currentLanguages.add(language)
                }
                old.copy(
                    codeSnippetSettings =
                        old.codeSnippetSettings.copy(selectedLanguages = currentLanguages))
              }
            }
          }
        }
      }
    }
  }
}
