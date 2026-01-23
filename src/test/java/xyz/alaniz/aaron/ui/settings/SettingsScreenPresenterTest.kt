package xyz.alaniz.aaron.ui.settings

import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import xyz.alaniz.aaron.data.CodeSnippetSettings
import xyz.alaniz.aaron.data.InMemorySettingsRepository
import xyz.alaniz.aaron.data.Language

class SettingsScreenPresenterTest {
  @Test
  fun `initial state is correct`() = runTest {
    val repository = InMemorySettingsRepository()
    val presenter = SettingsScreenPresenter(FakeNavigator(SettingsScreen), repository)

    presenter.test {
      val state = awaitItem()
      assertEquals(1, state.items.size) // Only "Enable Code Snippets" initially
      assertEquals("Enable Code Snippets", state.items[0].label)
      assertFalse(state.items[0].isChecked)
      assertEquals(0, state.focusedIndex)
    }
  }

  @Test
  fun `toggling enable code snippets shows languages`() = runTest {
    val repository = InMemorySettingsRepository()
    val presenter = SettingsScreenPresenter(FakeNavigator(SettingsScreen), repository)

    presenter.test {
      val initialState = awaitItem()
      initialState.onEvent(SettingsScreenEvent.ToggleCurrentItem)

      val newState = awaitItem()
      assertTrue(newState.items[0].isChecked)
      assertTrue(newState.items.size > 1) // Should have languages now

      val kotlinItem = newState.items.find { it.label == "Kotlin" }
      assertTrue(kotlinItem != null)
    }
  }

  @Test
  fun `navigation works`() = runTest {
    val repository = InMemorySettingsRepository()
    // Enable snippets by default for this test to have more items
    repository.updateSettings { it.copy(codeSnippetSettings = CodeSnippetSettings.Enabled()) }

    val presenter = SettingsScreenPresenter(FakeNavigator(SettingsScreen), repository)

    presenter.test {
      val state = awaitItem()
      assertEquals(0, state.focusedIndex)

      state.onEvent(SettingsScreenEvent.MoveFocusDown)
      val stateAfterDown = awaitItem()
      assertEquals(1, stateAfterDown.focusedIndex)

      stateAfterDown.onEvent(SettingsScreenEvent.MoveFocusUp)
      val stateAfterUp = awaitItem()
      assertEquals(0, stateAfterUp.focusedIndex)
    }
  }

  @Test
  fun `toggling language works`() = runTest {
    val repository = InMemorySettingsRepository()
    repository.updateSettings { it.copy(codeSnippetSettings = CodeSnippetSettings.Enabled()) }

    val presenter = SettingsScreenPresenter(FakeNavigator(SettingsScreen), repository)

    presenter.test {
      val state = awaitItem()
      // Index 0: Enable Code Snippets
      // Index 1: Only Code Snippets
      // Index 2: Kotlin
      state.onEvent(SettingsScreenEvent.MoveFocusDown) // Focus 1
      awaitItem() // Consume state update
      state.onEvent(SettingsScreenEvent.MoveFocusDown) // Focus 2

      val stateFocused = awaitItem()
      assertEquals(2, stateFocused.focusedIndex)
      assertEquals("Kotlin", stateFocused.items[2].label)
      assertFalse(stateFocused.items[2].isChecked)

      stateFocused.onEvent(SettingsScreenEvent.ToggleCurrentItem)

      val stateToggled = awaitItem()
      assertTrue(stateToggled.items[2].isChecked)
      val settings = repository.settings.value.codeSnippetSettings
      assertTrue(settings is CodeSnippetSettings.Enabled)
      assertTrue(
          (settings as CodeSnippetSettings.Enabled).selectedLanguages.contains(Language.KOTLIN))
    }
  }

  @Test
  fun `toggling only code snippets works`() = runTest {
    val repository = InMemorySettingsRepository()
    repository.updateSettings { it.copy(codeSnippetSettings = CodeSnippetSettings.Enabled()) }

    val presenter = SettingsScreenPresenter(FakeNavigator(SettingsScreen), repository)

    presenter.test {
      val state = awaitItem()
      // Index 0: Enable Code Snippets
      // Index 1: Only Code Snippets
      state.onEvent(SettingsScreenEvent.MoveFocusDown) // Focus 1

      val stateFocused = awaitItem()
      assertEquals(1, stateFocused.focusedIndex)
      assertEquals("Only Code Snippets", stateFocused.items[1].label)
      assertFalse(stateFocused.items[1].isChecked)

      stateFocused.onEvent(SettingsScreenEvent.ToggleCurrentItem)

      val stateToggled = awaitItem()
      assertTrue(stateToggled.items[1].isChecked)
      val settings = repository.settings.value.codeSnippetSettings
      assertTrue(settings is CodeSnippetSettings.Enabled)
      assertTrue((settings as CodeSnippetSettings.Enabled).onlyCodeSnippets)
    }
  }
}
