package com.madruga665.bookmarks.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madruga665.bookmarks.data.repository.AppLanguage
import com.madruga665.bookmarks.data.repository.AppThemeMode
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import com.madruga665.bookmarks.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        bookmarkRepository.allBookmarks,
        collectionRepository.collections,
        settingsRepository.themeMode,
        settingsRepository.language,
        settingsRepository.isHapticEnabled
    ) { bookmarks, collections, themeMode, language, isHaptic ->
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDayMillis = calendar.timeInMillis
        val bookmarksTodayCount = bookmarks.count { it.createdAt >= startOfDayMillis }

        SettingsUiState(
            isLoading = false,
            usageStatistics = UsageStatistics(
                totalBookmarks = bookmarks.size,
                bookmarksToday = bookmarksTodayCount,
                totalCollections = collections.size,
                dailyLimit = 4,
                collectionLimit = 3
            ),
            currentTheme = themeMode,
            currentLanguage = language,
            isHapticFeedbackEnabled = isHaptic,
            appVersion = "1.0.0",
            errorMessage = null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState(isLoading = true)
    )

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SetTheme -> setThemeMode(event.themeMode)
            is SettingsEvent.SetLanguage -> setLanguage(event.language)
            is SettingsEvent.ToggleHapticFeedback -> toggleHapticFeedback(event.enabled)
            is SettingsEvent.ExportBackup -> { /* Handled in UI / export handler */ }
            is SettingsEvent.RestoreBackup -> { /* Handled in UI / restore handler */ }
            is SettingsEvent.ImportBookmarks -> { /* Handled in UI / import handler */ }
        }
    }

    fun setThemeMode(mode: AppThemeMode) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(mode)
        }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            settingsRepository.setLanguage(language)
            applyLocale(language)
        }
    }

    fun toggleHapticFeedback(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setHapticEnabled(enabled)
        }
    }

    private fun applyLocale(language: AppLanguage) {
        val localeList = when (language) {
            AppLanguage.EN -> LocaleListCompat.forLanguageTags("en")
            AppLanguage.PT_BR -> LocaleListCompat.forLanguageTags("pt-BR")
            AppLanguage.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
        }
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}
