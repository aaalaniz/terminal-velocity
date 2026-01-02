package xyz.alaniz.aaron.ui.settings

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

data object SettingsScreenState : CircuitUiState

@AssistedInject
class SettingsScreenPresenter (
    @Assisted private val navigator: Navigator
) : Presenter<SettingsScreenState> {

    @AssistedFactory
    @CircuitInject(SettingsScreen::class, AppScope::class)
    fun interface Factory {
        fun create(
            navigator: Navigator
        ): SettingsScreenPresenter
    }

    @Composable
    override fun present(): SettingsScreenState {
        return SettingsScreenState
    }
}