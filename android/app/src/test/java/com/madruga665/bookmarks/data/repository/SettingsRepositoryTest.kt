package com.madruga665.bookmarks.data.repository

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private fun createRepository(): SettingsRepository {
        val testFile = tempFolder.newFile("test_settings.preferences_pb")
        val dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { testFile }
        )
        return SettingsRepository(dataStore)
    }

    @Test
    fun appThemeModeEnum_valuesAreCorrect() {
        assertEquals("LIGHT", AppThemeMode.LIGHT.name)
        assertEquals("CATPPUCCIN_MOCHA", AppThemeMode.CATPPUCCIN_MOCHA.name)
        assertEquals("SYSTEM", AppThemeMode.SYSTEM.name)
    }

    @Test
    fun appLanguageEnum_valuesAndPropertiesAreCorrect() {
        assertEquals("system", AppLanguage.SYSTEM.code)
        assertEquals("System Default", AppLanguage.SYSTEM.displayName)

        assertEquals("en", AppLanguage.EN.code)
        assertEquals("English", AppLanguage.EN.displayName)

        assertEquals("pt-BR", AppLanguage.PT_BR.code)
        assertEquals("Português (Brasil)", AppLanguage.PT_BR.displayName)
    }

    @Test
    fun settingsRepository_defaultsAreCorrect() = runTest(testDispatcher) {
        val repository = createRepository()

        assertEquals(AppThemeMode.SYSTEM, repository.themeMode.first())
        assertEquals(AppLanguage.SYSTEM, repository.language.first())
        assertTrue(repository.isHapticEnabled.first())
    }

    @Test
    fun setThemeMode_persistsAndEmitsUpdatedTheme() = runTest(testDispatcher) {
        val repository = createRepository()

        repository.setThemeMode(AppThemeMode.LIGHT)
        assertEquals(AppThemeMode.LIGHT, repository.themeMode.first())

        repository.setThemeMode(AppThemeMode.CATPPUCCIN_MOCHA)
        assertEquals(AppThemeMode.CATPPUCCIN_MOCHA, repository.themeMode.first())

        repository.setThemeMode(AppThemeMode.SYSTEM)
        assertEquals(AppThemeMode.SYSTEM, repository.themeMode.first())
    }

    @Test
    fun setLanguage_persistsAndEmitsUpdatedLanguage() = runTest(testDispatcher) {
        val repository = createRepository()

        repository.setLanguage(AppLanguage.PT_BR)
        assertEquals(AppLanguage.PT_BR, repository.language.first())

        repository.setLanguage(AppLanguage.EN)
        assertEquals(AppLanguage.EN, repository.language.first())

        repository.setLanguage(AppLanguage.SYSTEM)
        assertEquals(AppLanguage.SYSTEM, repository.language.first())
    }

    @Test
    fun setHapticEnabled_persistsAndEmitsUpdatedFlag() = runTest(testDispatcher) {
        val repository = createRepository()

        repository.setHapticEnabled(false)
        assertFalse(repository.isHapticEnabled.first())

        repository.setHapticEnabled(true)
        assertTrue(repository.isHapticEnabled.first())
    }

    @Test
    fun themeRepository_delegatesToSettingsRepository() = runTest(testDispatcher) {
        val settingsRepo = createRepository()
        val themeRepo = ThemeRepository(settingsRepo)

        assertEquals(AppThemeMode.SYSTEM, themeRepo.themeMode.first())

        themeRepo.setThemeMode(AppThemeMode.CATPPUCCIN_MOCHA)
        assertEquals(AppThemeMode.CATPPUCCIN_MOCHA, themeRepo.themeMode.first())
        assertEquals(AppThemeMode.CATPPUCCIN_MOCHA, settingsRepo.themeMode.first())
    }
}
