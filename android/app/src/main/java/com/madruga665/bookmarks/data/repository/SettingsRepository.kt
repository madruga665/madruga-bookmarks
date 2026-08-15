package com.madruga665.bookmarks.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_prefs")

enum class AppThemeMode {
    LIGHT,             // High-contrast Neobrutalism Light
    CATPPUCCIN_MOCHA,  // Catppuccin Mocha Dark
    SYSTEM             // Follows Android OS Dark Theme setting
}

enum class AppLanguage(val code: String, val displayName: String) {
    SYSTEM("system", "System Default"),
    EN("en", "English"),
    PT_BR("pt-BR", "Português (Brasil)")
}

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {
    constructor(context: Context) : this(context.settingsDataStore)

    companion object {
        val KEY_THEME_MODE = stringPreferencesKey("app_theme_mode")
        val KEY_LANGUAGE = stringPreferencesKey("app_language")
        val KEY_HAPTIC_ENABLED = booleanPreferencesKey("haptic_feedback_enabled")
    }

    val themeMode: Flow<AppThemeMode> = dataStore.data.map { prefs ->
        when (prefs[KEY_THEME_MODE]) {
            AppThemeMode.LIGHT.name -> AppThemeMode.LIGHT
            AppThemeMode.CATPPUCCIN_MOCHA.name -> AppThemeMode.CATPPUCCIN_MOCHA
            else -> AppThemeMode.SYSTEM
        }
    }

    val language: Flow<AppLanguage> = dataStore.data.map { prefs ->
        when (prefs[KEY_LANGUAGE]) {
            AppLanguage.EN.name -> AppLanguage.EN
            AppLanguage.PT_BR.name -> AppLanguage.PT_BR
            else -> AppLanguage.SYSTEM
        }
    }

    val isHapticEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_HAPTIC_ENABLED] ?: true
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        dataStore.edit { prefs ->
            prefs[KEY_THEME_MODE] = mode.name
        }
    }

    suspend fun setLanguage(language: AppLanguage) {
        dataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = language.name
        }
    }

    suspend fun setHapticEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_HAPTIC_ENABLED] = enabled
        }
    }
}
