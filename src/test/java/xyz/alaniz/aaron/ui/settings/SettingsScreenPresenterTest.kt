package xyz.alaniz.aaron.ui.settings

import com.google.common.truth.Truth.assertThat
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlin.test.Test
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
      assertThat(state.items).hasSize(1) // Only "Enable Code Snippets" initially
      assertThat(state.items[0].label).isEqualTo("Enable Code Snippets")
      assertThat(state.items[0].isChecked).isFalse()
      assertThat(state.focusedIndex).isEqualTo(0)
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
      assertThat(newState.items[0].isChecked).isTrue()
      assertThat(newState.items.size).isGreaterThan(1) // Should have languages now

      val kotlinItem = newState.items.find { it.label == "Kotlin" }
      assertThat(kotlinItem).isNotNull()
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
      assertThat(state.focusedIndex).isEqualTo(0)

      state.onEvent(SettingsScreenEvent.MoveFocusDown)
      val stateAfterDown = awaitItem()
      assertThat(stateAfterDown.focusedIndex).isEqualTo(1)

      stateAfterDown.onEvent(SettingsScreenEvent.MoveFocusUp)
      val stateAfterUp = awaitItem()
      assertThat(stateAfterUp.focusedIndex).isEqualTo(0)
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
      assertThat(stateFocused.focusedIndex).isEqualTo(2)
      assertThat(stateFocused.items[2].label).isEqualTo("Kotlin")
      assertThat(stateFocused.items[2].isChecked).isFalse()

      stateFocused.onEvent(SettingsScreenEvent.ToggleCurrentItem)

      val stateToggled = awaitItem()
      assertThat(stateToggled.items[2].isChecked).isTrue()
      val settings = repository.settings.value.codeSnippetSettings
      assertThat(settings).isInstanceOf(CodeSnippetSettings.Enabled::class.java)
      assertThat((settings as CodeSnippetSettings.Enabled).selectedLanguages)
          .contains(Language.KOTLIN)
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
      assertThat(stateFocused.focusedIndex).isEqualTo(1)
      assertThat(stateFocused.items[1].label).isEqualTo("Only Code Snippets")
      assertThat(stateFocused.items[1].isChecked).isFalse()

      stateFocused.onEvent(SettingsScreenEvent.ToggleCurrentItem)

      val stateToggled = awaitItem()
      assertThat(stateToggled.items[1].isChecked).isTrue()
      val settings = repository.settings.value.codeSnippetSettings
      assertThat(settings).isInstanceOf(CodeSnippetSettings.Enabled::class.java)
      assertThat((settings as CodeSnippetSettings.Enabled).onlyCodeSnippets).isTrue()
    }
  }
}
