package com.madruga665.bookmarks.ui.savemodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveBookmarkViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaveBookmarkModalUiState())
    val uiState: StateFlow<SaveBookmarkModalUiState> = _uiState.asStateFlow()

    init {
        loadCollections()
    }

    private fun loadCollections() {
        viewModelScope.launch {
            collectionRepository.collections.collectLatest { collections ->
                _uiState.value = _uiState.value.copy(
                    availableCollections = collections
                )
            }
        }
    }

    fun openSaveModal(targetUrl: String) {
        if (targetUrl.isNotBlank()) {
            val collections = _uiState.value.availableCollections
            _uiState.value = _uiState.value.copy(
                isVisible = true,
                targetUrl = targetUrl.trim(),
                selectedCollectionId = collections.firstOrNull()?.id ?: "col_unsorted",
                isPinned = false,
                isCreatingFolder = false,
                newFolderNameInput = "",
                newFolderColorAccent = "YELLOW",
                folderInputError = null,
                error = null,
                isSaving = false
            )
        }
    }

    fun dismissModal() {
        _uiState.value = _uiState.value.copy(
            isVisible = false
        )
    }

    fun onSelectCollection(collectionId: String) {
        _uiState.value = _uiState.value.copy(
            selectedCollectionId = collectionId
        )
    }

    fun onTogglePin() {
        _uiState.value = _uiState.value.copy(
            isPinned = !_uiState.value.isPinned
        )
    }

    fun onToggleCreateFolder() {
        _uiState.value = _uiState.value.copy(
            isCreatingFolder = !_uiState.value.isCreatingFolder,
            newFolderNameInput = "",
            newFolderColorAccent = "YELLOW",
            folderInputError = null
        )
    }

    fun onNewFolderNameChange(input: String) {
        _uiState.value = _uiState.value.copy(
            newFolderNameInput = input,
            folderInputError = null
        )
    }

    fun onNewFolderColorSelect(color: String) {
        _uiState.value = _uiState.value.copy(
            newFolderColorAccent = color
        )
    }

    fun onCreateFolderSubmit() {
        val name = _uiState.value.newFolderNameInput.trim()
        if (name.isBlank()) {
            _uiState.value = _uiState.value.copy(
                folderInputError = "Folder name cannot be empty"
            )
            return
        }

        viewModelScope.launch {
            val created = collectionRepository.createCollection(
                name = name,
                colorAccent = _uiState.value.newFolderColorAccent
            )
            if (created != null) {
                _uiState.value = _uiState.value.copy(
                    selectedCollectionId = created.id,
                    isCreatingFolder = false,
                    newFolderNameInput = "",
                    folderInputError = null
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    folderInputError = "Failed to create folder"
                )
            }
        }
    }

    fun onConfirmSave(onSuccess: () -> Unit) {
        val current = _uiState.value
        val url = current.targetUrl
        if (url.isBlank()) return

        viewModelScope.launch {
            _uiState.value = current.copy(isSaving = true)
            val success = bookmarkRepository.quickSaveBookmark(
                url = url,
                collectionId = current.selectedCollectionId,
                isPinned = current.isPinned
            )
            if (success) {
                _uiState.value = current.copy(
                    isSaving = false,
                    isVisible = false
                )
                onSuccess()
            } else {
                _uiState.value = current.copy(
                    isSaving = false,
                    error = "Failed to save bookmark"
                )
            }
        }
    }
}
