package xyz.alaniz.aaron.data

import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<Settings>
    fun updateSettings(transform: (Settings) -> Settings)
}
