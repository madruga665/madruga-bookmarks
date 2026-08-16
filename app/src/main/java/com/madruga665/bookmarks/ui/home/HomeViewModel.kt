package com.madruga665.bookmarks.ui.home

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
class HomeViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        loadCollections()
    }

    fun loadCollections() {
        viewModelScope.launch {
            collectionRepository.collections.collectLatest { list ->
                val current = _uiState.value
                if (current is HomeScreenUiState.Success) {
                    _uiState.value = current.copy(collections = list)
                } else {
                    _uiState.value = HomeScreenUiState.Success(collections = list)
                }
            }
        }
    }

    fun openActionsMenu(collection: com.madruga665.bookmarks.data.local.CollectionEntity) {
        val current = _uiState.value as? HomeScreenUiState.Success ?: return
        _uiState.value = current.copy(activeMenuCollection = collection)
    }

    fun dismissActionsMenu() {
        val current = _uiState.value as? HomeScreenUiState.Success ?: return
        _uiState.value = current.copy(
            activeMenuCollection = null,
            activeCardOffset = null,
            activeCardSize = null,
            touchPositionInWindow = null,
            hoveredOption = null
        )
    }

    fun onLongPressStart(
        collection: com.madruga665.bookmarks.data.local.CollectionEntity,
        touchInWindow: androidx.compose.ui.geometry.Offset,
        cardOffset: androidx.compose.ui.geometry.Offset,
        cardSize: androidx.compose.ui.unit.IntSize
    ) {
        val current = _uiState.value as? HomeScreenUiState.Success ?: return
        _uiState.value = current.copy(
            activeMenuCollection = collection,
            activeCardOffset = cardOffset,
            activeCardSize = cardSize,
            touchPositionInWindow = touchInWindow,
            hoveredOption = null
        )
    }

    fun onLongPressDrag(touchInWindow: androidx.compose.ui.geometry.Offset) {
        val current = _uiState.value as? HomeScreenUiState.Success ?: return
        if (current.activeMenuCollection != null) {
            _uiState.value = current.copy(touchPositionInWindow = touchInWindow)
        }
    }

    fun onHoveredOptionChange(option: com.madruga665.bookmarks.ui.components.CollectionOption?) {
        val current = _uiState.value as? HomeScreenUiState.Success ?: return
        if (current.hoveredOption != option) {
            _uiState.value = current.copy(hoveredOption = option)
        }
    }

    fun onLongPressRelease(onShareRequested: (com.madruga665.bookmarks.data.local.CollectionEntity) -> Unit) {
        val current = _uiState.value as? HomeScreenUiState.Success ?: return
        val targetCollection = current.activeMenuCollection ?: return
        val selectedOption = current.hoveredOption

        _uiState.value = current.copy(
            activeMenuCollection = null,
            activeCardOffset = null,
            activeCardSize = null,
            touchPositionInWindow = null,
            hoveredOption = null
        )

        when (selectedOption) {
            com.madruga665.bookmarks.ui.components.CollectionOption.EDIT -> openEditDialog(targetCollection)
            com.madruga665.bookmarks.ui.components.CollectionOption.SHARE -> onShareRequested(targetCollection)
            com.madruga665.bookmarks.ui.components.CollectionOption.DELETE -> openDeleteDialog(targetCollection)
            null -> { /* Simply close menu */ }
        }
    }

    fun openEditDialog(collection: com.madruga665.bookmarks.data.local.CollectionEntity) {
        val current = _uiState.value as? HomeScreenUiState.Success ?: return
        _uiState.value = current.copy(
            activeMenuCollection = null,
            collectionToEdit = collection
        )
    }

    fun dismissEditDialog() {
        val current = _uiState.value as? HomeScreenUiState.Success ?: return
        _uiState.value = current.copy(collectionToEdit = null)
    }

    fun updateCollection(id: String, name: String, colorAccent: String, iconKey: String) {
        val current = _uiState.value as? HomeScreenUiState.Success ?: return
        viewModelScope.launch {
            collectionRepository.updateCollection(id, name, colorAccent, iconKey)
            _uiState.value = current.copy(collectionToEdit = null)
        }
    }

    fun openDeleteDialog(collection: com.madruga665.bookmarks.data.local.CollectionEntity) {
        val current = _uiState.value as? HomeScreenUiState.Success ?: return
        _uiState.value = current.copy(
            activeMenuCollection = null,
            collectionToDelete = collection
        )
    }

    fun dismissDeleteDialog() {
        val current = _uiState.value as? HomeScreenUiState.Success ?: return
        _uiState.value = current.copy(collectionToDelete = null)
    }

    fun deleteCollection(collectionId: String) {
        val current = _uiState.value as? HomeScreenUiState.Success ?: return
        viewModelScope.launch {
            collectionRepository.deleteCollection(collectionId)
            _uiState.value = current.copy(collectionToDelete = null)
        }
    }

    fun shareCollection(context: android.content.Context, collection: com.madruga665.bookmarks.data.local.CollectionEntity) {
        dismissActionsMenu()
        viewModelScope.launch {
            val bookmarks = bookmarkRepository.getBookmarksForCollection(collection.id)
            com.madruga665.bookmarks.ui.utils.ShareUtils.shareCollection(context, collection, bookmarks)
        }
    }

    fun onUrlInputChange(newInput: String) {
        val current = _uiState.value
        if (current is HomeScreenUiState.Success) {
            _uiState.value = current.copy(
                quickSaveUrlInput = newInput,
                inputError = null
            )
        }
    }

    fun onPasteFromClipboard(clipboardText: String) {
        if (clipboardText.isNotBlank()) {
            onUrlInputChange(clipboardText.trim())
        }
    }

    fun onQuickSaveSubmit() {
        val current = _uiState.value as? HomeScreenUiState.Success ?: return
        val url = current.quickSaveUrlInput.trim()

        if (url.isBlank() || (!url.startsWith("http://") && !url.startsWith("https://"))) {
            _uiState.value = current.copy(inputError = "Please enter a valid web URL (http:// or https://)")
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isSaving = true)
            val success = bookmarkRepository.quickSaveBookmark(url)
            if (success) {
                _uiState.value = current.copy(
                    quickSaveUrlInput = "",
                    inputError = null,
                    isSaving = false
                )
            } else {
                _uiState.value = current.copy(
                    inputError = "Failed to save bookmark",
                    isSaving = false
                )
            }
        }
    }
}
