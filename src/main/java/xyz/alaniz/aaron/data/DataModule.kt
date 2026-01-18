package xyz.alaniz.aaron.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface DataModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideWordRepository(settingsRepository: SettingsRepository): WordRepository {
        return InMemoryWordRepository(settingsRepository)
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideSettingsRepository(): SettingsRepository {
        return InMemorySettingsRepository()
    }
}
