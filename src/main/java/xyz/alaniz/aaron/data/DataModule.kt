package xyz.alaniz.aaron.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import xyz.alaniz.aaron.di.IoDispatcher

@ContributesTo(AppScope::class)
interface DataModule {
  @Provides @IoDispatcher fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
