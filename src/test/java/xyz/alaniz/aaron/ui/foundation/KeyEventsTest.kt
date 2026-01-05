package xyz.alaniz.aaron.ui.foundation

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KeyEventsTest {

    @Test
    fun `Enter key event is correct`() {
        assertEquals("Enter", KeyEvents.Enter.key)
        assertFalse(KeyEvents.Enter.ctrl)
    }

    @Test
    fun `ArrowUp key event is correct`() {
        assertEquals("ArrowUp", KeyEvents.ArrowUp.key)
        assertFalse(KeyEvents.ArrowUp.ctrl)
    }

    @Test
    fun `k key event is correct`() {
        assertEquals("k", KeyEvents.k.key)
        assertFalse(KeyEvents.k.ctrl)
    }

    @Test
    fun `ArrowDown key event is correct`() {
        assertEquals("ArrowDown", KeyEvents.ArrowDown.key)
        assertFalse(KeyEvents.ArrowDown.ctrl)
    }

    @Test
    fun `j key event is correct`() {
        assertEquals("j", KeyEvents.j.key)
        assertFalse(KeyEvents.j.ctrl)
    }
    
    @Test
    fun `b key event is correct`() {
        assertEquals("b", KeyEvents.b.key)
        assertFalse(KeyEvents.b.ctrl)
    }

    @Test
    fun `CtrlC key event is correct`() {
        assertEquals("c", KeyEvents.CtrlC.key)
        assertTrue(KeyEvents.CtrlC.ctrl)
    }
}
