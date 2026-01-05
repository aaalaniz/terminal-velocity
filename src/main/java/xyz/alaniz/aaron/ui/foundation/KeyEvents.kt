package xyz.alaniz.aaron.ui.foundation

import com.jakewharton.mosaic.layout.KeyEvent

data object KeyEvents {
    val Enter = KeyEvent("Enter")

    val ArrowUp = KeyEvent("ArrowUp")
    val k = KeyEvent("k")

    val ArrowDown = KeyEvent("ArrowDown")
    val j = KeyEvent("j")

    val b = KeyEvent("b")
    val CtrlC = KeyEvent("c", ctrl = true)
}


