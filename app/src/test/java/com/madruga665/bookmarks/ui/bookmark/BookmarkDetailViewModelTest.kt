package com.madruga665.bookmarks.ui.bookmark

import androidx.lifecycle.SavedStateHandle
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookmarkDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val bookmarkRepository: BookmarkRepository = mockk(relaxed = true)
    private val collectionRepository: CollectionRepository = mockk(relaxed = true)

    private val targetBookmarkId = "bm_123"
    private val sampleCollection = CollectionEntity(
        id = "col_ia",
        name = "IA",
        linkCount = 1,
        subcollectionCount = 0,
        parentId = null,
        iconKey = "code",
        colorAccent = "YELLOW",
        createdAt = 1000L,
        updatedAt = 1000L
    )

    private val sampleBookmark = BookmarkEntity(
        id = targetBookmarkId,
        url = "https://openai.com",
        title = "OpenAI",
        description = "OpenAI research and platform overview",
        faviconUrl = "https://openai.com/favicon.ico",
        thumbnailUrl = "https://openai.com/hero.png",
        sourcePlatform = "Web",
        collectionId = "col_ia",
        notes = "Important AI platform",
        tags = "AI,Tech",
        isPinned = false,
        createdAt = 1000L,
        updatedAt = 1000L,
        syncStatus = "SYNCED"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { bookmarkRepository.getBookmarkById(targetBookmarkId) } returns flowOf(sampleBookmark)
        coEvery { collectionRepository.collections } returns flowOf(listOf(sampleCollection))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(bookmarkId: String = targetBookmarkId): BookmarkDetailViewModel {
        return BookmarkDetailViewModel(
            bookmarkRepository = bookmarkRepository,
            collectionRepository = collectionRepository,
            savedStateHandle = SavedStateHandle(mapOf("bookmarkId" to bookmarkId))
        )
    }

    @Test
    fun uiState_loadsBookmarkAndCollection_updatesStateCorrectly() {
        val viewModel = createViewModel()
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(sampleBookmark, state.bookmark)
        assertEquals(sampleCollection, state.collection)
        assertEquals(1, state.availableCollections.size)
        assertEquals("OpenAI", state.editedTitle)
        assertEquals("Important AI platform", state.editedNotes)
    }

    @Test
    fun uiState_blankBookmarkId_setsError() {
        val viewModel = createViewModel(bookmarkId = "")
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals("Invalid bookmark ID", state.userMessage)
    }

    @Test
    fun onTitleEditing_flowWorksCorrectly() {
        val viewModel = createViewModel()

        viewModel.onStartEditingTitle()
        assertTrue(viewModel.uiState.value.isEditingTitle)
        assertEquals("OpenAI", viewModel.uiState.value.editedTitle)

        viewModel.onTitleChange("OpenAI ChatGPT")
        assertEquals("OpenAI ChatGPT", viewModel.uiState.value.editedTitle)

        viewModel.onSaveTitle()
        assertFalse(viewModel.uiState.value.isEditingTitle)
        coVerify { bookmarkRepository.updateTitle(targetBookmarkId, "OpenAI ChatGPT") }

        viewModel.onStartEditingTitle()
        viewModel.onTitleChange("Temporary")
        viewModel.onCancelEditingTitle()
        assertFalse(viewModel.uiState.value.isEditingTitle)
        assertEquals("OpenAI", viewModel.uiState.value.editedTitle)
    }

    @Test
    fun onSaveTitle_blankTitle_setsUserMessage() {
        val viewModel = createViewModel()
        viewModel.onStartEditingTitle()
        viewModel.onTitleChange("   ")
        viewModel.onSaveTitle()

        assertEquals("Title cannot be empty", viewModel.uiState.value.userMessage)
        assertTrue(viewModel.uiState.value.isEditingTitle)
    }

    @Test
    fun onTogglePin_callsRepositoryTogglePin() {
        val viewModel = createViewModel()
        viewModel.onTogglePin()
        coVerify { bookmarkRepository.togglePin(targetBookmarkId) }
    }

    @Test
    fun onToggleDescriptionExpanded_togglesState() {
        val viewModel = createViewModel()
        assertFalse(viewModel.uiState.value.isDescriptionExpanded)

        viewModel.onToggleDescriptionExpanded()
        assertTrue(viewModel.uiState.value.isDescriptionExpanded)

        viewModel.onToggleDescriptionExpanded()
        assertFalse(viewModel.uiState.value.isDescriptionExpanded)
    }

    @Test
    fun onNotesEditing_worksCorrectly() {
        val viewModel = createViewModel()

        viewModel.onStartEditingNotes()
        assertTrue(viewModel.uiState.value.isEditingNotes)
        assertEquals("Important AI platform", viewModel.uiState.value.editedNotes)

        viewModel.onNotesChange("Updated notes here")
        assertEquals("Updated notes here", viewModel.uiState.value.editedNotes)

        viewModel.onSaveNotes()
        assertFalse(viewModel.uiState.value.isEditingNotes)
        coVerify { bookmarkRepository.updateNotes(targetBookmarkId, "Updated notes here") }

        viewModel.onStartEditingNotes()
        viewModel.onNotesChange("Discard me")
        viewModel.onCancelEditingNotes()
        assertFalse(viewModel.uiState.value.isEditingNotes)
    }

    @Test
    fun onTagOperations_addAndRemove_callsRepository() {
        val viewModel = createViewModel()

        viewModel.onOpenAddTagDialog()
        assertTrue(viewModel.uiState.value.isAddingTag)
        assertEquals("", viewModel.uiState.value.newTagInput)

        viewModel.onNewTagInputChange("Kotlin")
        assertEquals("Kotlin", viewModel.uiState.value.newTagInput)

        viewModel.onSaveNewTag()
        assertFalse(viewModel.uiState.value.isAddingTag)
        coVerify { bookmarkRepository.addTag(targetBookmarkId, "kotlin") }

        viewModel.onRemoveTag("AI")
        coVerify { bookmarkRepository.removeTag(targetBookmarkId, "AI") }
    }

    @Test
    fun onSaveNewTag_atMaxTags_rejectsNewTag() {
        val bookmarkWithMaxTags = sampleBookmark.copy(tags = "t1,t2,t3,t4,t5,t6,t7,t8,t9,t10")
        coEvery { bookmarkRepository.getBookmarkById(targetBookmarkId) } returns flowOf(bookmarkWithMaxTags)

        val viewModel = createViewModel()
        viewModel.onOpenAddTagDialog()
        viewModel.onNewTagInputChange("newtag")
        viewModel.onSaveNewTag()

        assertEquals("Maximum of 10 tags reached", viewModel.uiState.value.userMessage)
        coVerify(exactly = 0) { bookmarkRepository.addTag(any(), any()) }
    }

    @Test
    fun onSaveNewTag_blankTag_doesNotCallRepository() {
        val viewModel = createViewModel()
        viewModel.onOpenAddTagDialog()
        viewModel.onNewTagInputChange("   ")
        viewModel.onSaveNewTag()

        assertFalse(viewModel.uiState.value.isAddingTag)
        coVerify(exactly = 0) { bookmarkRepository.addTag(any(), any()) }
    }

    @Test
    fun onMoveCollectionOperations_worksCorrectly() {
        val viewModel = createViewModel()

        viewModel.onOpenMoveCollectionSheet()
        assertTrue(viewModel.uiState.value.isMoveSheetVisible)

        viewModel.onDismissMoveCollectionSheet()
        assertFalse(viewModel.uiState.value.isMoveSheetVisible)

        viewModel.onSelectCollection("col_other")
        coVerify { bookmarkRepository.moveToCollection(targetBookmarkId, "col_other") }
        assertFalse(viewModel.uiState.value.isMoveSheetVisible)
    }

    @Test
    fun onDeleteOperations_worksCorrectly() {
        val viewModel = createViewModel()

        viewModel.onOpenDeleteDialog()
        assertTrue(viewModel.uiState.value.isConfirmingDelete)

        viewModel.onDismissDeleteDialog()
        assertFalse(viewModel.uiState.value.isConfirmingDelete)

        var deletedCallbackCalled = false
        viewModel.onConfirmDelete { deletedCallbackCalled = true }

        coVerify { bookmarkRepository.deleteBookmark(targetBookmarkId) }
        assertFalse(viewModel.uiState.value.isConfirmingDelete)
        assertTrue(deletedCallbackCalled)
    }

    @Test
    fun onRefreshMetadata_success_setsSuccessMessage() {
        coEvery { bookmarkRepository.refreshMetadata(targetBookmarkId) } returns true
        val viewModel = createViewModel()

        viewModel.onRefreshMetadata()
        assertEquals("Metadata updated!", viewModel.uiState.value.userMessage)

        viewModel.clearUserMessage()
        assertNull(viewModel.uiState.value.userMessage)
    }

    @Test
    fun onRefreshMetadata_failure_setsFailureMessage() {
        coEvery { bookmarkRepository.refreshMetadata(targetBookmarkId) } returns false
        val viewModel = createViewModel()

        viewModel.onRefreshMetadata()
        assertEquals("Failed to refresh metadata", viewModel.uiState.value.userMessage)
    }
}
