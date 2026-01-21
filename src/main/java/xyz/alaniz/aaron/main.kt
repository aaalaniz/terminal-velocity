@file:JvmName("Main")

package xyz.alaniz.aaron

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.runMosaic
import dev.zacsweers.metro.createGraphFactory
import kotlinx.coroutines.awaitCancellation
import xyz.alaniz.aaron.di.ApplicationGraph
import xyz.alaniz.aaron.ui.title.TitleScreen

suspend fun main() {
  runMosaic {
    val scope = rememberCoroutineScope()
    var exit by remember { mutableStateOf(false) }
    val applicationGraph = createGraphFactory<ApplicationGraph.Factory>().create(appScope = scope)
    CircuitApp(
        initialScreen = TitleScreen,
        circuit = applicationGraph.circuit,
        onRootPop = { exit = true },
        decoration = applicationGraph.navDecoration,
    )
    // Mosaic exits if no effects are running, so we use this to keep it alive until the app exits
    if (!exit) {
      LaunchedEffect(Unit) { awaitCancellation() }
    }
  }
}
