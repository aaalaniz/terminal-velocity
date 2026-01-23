package xyz.alaniz.aaron.ui.title

import com.google.common.truth.Truth.assertThat
import com.jakewharton.mosaic.terminal.KeyboardEvent
import com.jakewharton.mosaic.testing.runMosaicTest
import dev.zacsweers.metro.createGraphFactory
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import xyz.alaniz.aaron.CircuitApp
import xyz.alaniz.aaron.di.ApplicationGraph

class TitleScreenIntegrationTest {
  @Test
  fun testTitleScreenNavigation() = runTest {
    runMosaicTest {
      val scope = this@runTest
      val applicationGraph = createGraphFactory<ApplicationGraph.Factory>().create(appScope = scope)
      setContent { CircuitApp(TitleScreen, applicationGraph.circuit, {}) }

      // Initial State: Title Screen
      var snapshot = awaitSnapshot()
      assertThat(snapshot).contains("Start game")
      assertThat(snapshot).contains("> Start game")

      // Move down to Settings using 'j'
      sendKeyEvent(KeyboardEvent('j'.code))
      snapshot = awaitSnapshot()
      assertThat(snapshot).contains("> Settings")

      // Move up to Start Game using 'k'
      sendKeyEvent(KeyboardEvent('k'.code))
      snapshot = awaitSnapshot()
      assertThat(snapshot).contains("> Start game")

      // Select Start Game using Enter (13 - CR)
      sendKeyEvent(KeyboardEvent(13))

      // Wait for navigation
      snapshot = awaitSnapshot()
      // TODO Validate the game screen content
      assertThat(snapshot).isNotEmpty()
    }
  }
}
