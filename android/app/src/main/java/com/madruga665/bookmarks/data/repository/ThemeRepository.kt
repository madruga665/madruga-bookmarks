package com.madruga665.bookmarks.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "theme_prefs")

enum class AppThemeMode {
    LIGHT,             // Light mode (design screenshot reference)
    CATPPUCCIN_MOCHA,  // Dark mode (Catppuccin Mocha palette)
    SYSTEM             // Follow system preference
}

class ThemeRepository(private val context: Context) {

    private val THEME_KEY = stringPreferencesKey("app_theme_mode")

    val themeMode: Flow<AppThemeMode> = context.dataStore.data.map { prefs ->
        when (prefs[THEME_KEY]) {
            AppThemeMode.LIGHT.name -> AppThemeMode.LIGHT
            AppThemeMode.CATPPUCCIN_MOCHA.name -> AppThemeMode.CATPPUCCIN_MOCHA
            else -> AppThemeMode.SYSTEM
        }
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = mode.name
        }
    }
}
