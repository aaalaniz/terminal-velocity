package xyz.alaniz.aaron.ui.title

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

sealed interface TitleScreenOption {
    val isFocused: Boolean
    val label: String

    data class StartGame(override val isFocused: Boolean) : TitleScreenOption {
        override val label = "Start game"
    }
    data class Settings(override val isFocused: Boolean) : TitleScreenOption {
        override val label = "Settings"
    }
    data class Quit(override val isFocused: Boolean) : TitleScreenOption {
        override val label = "Quit"
    }
}

sealed interface TitleScreenState : CircuitUiState {
    val selectableOptions: List<TitleScreenOption>

    data class SelectingOption(override val selectableOptions: List<TitleScreenOption>) : TitleScreenState
}

private val INITIAL_TITLE_SCREEN_STATE = TitleScreenState.SelectingOption(selectableOptions = listOf(
    TitleScreenOption.StartGame(true),
    TitleScreenOption.Settings(false),
    TitleScreenOption.Quit(false))
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
        val titleScreenState = remember {
            mutableStateOf(INITIAL_TITLE_SCREEN_STATE)
        }
        return titleScreenState.value
    }
}