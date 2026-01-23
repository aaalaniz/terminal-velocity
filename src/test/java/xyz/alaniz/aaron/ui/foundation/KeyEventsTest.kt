package xyz.alaniz.aaron.ui.foundation

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class KeyEventsTest {

  @Test
  fun `Enter key event is correct`() {
    assertThat(KeyEvents.Enter.key).isEqualTo("Enter")
    assertThat(KeyEvents.Enter.ctrl).isFalse()
  }

  @Test
  fun `ArrowUp key event is correct`() {
    assertThat(KeyEvents.ArrowUp.key).isEqualTo("ArrowUp")
    assertThat(KeyEvents.ArrowUp.ctrl).isFalse()
  }

  @Test
  fun `k key event is correct`() {
    assertThat(KeyEvents.k.key).isEqualTo("k")
    assertThat(KeyEvents.k.ctrl).isFalse()
  }

  @Test
  fun `ArrowDown key event is correct`() {
    assertThat(KeyEvents.ArrowDown.key).isEqualTo("ArrowDown")
    assertThat(KeyEvents.ArrowDown.ctrl).isFalse()
  }

  @Test
  fun `j key event is correct`() {
    assertThat(KeyEvents.j.key).isEqualTo("j")
    assertThat(KeyEvents.j.ctrl).isFalse()
  }

  @Test
  fun `b key event is correct`() {
    assertThat(KeyEvents.b.key).isEqualTo("b")
    assertThat(KeyEvents.b.ctrl).isFalse()
  }

  @Test
  fun `CtrlC key event is correct`() {
    assertThat(KeyEvents.CtrlC.key).isEqualTo("c")
    assertThat(KeyEvents.CtrlC.ctrl).isTrue()
  }
}
