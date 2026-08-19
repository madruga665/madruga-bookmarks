package com.madruga665.bookmarks.ui.collection

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import com.madruga665.bookmarks.ui.components.BookmarkOption
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
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
    fun uiState_combinesCollectionAndBookmarks_updatesStateCorrectly() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(sampleCollection, state.collection)
        assertEquals(2, state.bookmarks.size)
        assertEquals("bm_1", state.bookmarks[0].id)
        assertEquals("bm_2", state.bookmarks[1].id)
        assertNull(state.error)

        collectJob.cancel()
    }

    @Test
    fun uiState_whenCollectionNotFound_returnsNullCollectionInState() = runTest {
        val unknownId = "col_unknown"
        coEvery { collectionRepository.getCollectionById(unknownId) } returns flowOf(null)
        coEvery { bookmarkRepository.getBookmarksByCollection(unknownId) } returns flowOf(emptyList())

        val unknownViewModel = CollectionDetailViewModel(
            collectionId = unknownId,
            collectionRepository = collectionRepository,
            bookmarkRepository = bookmarkRepository
        )

        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            unknownViewModel.uiState.collect()
        }

        val state = unknownViewModel.uiState.value

        assertFalse(state.isLoading)
        assertNull(state.collection)
        assertEquals(0, state.bookmarks.size)

        collectJob.cancel()
    }

    @Test
    fun uiState_whenBookmarksEmpty_returnsEmptyBookmarksListInState() = runTest {
        coEvery { bookmarkRepository.getBookmarksByCollection(targetCollectionId) } returns flowOf(emptyList())

        val emptyBookmarksViewModel = CollectionDetailViewModel(
            collectionId = targetCollectionId,
            collectionRepository = collectionRepository,
            bookmarkRepository = bookmarkRepository
        )

        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            emptyBookmarksViewModel.uiState.collect()
        }

        val state = emptyBookmarksViewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(sampleCollection, state.collection)
        assertEquals(0, state.bookmarks.size)

        collectJob.cancel()
    }

    @Test
    fun onLongPressStart_setsActiveBookmarkAndPositions() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val bookmark = sampleBookmarks.first()
        viewModel.onLongPressStart(
            bookmark = bookmark,
            touchPosition = Offset(100f, 200f),
            cardOffset = Offset(50f, 150f),
            cardSize = IntSize(300, 200)
        )

        val state = viewModel.uiState.value
        assertEquals(bookmark, state.activeMenuBookmark)
        assertEquals(Offset(50f, 150f), state.activeCardOffset)
        assertEquals(IntSize(300, 200), state.activeCardSize)
        assertEquals(Offset(100f, 200f), state.touchPositionInWindow)
        assertNull(state.hoveredOption)

        collectJob.cancel()
    }

    @Test
    fun onLongPressDrag_updatesTouchPosition() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val bookmark = sampleBookmarks.first()
        viewModel.onLongPressStart(
            bookmark = bookmark,
            touchPosition = Offset(100f, 200f),
            cardOffset = Offset(50f, 150f),
            cardSize = IntSize(300, 200)
        )

        viewModel.onLongPressDrag(Offset(120f, 230f))

        val state = viewModel.uiState.value
        assertEquals(Offset(120f, 230f), state.touchPositionInWindow)

        collectJob.cancel()
    }

    @Test
    fun onHoveredOptionChange_updatesHoveredOption() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        viewModel.onHoveredOptionChange(BookmarkOption.PIN)
        assertEquals(BookmarkOption.PIN, viewModel.uiState.value.hoveredOption)

        viewModel.onHoveredOptionChange(BookmarkOption.DELETE)
        assertEquals(BookmarkOption.DELETE, viewModel.uiState.value.hoveredOption)

        viewModel.onHoveredOptionChange(null)
        assertNull(viewModel.uiState.value.hoveredOption)

        collectJob.cancel()
    }

    @Test
    fun dismissActionsMenu_resetsActiveOverlayState() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val bookmark = sampleBookmarks.first()
        viewModel.onLongPressStart(
            bookmark = bookmark,
            touchPosition = Offset(100f, 200f),
            cardOffset = Offset(50f, 150f),
            cardSize = IntSize(300, 200)
        )
        viewModel.onHoveredOptionChange(BookmarkOption.SHARE)

        viewModel.dismissActionsMenu()

        val state = viewModel.uiState.value
        assertNull(state.activeMenuBookmark)
        assertNull(state.activeCardOffset)
        assertNull(state.activeCardSize)
        assertNull(state.touchPositionInWindow)
        assertNull(state.hoveredOption)

        collectJob.cancel()
    }

    @Test
    fun onLongPressRelease_triggersActionCallbackAndResetsState() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val bookmark = sampleBookmarks.first()
        viewModel.onLongPressStart(
            bookmark = bookmark,
            touchPosition = Offset(100f, 200f),
            cardOffset = Offset(50f, 150f),
            cardSize = IntSize(300, 200)
        )
        viewModel.onHoveredOptionChange(BookmarkOption.PIN)

        var selectedBookmark: BookmarkEntity? = null
        var selectedOption: BookmarkOption? = null

        viewModel.onLongPressRelease { bm, opt ->
            selectedBookmark = bm
            selectedOption = opt
        }

        assertEquals(bookmark, selectedBookmark)
        assertEquals(BookmarkOption.PIN, selectedOption)
        assertNull(viewModel.uiState.value.activeMenuBookmark)

        collectJob.cancel()
    }

    @Test
    fun deleteDialog_openAndDismiss_updatesState() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val bookmark = sampleBookmarks.first()
        viewModel.openDeleteDialog(bookmark)

        assertEquals(bookmark, viewModel.uiState.value.bookmarkToDelete)

        viewModel.dismissDeleteDialog()
        assertNull(viewModel.uiState.value.bookmarkToDelete)

        collectJob.cancel()
    }

    @Test
    fun togglePin_callsRepository() = runTest {
        viewModel.togglePin("bm_1")
        coVerify(exactly = 1) { bookmarkRepository.togglePin("bm_1") }
    }

    @Test
    fun deleteBookmark_callsRepositoryAndDismissesDialog() = runTest {
        val bookmark = sampleBookmarks.first()
        viewModel.openDeleteDialog(bookmark)

        viewModel.deleteBookmark("bm_1")

        coVerify(exactly = 1) { bookmarkRepository.deleteBookmark("bm_1") }
        assertNull(viewModel.uiState.value.bookmarkToDelete)
    }
}
