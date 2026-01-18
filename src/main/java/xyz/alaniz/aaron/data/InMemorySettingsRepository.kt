package xyz.alaniz.aaron.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemorySettingsRepository : SettingsRepository {
  private val _settings = MutableStateFlow(Settings())
  override val settings: StateFlow<Settings> = _settings.asStateFlow()

  override fun updateSettings(transform: (Settings) -> Settings) {
    _settings.update(transform)
  }
}
