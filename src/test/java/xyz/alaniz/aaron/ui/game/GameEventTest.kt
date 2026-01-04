package xyz.alaniz.aaron.ui.game

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class GameEventTest {
    @Test
    fun `GameEvent includes ClearError`() {
        val clearError = Class.forName("xyz.alaniz.aaron.ui.game.GameEvent\$ClearError")
        assertNotNull(clearError)
    }
}
