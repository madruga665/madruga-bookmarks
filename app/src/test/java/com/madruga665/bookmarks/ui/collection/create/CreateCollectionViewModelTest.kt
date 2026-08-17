package com.madruga665.bookmarks.ui.collection.create

import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.data.repository.CollectionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class CreateCollectionViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val collectionRepository: CollectionRepository = mockk(relaxed = true)
    private lateinit var viewModel: CreateCollectionViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CreateCollectionViewModel(collectionRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun defaultState_initializesWithExpectedDefaults() {
        val state = viewModel.uiState.value
        assertEquals("", state.nameInput)
        assertEquals("#FFE600", state.selectedColor)
        assertEquals("folder", state.selectedIconKey)
        assertFalse(state.isSubmitting)
        assertNull(state.errorMessage)
        assertFalse(state.isSuccess)
        assertEquals(0, state.characterCount)
        assertFalse(state.isSubmitEnabled)
    }

    @Test
    fun onNameChange_updatesNameInputAndEnablesSubmit() {
        viewModel.onNameChange("Android Dev")
        val state = viewModel.uiState.value
        assertEquals("Android Dev", state.nameInput)
        assertEquals(11, state.characterCount)
        assertTrue(state.isSubmitEnabled)
        assertNull(state.errorMessage)
    }

    @Test
    fun onNameChange_capsAt40Characters() {
        val longName = "A".repeat(50)
        viewModel.onNameChange(longName)
        val state = viewModel.uiState.value
        assertEquals(40, state.nameInput.length)
        assertEquals("A".repeat(40), state.nameInput)
        assertEquals(40, state.characterCount)
        assertTrue(state.isSubmitEnabled)
    }

    @Test
    fun onNameChange_clearsPreviousErrorMessage() {
        viewModel.createCollection()
        assertEquals("Collection name cannot be empty", viewModel.uiState.value.errorMessage)

        viewModel.onNameChange("Kotlin")
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun onColorSelect_updatesSelectedColor() {
        viewModel.onColorSelect("#FF4B8B")
        assertEquals("#FF4B8B", viewModel.uiState.value.selectedColor)
    }

    @Test
    fun onIconSelect_updatesSelectedIconKey() {
        viewModel.onIconSelect("code")
        assertEquals("code", viewModel.uiState.value.selectedIconKey)
    }

    @Test
    fun createCollection_withBlankName_setsErrorMessageAndDoesNotCallRepository() {
        viewModel.onNameChange("   ")
        viewModel.createCollection()

        val state = viewModel.uiState.value
        assertEquals("Collection name cannot be empty", state.errorMessage)
        assertFalse(state.isSubmitting)
        assertFalse(state.isSuccess)

        coVerify(exactly = 0) { collectionRepository.createCollection(any(), any(), any()) }
    }

    @Test
    fun createCollection_successful_callsRepositoryAndInvokesOnSuccess() {
        val createdEntity = CollectionEntity(
            id = "col_new_123",
            name = "Compose UI",
            linkCount = 0,
            subcollectionCount = 0,
            parentId = null,
            iconKey = "palette",
            colorAccent = "#9B51E0",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        coEvery {
            collectionRepository.createCollection("Compose UI", "#9B51E0", "palette")
        } returns createdEntity

        viewModel.onNameChange("Compose UI")
        viewModel.onColorSelect("#9B51E0")
        viewModel.onIconSelect("palette")

        var callbackInvokedWith: CollectionEntity? = null
        viewModel.createCollection {
            callbackInvokedWith = it
        }

        val state = viewModel.uiState.value
        assertTrue(state.isSuccess)
        assertFalse(state.isSubmitting)
        assertNull(state.errorMessage)
        assertEquals(createdEntity, callbackInvokedWith)

        coVerify(exactly = 1) {
            collectionRepository.createCollection("Compose UI", "#9B51E0", "palette")
        }
    }

    @Test
    fun createCollection_repositoryReturnsNull_setsFailedErrorMessage() {
        coEvery {
            collectionRepository.createCollection("Failing Col", "#FFE600", "folder")
        } returns null

        viewModel.onNameChange("Failing Col")

        var callbackCalled = false
        viewModel.createCollection {
            callbackCalled = true
        }

        val state = viewModel.uiState.value
        assertFalse(state.isSuccess)
        assertFalse(state.isSubmitting)
        assertEquals("Failed to create collection", state.errorMessage)
        assertFalse(callbackCalled)
    }

    @Test
    fun createCollection_repositoryThrowsException_setsFailedErrorMessage() {
        coEvery {
            collectionRepository.createCollection(any(), any(), any())
        } throws RuntimeException("DB error")

        viewModel.onNameChange("Error Col")

        var callbackCalled = false
        viewModel.createCollection {
            callbackCalled = true
        }

        val state = viewModel.uiState.value
        assertFalse(state.isSuccess)
        assertFalse(state.isSubmitting)
        assertEquals("Failed to create collection", state.errorMessage)
        assertFalse(callbackCalled)
    }

    @Test
    fun resetState_resetsUiStateToDefault() {
        viewModel.onNameChange("Temporary")
        viewModel.onColorSelect("#00C49F")
        viewModel.onIconSelect("book")

        viewModel.resetState()

        val state = viewModel.uiState.value
        assertEquals("", state.nameInput)
        assertEquals("#FFE600", state.selectedColor)
        assertEquals("folder", state.selectedIconKey)
        assertFalse(state.isSubmitting)
        assertNull(state.errorMessage)
        assertFalse(state.isSuccess)
    }
}
