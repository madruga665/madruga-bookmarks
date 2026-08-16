package com.madruga665.bookmarks.ui.collection

import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val collectionRepository: CollectionRepository = mockk(relaxed = true)
    private val bookmarkRepository: BookmarkRepository = mockk(relaxed = true)
    private lateinit var viewModel: CollectionDetailViewModel

    private val targetCollectionId = "col_ia"
    private val sampleCollection = CollectionEntity(
        id = "col_ia",
        name = "IA",
        linkCount = 2,
        subcollectionCount = 0,
        parentId = null,
        iconKey = "code",
        colorAccent = "YELLOW",
        createdAt = 1000L,
        updatedAt = 1000L
    )

    private val sampleBookmarks = listOf(
        BookmarkEntity(
            id = "bm_1",
            url = "https://openai.com",
            title = "OpenAI",
            faviconUrl = null,
            collectionId = "col_ia",
            isPinned = false,
            createdAt = 1000L
        ),
        BookmarkEntity(
            id = "bm_2",
            url = "https://anthropic.com",
            title = "Anthropic",
            faviconUrl = null,
            collectionId = "col_ia",
            isPinned = true,
            createdAt = 1001L
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { collectionRepository.getCollectionById(targetCollectionId) } returns flowOf(sampleCollection)
        coEvery { bookmarkRepository.getBookmarksByCollection(targetCollectionId) } returns flowOf(sampleBookmarks)

        viewModel = CollectionDetailViewModel(
            collectionId = targetCollectionId,
            collectionRepository = collectionRepository,
            bookmarkRepository = bookmarkRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiState_combinesCollectionAndBookmarks_updatesStateCorrectly() {
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(sampleCollection, state.collection)
        assertEquals(2, state.bookmarks.size)
        assertEquals("bm_1", state.bookmarks[0].id)
        assertEquals("bm_2", state.bookmarks[1].id)
        assertNull(state.error)
    }

    @Test
    fun uiState_whenCollectionNotFound_returnsNullCollectionInState() {
        val unknownId = "col_unknown"
        coEvery { collectionRepository.getCollectionById(unknownId) } returns flowOf(null)
        coEvery { bookmarkRepository.getBookmarksByCollection(unknownId) } returns flowOf(emptyList())

        val unknownViewModel = CollectionDetailViewModel(
            collectionId = unknownId,
            collectionRepository = collectionRepository,
            bookmarkRepository = bookmarkRepository
        )

        val state = unknownViewModel.uiState.value

        assertFalse(state.isLoading)
        assertNull(state.collection)
        assertEquals(0, state.bookmarks.size)
    }

    @Test
    fun uiState_whenBookmarksEmpty_returnsEmptyBookmarksListInState() {
        coEvery { bookmarkRepository.getBookmarksByCollection(targetCollectionId) } returns flowOf(emptyList())

        val emptyBookmarksViewModel = CollectionDetailViewModel(
            collectionId = targetCollectionId,
            collectionRepository = collectionRepository,
            bookmarkRepository = bookmarkRepository
        )

        val state = emptyBookmarksViewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(sampleCollection, state.collection)
        assertEquals(0, state.bookmarks.size)
    }
}
