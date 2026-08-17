package com.madruga665.bookmarks.ui.bookmark

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkDetailViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val collectionRepository: CollectionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val bookmarkId: String = savedStateHandle["bookmarkId"] ?: ""

    private val _uiState = MutableStateFlow(BookmarkDetailUiState())
    val uiState: StateFlow<BookmarkDetailUiState> = _uiState.asStateFlow()

    init {
        loadBookmarkDetails()
    }

    private fun loadBookmarkDetails() {
        if (bookmarkId.isBlank()) {
            _uiState.update { it.copy(isLoading = false, userMessage = "Invalid bookmark ID") }
            return
        }

        viewModelScope.launch {
            combine(
                bookmarkRepository.getBookmarkById(bookmarkId),
                collectionRepository.collections
            ) { bookmark, collections ->
                Pair(bookmark, collections)
            }.collectLatest { (bookmark, collections) ->
                val col = collections.find { it.id == bookmark?.collectionId }
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        bookmark = bookmark,
                        collection = col,
                        availableCollections = collections,
                        editedTitle = if (current.isEditingTitle) current.editedTitle else (bookmark?.title ?: ""),
                        editedNotes = if (current.isEditingNotes) current.editedNotes else (bookmark?.notes ?: "")
                    )
                }
            }
        }
    }

    fun onStartEditingTitle() {
        _uiState.update {
            it.copy(
                isEditingTitle = true,
                editedTitle = it.bookmark?.title ?: ""
            )
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(editedTitle = newTitle) }
    }

    fun onSaveTitle() {
        val newTitle = _uiState.value.editedTitle.trim()
        if (newTitle.isNotBlank()) {
            viewModelScope.launch {
                bookmarkRepository.updateTitle(bookmarkId, newTitle)
                _uiState.update { it.copy(isEditingTitle = false) }
            }
        } else {
            _uiState.update { it.copy(userMessage = "Title cannot be empty") }
        }
    }

    fun onCancelEditingTitle() {
        _uiState.update {
            it.copy(
                isEditingTitle = false,
                editedTitle = it.bookmark?.title ?: ""
            )
        }
    }

    fun onTogglePin() {
        viewModelScope.launch {
            bookmarkRepository.togglePin(bookmarkId)
        }
    }

    fun onToggleDescriptionExpanded() {
        _uiState.update { it.copy(isDescriptionExpanded = !it.isDescriptionExpanded) }
    }

    fun onStartEditingNotes() {
        _uiState.update {
            it.copy(
                isEditingNotes = true,
                editedNotes = it.bookmark?.notes ?: ""
            )
        }
    }

    fun onNotesChange(newNotes: String) {
        _uiState.update { it.copy(editedNotes = newNotes) }
    }

    fun onSaveNotes() {
        val notes = _uiState.value.editedNotes.trim()
        viewModelScope.launch {
            bookmarkRepository.updateNotes(bookmarkId, notes.ifEmpty { null })
            _uiState.update { it.copy(isEditingNotes = false) }
        }
    }

    fun onCancelEditingNotes() {
        _uiState.update {
            it.copy(
                isEditingNotes = false,
                editedNotes = it.bookmark?.notes ?: ""
            )
        }
    }

    fun onOpenAddTagDialog() {
        _uiState.update { it.copy(isAddingTag = true, newTagInput = "") }
    }

    fun onNewTagInputChange(text: String) {
        _uiState.update { it.copy(newTagInput = text.take(25)) }
    }

    fun onSaveNewTag() {
        val tag = _uiState.value.newTagInput.trim().removePrefix("#").lowercase()
        if (tag.isBlank()) {
            _uiState.update { it.copy(isAddingTag = false, newTagInput = "") }
            return
        }

        val currentBookmark = _uiState.value.bookmark
        val currentTags = currentBookmark?.tags?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList()

        if (currentTags.size >= 10) {
            _uiState.update { it.copy(userMessage = "Maximum of 10 tags reached") }
            return
        }

        if (currentTags.any { it.equals(tag, ignoreCase = true) }) {
            _uiState.update { it.copy(isAddingTag = false, newTagInput = "") }
            return
        }

        viewModelScope.launch {
            bookmarkRepository.addTag(bookmarkId, tag)
            _uiState.update { it.copy(isAddingTag = false, newTagInput = "") }
        }
    }

    fun onDismissAddTagDialog() {
        _uiState.update { it.copy(isAddingTag = false, newTagInput = "") }
    }

    fun onRemoveTag(tag: String) {
        viewModelScope.launch {
            bookmarkRepository.removeTag(bookmarkId, tag)
        }
    }

    fun onOpenMoveCollectionSheet() {
        _uiState.update { it.copy(isMoveSheetVisible = true) }
    }

    fun onDismissMoveCollectionSheet() {
        _uiState.update { it.copy(isMoveSheetVisible = false) }
    }

    fun onSelectCollection(collectionId: String) {
        viewModelScope.launch {
            bookmarkRepository.moveToCollection(bookmarkId, collectionId)
            _uiState.update { it.copy(isMoveSheetVisible = false) }
        }
    }

    fun onOpenDeleteDialog() {
        _uiState.update { it.copy(isConfirmingDelete = true) }
    }

    fun onDismissDeleteDialog() {
        _uiState.update { it.copy(isConfirmingDelete = false) }
    }

    fun onConfirmDelete(onDeleted: () -> Unit) {
        viewModelScope.launch {
            bookmarkRepository.deleteBookmark(bookmarkId)
            _uiState.update { it.copy(isConfirmingDelete = false) }
            onDeleted()
        }
    }

    fun onRefreshMetadata() {
        viewModelScope.launch {
            val success = bookmarkRepository.refreshMetadata(bookmarkId)
            if (!success) {
                _uiState.update { it.copy(userMessage = "Failed to refresh metadata") }
            } else {
                _uiState.update { it.copy(userMessage = "Metadata updated!") }
            }
        }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
