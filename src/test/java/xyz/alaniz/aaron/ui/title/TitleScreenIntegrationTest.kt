package xyz.alaniz.aaron.ui.title

import com.jakewharton.mosaic.terminal.KeyboardEvent
import com.jakewharton.mosaic.testing.runMosaicTest
import dev.zacsweers.metro.createGraphFactory
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
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

      // Wait for navigation
      snapshot = awaitSnapshot()
      // TODO Validate the game screen content
      assertFalse(snapshot.isEmpty())
    }
  }
}
