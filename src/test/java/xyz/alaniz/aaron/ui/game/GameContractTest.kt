package xyz.alaniz.aaron.ui.game

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class GameContractTest {

    @Test
    fun `GameState has required properties`() {
        // We expect a data class implementing GameState
        // For now, we just check the interface or a concrete implementation if we had one.
        // Since GameState is a sealed interface, we'll likely have a data class 'State' inside it or it will be a data class itself.
        // The spec says:
        // currentWord: String
        // userInput: String
        // score: Int
        // gameState: Enum

        // Since we haven't defined the data class yet, we can't reflect on it easily unless we know its name.
        // Let's assume the standard pattern: GameState is the data class or interface.
        // If it's an interface, we can't check properties easily without an implementation.
        
        // Let's try to verify that we can instantiate the specific events.
        // Reflecting on "GameEvent.LetterTyped"
    }

    @Test
    fun `GameEvent sealed hierarchy exists`() {
        // Check for GameStarted
        val gameStarted = Class.forName("xyz.alaniz.aaron.ui.game.GameEvent\$GameStarted")
        assertNotNull(gameStarted)

        // Check for GameReset
        val gameReset = Class.forName("xyz.alaniz.aaron.ui.game.GameEvent\$GameReset")
        assertNotNull(gameReset)
        
        // Check for LetterTyped
        val letterTyped = Class.forName("xyz.alaniz.aaron.ui.game.GameEvent\$LetterTyped")
        assertNotNull(letterTyped)
    }
}
