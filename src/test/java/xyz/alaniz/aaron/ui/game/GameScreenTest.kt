package xyz.alaniz.aaron.ui.game

import com.slack.circuit.runtime.screen.Screen
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class GameScreenTest {
    @Test
    fun `GameScreen is a Screen`() {
        assertTrue(GameScreen is Screen)
    }
}
