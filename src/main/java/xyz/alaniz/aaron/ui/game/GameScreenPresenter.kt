package xyz.alaniz.aaron.ui.game

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

data object GameScreenState : CircuitUiState

@AssistedInject
class GameScreenPresenter (
    @Assisted private val navigator: Navigator
) : Presenter<GameScreenState> {

    @AssistedFactory
    @CircuitInject(GameScreen::class, AppScope::class)
    fun interface Factory {
        fun create(
            navigator: Navigator
        ): GameScreenPresenter
    }

    @Composable
    override fun present(): GameScreenState {
        return GameScreenState
    }
}