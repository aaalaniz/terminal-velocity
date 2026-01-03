package xyz.alaniz.aaron

import androidx.compose.runtime.Composable
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.screen.Screen

@Composable
fun CircuitApp(initialScreen: Screen, circuit: Circuit, onRootPop: () -> Unit) {
    val backStack = rememberSaveableBackStack(root = initialScreen)
    val navigator = rememberCircuitNavigator(backStack) {
        onRootPop()
    }

    CircuitCompositionLocals(circuit) {
        NavigableCircuitContent(
            navigator = navigator,
            backStack = backStack
        )
    }
}