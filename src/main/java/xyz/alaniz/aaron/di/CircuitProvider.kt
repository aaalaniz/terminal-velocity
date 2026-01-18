package xyz.alaniz.aaron.di

import com.slack.circuit.backstack.NavDecoration
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface CircuitProvider {
  val circuit: Circuit

  @Provides
  @SingleIn(AppScope::class)
  fun provideCircuit(
      presenterFactories: Set<Presenter.Factory>,
      uiFactories: Set<Ui.Factory>,
      navDecoration: NavDecoration
  ): Circuit {
    return Circuit.Builder()
        .addPresenterFactories(presenterFactories)
        .addUiFactories(uiFactories)
        .setDefaultNavDecoration(navDecoration)
        .build()
  }
}
