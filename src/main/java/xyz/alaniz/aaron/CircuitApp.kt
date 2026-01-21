package xyz.alaniz.aaron

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.backstack.NavDecoration
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.screen.Screen
import xyz.alaniz.aaron.ui.foundation.MosaicNavDecoration

@Composable
fun CircuitApp(
    initialScreen: Screen,
    circuit: Circuit,
    onRootPop: () -> Unit,
) {
  val backStack = rememberSaveableBackStack(root = initialScreen)
  val navigator = rememberCircuitNavigator(backStack) { onRootPop() }
  val decoration = remember { MosaicNavDecoration() }

  CircuitCompositionLocals(circuit) {
    NavigableCircuitContent(navigator = navigator, backStack = backStack, decoration = decoration)
  }
}
