package xyz.alaniz.aaron.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import xyz.alaniz.aaron.di.IoDispatcher

@ContributesTo(AppScope::class)
interface DataModule {
  @Provides @IoDispatcher fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

  @Provides
  @SingleIn(AppScope::class)
  fun provideResourceReader(): ResourceReader {
    return ClasspathResourceReader()
  }

  @Provides
  @SingleIn(AppScope::class)
  fun provideWordRepository(
      settingsRepository: SettingsRepository,
      resourceReader: ResourceReader,
      @IoDispatcher ioDispatcher: CoroutineDispatcher
  ): WordRepository {
    return MarkdownWordRepository(settingsRepository, resourceReader, ioDispatcher)
  }

  @Provides
  @SingleIn(AppScope::class)
  fun provideSettingsRepository(): SettingsRepository {
    return InMemorySettingsRepository()
  }
}
