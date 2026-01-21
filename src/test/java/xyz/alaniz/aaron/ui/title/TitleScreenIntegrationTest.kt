package xyz.alaniz.aaron.ui.title

import com.jakewharton.mosaic.terminal.KeyboardEvent
import com.jakewharton.mosaic.testing.runMosaicTest
import com.jakewharton.mosaic.ui.Text
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.ui
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import xyz.alaniz.aaron.CircuitApp
import xyz.alaniz.aaron.ui.foundation.MosaicNavDecoration
import xyz.alaniz.aaron.ui.game.GameScreen

class TitleScreenIntegrationTest {
  @Test
  fun testTitleScreenNavigation() = runTest {
    val circuit =
        Circuit.Builder()
            .addPresenterFactory(
                TitleScreenPresenterFactory { navigator -> TitleScreenPresenter(navigator) })
            .addPresenterFactory { screen, _, _ ->
              when (screen) {
                is GameScreen ->
                    object : Presenter<CircuitUiState> {
                      @androidx.compose.runtime.Composable
                      override fun present(): CircuitUiState = object : CircuitUiState {}
                    }
                else -> null
              }
            }
            .addUiFactory(TitleScreenUiFactory())
            .addUiFactory { screen, _ ->
              when (screen) {
                is GameScreen -> ui<CircuitUiState> { _, _ -> Text("Game Screen") }
                else -> null
              }
            }
            .build()

    runMosaicTest {
      setContent { CircuitApp(TitleScreen, circuit, {}, MosaicNavDecoration()) }

      // Initial State: Title Screen
      var snapshot = awaitSnapshot()
      assertTrue(snapshot.contains("Start game"), "Should show Start Game option")
      assertTrue(snapshot.contains("> Start game"), "Start Game should be selected initially")

      // Move down to Settings using 'j'
      sendKeyEvent(KeyboardEvent('j'.code))
      snapshot = awaitSnapshot()
      assertTrue(snapshot.contains("> Settings"), "Settings should be selected after moving down")

      // Move up to Start Game using 'k'
      sendKeyEvent(KeyboardEvent('k'.code))
      snapshot = awaitSnapshot()
      assertTrue(snapshot.contains("> Start game"), "Start Game should be selected after moving up")

      // Select Start Game using Enter (13 - CR)
      sendKeyEvent(KeyboardEvent(13))

      // Wait for navigation to Game Screen
      snapshot = awaitSnapshot()
      assertTrue(snapshot.contains("Game Screen"), "Should navigate to Game Screen")
    }
  }
}
