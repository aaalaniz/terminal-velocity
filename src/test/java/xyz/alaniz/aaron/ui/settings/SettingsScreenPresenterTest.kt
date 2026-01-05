package xyz.alaniz.aaron.ui.settings

import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SettingsScreenPresenterTest {
    @Test
    fun `OnBack navigates back`() = runTest {
        val navigator = FakeNavigator(SettingsScreen)
        val presenter = SettingsScreenPresenter(navigator)

        presenter.test {
            val state = awaitItem()
            state.onEvent(SettingsScreenEvent.OnBack)
            navigator.awaitPop()
        }
    }
}
