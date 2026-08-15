package com.madruga665.bookmarks.ui.settings

import com.madruga665.bookmarks.data.repository.AppLanguage
import com.madruga665.bookmarks.data.repository.AppThemeMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsScreenTest {

    @Test
    fun usageStatistics_defaultsAndCustomValues() {
        val defaultStats = UsageStatistics()
        assertEquals(0, defaultStats.totalBookmarks)
        assertEquals(0, defaultStats.bookmarksToday)
        assertEquals(0, defaultStats.totalCollections)
        assertEquals(4, defaultStats.dailyLimit)
        assertEquals(3, defaultStats.collectionLimit)

        val customStats = UsageStatistics(
            totalBookmarks = 25,
            bookmarksToday = 3,
            totalCollections = 5,
            dailyLimit = 10,
            collectionLimit = 8
        )
        assertEquals(25, customStats.totalBookmarks)
        assertEquals(3, customStats.bookmarksToday)
        assertEquals(5, customStats.totalCollections)
        assertEquals(10, customStats.dailyLimit)
        assertEquals(8, customStats.collectionLimit)
    }

    @Test
    fun settingsUiState_defaultsAndCopy() {
        val state = SettingsUiState(
            isLoading = false,
            usageStatistics = UsageStatistics(
                totalBookmarks = 10,
                bookmarksToday = 2,
                totalCollections = 3
            ),
            currentTheme = AppThemeMode.LIGHT,
            currentLanguage = AppLanguage.PT_BR,
            isHapticFeedbackEnabled = true,
            appVersion = "1.0.0",
            errorMessage = null
        )

        assertFalse(state.isLoading)
        assertEquals(10, state.usageStatistics.totalBookmarks)
        assertEquals(2, state.usageStatistics.bookmarksToday)
        assertEquals(3, state.usageStatistics.totalCollections)
        assertEquals(AppThemeMode.LIGHT, state.currentTheme)
        assertEquals(AppLanguage.PT_BR, state.currentLanguage)
        assertTrue(state.isHapticFeedbackEnabled)
        assertEquals("1.0.0", state.appVersion)
        assertNull(state.errorMessage)

        val updatedState = state.copy(
            isHapticFeedbackEnabled = false,
            currentTheme = AppThemeMode.CATPPUCCIN_MOCHA
        )
        assertFalse(updatedState.isHapticFeedbackEnabled)
        assertEquals(AppThemeMode.CATPPUCCIN_MOCHA, updatedState.currentTheme)
    }

    @Test
    fun settingsEvent_typesAndProperties() {
        val themeEvent = SettingsEvent.SetTheme(AppThemeMode.SYSTEM)
        assertEquals(AppThemeMode.SYSTEM, themeEvent.themeMode)

        val langEvent = SettingsEvent.SetLanguage(AppLanguage.EN)
        assertEquals(AppLanguage.EN, langEvent.language)

        val hapticEvent = SettingsEvent.ToggleHapticFeedback(false)
        assertFalse(hapticEvent.enabled)

        val exportEvent = SettingsEvent.ExportBackup
        val restoreEvent = SettingsEvent.RestoreBackup
        val importEvent = SettingsEvent.ImportBookmarks

        assertEquals(SettingsEvent.ExportBackup, exportEvent)
        assertEquals(SettingsEvent.RestoreBackup, restoreEvent)
        assertEquals(SettingsEvent.ImportBookmarks, importEvent)
    }
}
