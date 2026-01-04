package xyz.alaniz.aaron.ui.game

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse

class GameStateTest {
    @Test
    fun `GameState has isError property`() {
        val state = GameState.State("word", "", 0, GameStatus.WAITING, isError = true) {}
        // If this compiles and runs, the property exists
        assert(state.isError)
    }
}
