package com.madruga665.bookmarks.ui.settings

import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.data.repository.AppLanguage
import com.madruga665.bookmarks.data.repository.AppThemeMode
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import com.madruga665.bookmarks.data.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val settingsRepository: SettingsRepository = mockk(relaxed = true)
    private val bookmarkRepository: BookmarkRepository = mockk(relaxed = true)
    private val collectionRepository: CollectionRepository = mockk(relaxed = true)

    private val themeModeFlow = MutableStateFlow(AppThemeMode.SYSTEM)
    private val languageFlow = MutableStateFlow(AppLanguage.SYSTEM)
    private val hapticFlow = MutableStateFlow(true)
    private val bookmarksFlow = MutableStateFlow<List<BookmarkEntity>>(emptyList())
    private val collectionsFlow = MutableStateFlow<List<CollectionEntity>>(emptyList())

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { settingsRepository.themeMode } returns themeModeFlow
        coEvery { settingsRepository.language } returns languageFlow
        coEvery { settingsRepository.isHapticEnabled } returns hapticFlow
        coEvery { bookmarkRepository.allBookmarks } returns bookmarksFlow
        coEvery { collectionRepository.collections } returns collectionsFlow

        viewModel = SettingsViewModel(
            settingsRepository = settingsRepository,
            bookmarkRepository = bookmarkRepository,
            collectionRepository = collectionRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiState_combinesAllFlowsAccurately() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfToday = calendar.timeInMillis

        val sampleBookmarks = listOf(
            BookmarkEntity(
                id = "1",
                url = "https://example.com/1",
                title = "Today Bookmark 1",
                faviconUrl = null,
                thumbnailUrl = null,
                sourcePlatform = "Web",
                collectionId = "col_1",
                isPinned = false,
                createdAt = startOfToday + 10000,
                syncStatus = "SYNCED"
            ),
            BookmarkEntity(
                id = "2",
                url = "https://example.com/2",
                title = "Today Bookmark 2",
                faviconUrl = null,
                thumbnailUrl = null,
                sourcePlatform = "Web",
                collectionId = "col_1",
                isPinned = false,
                createdAt = startOfToday + 50000,
                syncStatus = "SYNCED"
            ),
            BookmarkEntity(
                id = "3",
                url = "https://example.com/3",
                title = "Yesterday Bookmark",
                faviconUrl = null,
                thumbnailUrl = null,
                sourcePlatform = "Web",
                collectionId = "col_1",
                isPinned = false,
                createdAt = startOfToday - 86400000,
                syncStatus = "SYNCED"
            )
        )

        val sampleCollections = listOf(
            CollectionEntity(
                id = "col_1",
                name = "Design",
                linkCount = 3,
                subcollectionCount = 0,
                parentId = null,
                iconKey = "folder",
                colorAccent = "YELLOW",
                createdAt = now,
                updatedAt = now
            ),
            CollectionEntity(
                id = "col_2",
                name = "Dev",
                linkCount = 0,
                subcollectionCount = 0,
                parentId = null,
                iconKey = "folder",
                colorAccent = "PURPLE",
                createdAt = now,
                updatedAt = now
            )
        )

        bookmarksFlow.value = sampleBookmarks
        collectionsFlow.value = sampleCollections
        themeModeFlow.value = AppThemeMode.CATPPUCCIN_MOCHA
        languageFlow.value = AppLanguage.PT_BR
        hapticFlow.value = false

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(3, state.usageStatistics.totalBookmarks)
        assertEquals(2, state.usageStatistics.bookmarksToday)
        assertEquals(2, state.usageStatistics.totalCollections)
        assertEquals(4, state.usageStatistics.dailyLimit)
        assertEquals(3, state.usageStatistics.collectionLimit)
        assertEquals(AppThemeMode.CATPPUCCIN_MOCHA, state.currentTheme)
        assertEquals(AppLanguage.PT_BR, state.currentLanguage)
        assertFalse(state.isHapticFeedbackEnabled)
    }

    @Test
    fun setThemeMode_delegatesToRepository() = runTest(testDispatcher) {
        viewModel.setThemeMode(AppThemeMode.LIGHT)
        coVerify(exactly = 1) { settingsRepository.setThemeMode(AppThemeMode.LIGHT) }

        viewModel.onEvent(SettingsEvent.SetTheme(AppThemeMode.CATPPUCCIN_MOCHA))
        coVerify(exactly = 1) { settingsRepository.setThemeMode(AppThemeMode.CATPPUCCIN_MOCHA) }
    }

    @Test
    fun setLanguage_delegatesToRepository() = runTest(testDispatcher) {
        viewModel.setLanguage(AppLanguage.PT_BR)
        coVerify(exactly = 1) { settingsRepository.setLanguage(AppLanguage.PT_BR) }

        viewModel.onEvent(SettingsEvent.SetLanguage(AppLanguage.EN))
        coVerify(exactly = 1) { settingsRepository.setLanguage(AppLanguage.EN) }
    }

    @Test
    fun toggleHapticFeedback_delegatesToRepository() = runTest(testDispatcher) {
        viewModel.toggleHapticFeedback(false)
        coVerify(exactly = 1) { settingsRepository.setHapticEnabled(false) }

        viewModel.onEvent(SettingsEvent.ToggleHapticFeedback(true))
        coVerify(exactly = 1) { settingsRepository.setHapticEnabled(true) }
    }
}
