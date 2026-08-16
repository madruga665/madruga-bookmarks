package com.madruga665.bookmarks.data.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow

class ThemeRepository(private val settingsRepository: SettingsRepository) {

    constructor(context: Context) : this(SettingsRepository(context))

    val themeMode: Flow<AppThemeMode> = settingsRepository.themeMode

    suspend fun setThemeMode(mode: AppThemeMode) {
        settingsRepository.setThemeMode(mode)
    }
}
