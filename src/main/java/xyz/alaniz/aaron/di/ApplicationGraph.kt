package xyz.alaniz.aaron.di

import com.slack.circuit.foundation.Circuit
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.CoroutineScope

@DependencyGraph(AppScope::class)
interface ApplicationGraph {
  val circuit: Circuit

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(
        @Provides appScope: CoroutineScope,
    ): ApplicationGraph
  }
}
