package com.madruga665.bookmarks.ui.settings

import com.madruga665.bookmarks.data.repository.AppLanguage
import com.madruga665.bookmarks.data.repository.AppThemeMode

data class UsageStatistics(
    val totalBookmarks: Int = 0,
    val bookmarksToday: Int = 0,
    val totalCollections: Int = 0,
    val dailyLimit: Int = 4,
    val collectionLimit: Int = 3
)

data class SettingsUiState(
    val isLoading: Boolean = false,
    val usageStatistics: UsageStatistics = UsageStatistics(),
    val currentTheme: AppThemeMode = AppThemeMode.SYSTEM,
    val currentLanguage: AppLanguage = AppLanguage.SYSTEM,
    val isHapticFeedbackEnabled: Boolean = true,
    val appVersion: String = "1.0.0",
    val errorMessage: String? = null
)

sealed interface SettingsEvent {
    data class SetTheme(val themeMode: AppThemeMode) : SettingsEvent
    data class SetLanguage(val language: AppLanguage) : SettingsEvent
    data class ToggleHapticFeedback(val enabled: Boolean) : SettingsEvent
    data object ExportBackup : SettingsEvent
    data object RestoreBackup : SettingsEvent
    data object ImportBookmarks : SettingsEvent
}
