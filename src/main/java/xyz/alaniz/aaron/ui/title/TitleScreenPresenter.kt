package xyz.alaniz.aaron.ui.title

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.popRoot
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
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

    data class TitleOptionSelected(val titleScreenOption: TitleScreenOption) : TitleScreenEvent
}

data class TitleScreenState(
    val selectedTitleScreenOptionIndex: Int,
    val selectableOptions: List<TitleScreenOption>,
    val onEvent: (TitleScreenEvent) -> Unit,
) : CircuitUiState

private val TITLE_SCREEN_OPTIONS = listOf(
    TitleScreenOption.StartGame,
    TitleScreenOption.Settings,
    TitleScreenOption.Quit
)

@AssistedInject
class TitleScreenPresenter (
    @Assisted private val navigator: Navigator
) : Presenter<TitleScreenState> {

    @AssistedFactory
    @CircuitInject(TitleScreen::class, AppScope::class)
    fun interface Factory {
        fun create(
            navigator: Navigator
        ): TitleScreenPresenter
    }

    @Composable
    override fun present(): TitleScreenState {
        var selectedTitleScreenOptionIndex by remember {
            mutableIntStateOf(0)
        }

        return TitleScreenState(selectedTitleScreenOptionIndex = selectedTitleScreenOptionIndex,
            selectableOptions = TITLE_SCREEN_OPTIONS) { titleScreenEvent ->
            when (titleScreenEvent) {
                TitleScreenEvent.NextTitleOption -> {
                    selectedTitleScreenOptionIndex = selectedTitleScreenOptionIndex
                        .nextIndex(TITLE_SCREEN_OPTIONS)
                }
                TitleScreenEvent.PreviousTitleOption -> {
                    selectedTitleScreenOptionIndex = selectedTitleScreenOptionIndex.previousIndex()
                }
                is TitleScreenEvent.TitleOptionSelected -> when (titleScreenEvent.titleScreenOption) {
                    is TitleScreenOption.StartGame -> navigator.goTo(GameScreen)
                    is TitleScreenOption.Settings -> navigator.goTo(SettingsScreen)
                    is TitleScreenOption.Quit -> navigator.popRoot()
                }
            }
        }
    }

    private fun Int.nextIndex(list: List<*>): Int = (this + 1).coerceAtMost(list.size - 1)

    private fun Int.previousIndex(): Int = (this - 1).coerceAtLeast(0)
}