package xyz.alaniz.aaron.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.CoroutineScope

@DependencyGraph(AppScope::class)
interface ApplicationGraph {

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(
        @Provides appScope: CoroutineScope,
    ): ApplicationGraph
  }
}
