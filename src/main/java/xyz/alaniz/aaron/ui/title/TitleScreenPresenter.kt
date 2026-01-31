package xyz.alaniz.aaron.ui.title

import androidx.compose.runtime.Composable
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
import xyz.alaniz.aaron.data.AppVersion
import xyz.alaniz.aaron.ui.game.GameScreen
import xyz.alaniz.aaron.ui.settings.SettingsScreen

sealed interface TitleScreenOption {
  val label: String

  data object StartGame : TitleScreenOption {
    override val label = "Start game"
  }

  data object Settings : TitleScreenOption {
    override val label = "Settings"
  }

  data object Quit : TitleScreenOption {
    override val label = "Quit"
  }
}

sealed interface TitleScreenEvent {
  data object PreviousTitleOption : TitleScreenEvent

  data object NextTitleOption : TitleScreenEvent

  data object Quit : TitleScreenEvent

  data class TitleOptionSelected(val titleScreenOption: TitleScreenOption) : TitleScreenEvent
}

data class TitleScreenState(
    val selectedTitleScreenOptionIndex: Int,
    val selectableOptions: List<TitleScreenOption>,
    val appVersion: String,
    val onEvent: (TitleScreenEvent) -> Unit,
) : CircuitUiState

private val TITLE_SCREEN_OPTIONS =
    listOf(TitleScreenOption.StartGame, TitleScreenOption.Settings, TitleScreenOption.Quit)

@AssistedInject
class TitleScreenPresenter(
    @Assisted private val navigator: Navigator,
    private val appVersion: AppVersion,
) : Presenter<TitleScreenState> {

  @AssistedFactory
  @CircuitInject(TitleScreen::class, AppScope::class)
  fun interface Factory {
    fun create(navigator: Navigator): TitleScreenPresenter
  }

  @Composable
  override fun present(): TitleScreenState {
    var selectedTitleScreenOptionIndex by remember { mutableIntStateOf(0) }

    return TitleScreenState(
        selectedTitleScreenOptionIndex = selectedTitleScreenOptionIndex,
        selectableOptions = TITLE_SCREEN_OPTIONS,
        appVersion = appVersion.version,
    ) { titleScreenEvent ->
      when (titleScreenEvent) {
        TitleScreenEvent.NextTitleOption -> {
          selectedTitleScreenOptionIndex =
              selectedTitleScreenOptionIndex.nextIndex(TITLE_SCREEN_OPTIONS)
        }
        TitleScreenEvent.PreviousTitleOption -> {
          selectedTitleScreenOptionIndex =
              selectedTitleScreenOptionIndex.previousIndex(TITLE_SCREEN_OPTIONS)
        }
        TitleScreenEvent.Quit -> navigator.pop()
        is TitleScreenEvent.TitleOptionSelected ->
            when (titleScreenEvent.titleScreenOption) {
              is TitleScreenOption.StartGame -> navigator.goTo(GameScreen)
              is TitleScreenOption.Settings -> navigator.goTo(SettingsScreen)
              is TitleScreenOption.Quit -> navigator.pop()
            }
      }
    }
  }

  private fun Int.nextIndex(list: List<*>): Int {
    val next = this + 1
    return if (next >= list.size) 0 else next
  }

  private fun Int.previousIndex(list: List<*>): Int {
    val prev = this - 1
    return if (prev < 0) list.lastIndex else prev
  }
}
