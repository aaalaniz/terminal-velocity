package xyz.alaniz.aaron.ui.title

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

data object TitleScreenState : CircuitUiState

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
        return TitleScreenState
    }
}