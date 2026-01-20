package xyz.alaniz.aaron.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class InMemorySettingsRepository : SettingsRepository {
  private val _settings = MutableStateFlow(Settings())
  override val settings: StateFlow<Settings> = _settings.asStateFlow()

  override fun updateSettings(transform: (Settings) -> Settings) {
    _settings.update(transform)
  }
}
