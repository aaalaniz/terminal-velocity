package xyz.alaniz.aaron.ui.title

import com.google.common.truth.Truth.assertThat
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import xyz.alaniz.aaron.ui.game.GameScreen
import xyz.alaniz.aaron.ui.settings.SettingsScreen

class TitleScreenPresenterTest {
  @Test
  fun `initial state is correct`() = runTest {
    val navigator = FakeNavigator(TitleScreen)
    val presenter = TitleScreenPresenter(navigator)

    presenter.test {
      val initialState = awaitItem()
      assertThat(initialState.selectedTitleScreenOptionIndex).isEqualTo(0)
      assertThat(initialState.selectableOptions).hasSize(3)
      assertThat(initialState.selectableOptions[0]).isEqualTo(TitleScreenOption.StartGame)
    }
  }

  @Test
  fun `NextTitleOption increments index`() = runTest {
    val navigator = FakeNavigator(TitleScreen)
    val presenter = TitleScreenPresenter(navigator)

    presenter.test {
      val initialState = awaitItem()
      initialState.onEvent(TitleScreenEvent.NextTitleOption)
      assertThat(awaitItem().selectedTitleScreenOptionIndex).isEqualTo(1)
    }
  }

  @Test
  fun `NextTitleOption is clamped at the end`() = runTest {
    val navigator = FakeNavigator(TitleScreen)
    val presenter = TitleScreenPresenter(navigator)

    presenter.test {
      var state = awaitItem()
      // There are 3 options, max index is 2
      // To index 1
      state.onEvent(TitleScreenEvent.NextTitleOption)
      state = awaitItem()
      // To index 2
      state.onEvent(TitleScreenEvent.NextTitleOption)
      state = awaitItem()

      // Further events should not emit new state
      state.onEvent(TitleScreenEvent.NextTitleOption)
      expectNoEvents()

      assertThat(state.selectedTitleScreenOptionIndex).isEqualTo(2)
    }
  }

  @Test
  fun `PreviousTitleOption decrements index`() = runTest {
    val navigator = FakeNavigator(TitleScreen)
    val presenter = TitleScreenPresenter(navigator)

    presenter.test {
      var state = awaitItem()
      state.onEvent(TitleScreenEvent.NextTitleOption)
      state = awaitItem() // Consume index 1

      state.onEvent(TitleScreenEvent.PreviousTitleOption)
      assertThat(awaitItem().selectedTitleScreenOptionIndex).isEqualTo(0)
    }
  }

  @Test
  fun `PreviousTitleOption is clamped at 0`() = runTest {
    val navigator = FakeNavigator(TitleScreen)
    val presenter = TitleScreenPresenter(navigator)

    presenter.test {
      var state = awaitItem()
      state.onEvent(TitleScreenEvent.PreviousTitleOption)
      // Should still be 0, but no new item might be emitted if state didn't change.
      // Actually it might emit the same state or not emit at all if using mutableStateOf.
      // If it doesn't emit, awaitItem will timeout.
      // In the presenter, if index is 0, (0-1).coerceAtLeast(0) is 0. No change.
      expectNoEvents()
      assertThat(state.selectedTitleScreenOptionIndex).isEqualTo(0)
    }
  }

  @Test
  fun `TitleOptionSelected StartGame navigates to GameScreen`() = runTest {
    val navigator = FakeNavigator(TitleScreen)
    val presenter = TitleScreenPresenter(navigator)

    presenter.test {
      val initialState = awaitItem()
      initialState.onEvent(TitleScreenEvent.TitleOptionSelected(TitleScreenOption.StartGame))
      assertThat(navigator.awaitNextScreen()).isEqualTo(GameScreen)
    }
  }

  @Test
  fun `TitleOptionSelected Settings navigates to SettingsScreen`() = runTest {
    val navigator = FakeNavigator(TitleScreen)
    val presenter = TitleScreenPresenter(navigator)

    presenter.test {
      val initialState = awaitItem()
      initialState.onEvent(TitleScreenEvent.TitleOptionSelected(TitleScreenOption.Settings))
      assertThat(navigator.awaitNextScreen()).isEqualTo(SettingsScreen)
    }
  }

  @Test
  fun `TitleOptionSelected Quit pops navigation`() = runTest {
    val navigator = FakeNavigator(TitleScreen)
    val presenter = TitleScreenPresenter(navigator)

    presenter.test {
      val initialState = awaitItem()
      initialState.onEvent(TitleScreenEvent.TitleOptionSelected(TitleScreenOption.Quit))
      navigator.awaitPop()
    }
  }
}
