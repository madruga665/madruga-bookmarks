package com.madruga665.bookmarks.ui.collection

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import com.madruga665.bookmarks.ui.components.BookmarkActionsOverlayState
import com.madruga665.bookmarks.ui.components.BookmarkOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionDetailViewModel(
    private val collectionId: String,
    private val collectionRepository: CollectionRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    private val _overlayState = MutableStateFlow(BookmarkActionsOverlayState())

    val uiState: StateFlow<CollectionDetailUiState> = combine(
        collectionRepository.getCollectionById(collectionId),
        bookmarkRepository.getBookmarksByCollection(collectionId),
        _overlayState
    ) { collection, bookmarks, overlay ->
        CollectionDetailUiState(
            isLoading = false,
            collection = collection,
            bookmarks = bookmarks,
            subcollections = emptyList(),
            error = null,
            activeMenuBookmark = overlay.activeBookmark,
            activeCardOffset = overlay.cardOffset,
            activeCardSize = overlay.cardSize,
            touchPositionInWindow = overlay.touchPositionInWindow,
            hoveredOption = overlay.hoveredOption,
            bookmarkToDelete = overlay.bookmarkToDelete
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CollectionDetailUiState(isLoading = true)
    )

    fun onLongPressStart(
        bookmark: BookmarkEntity,
        touchPosition: Offset,
        cardOffset: Offset,
        cardSize: IntSize
    ) {
        _overlayState.update { current ->
            current.copy(
                activeBookmark = bookmark,
                cardOffset = cardOffset,
                cardSize = cardSize,
                touchPositionInWindow = touchPosition,
                hoveredOption = null
            )
        }
    }

    fun onLongPressDrag(touchPosition: Offset) {
        _overlayState.update { current ->
            if (current.activeBookmark != null) {
                current.copy(touchPositionInWindow = touchPosition)
            } else {
                current
            }
        }
    }

    fun onHoveredOptionChange(option: BookmarkOption?) {
        _overlayState.update { current ->
            if (current.hoveredOption != option) {
                current.copy(hoveredOption = option)
            } else {
                current
            }
        }
    }

    fun dismissActionsMenu() {
        _overlayState.update { current ->
            current.copy(
                activeBookmark = null,
                cardOffset = null,
                cardSize = null,
                touchPositionInWindow = null,
                hoveredOption = null
            )
        }
    }

    fun onLongPressRelease(onActionSelected: (BookmarkEntity, BookmarkOption) -> Unit = { _, _ -> }) {
        val current = _overlayState.value
        val bookmark = current.activeBookmark
        val option = current.hoveredOption

        _overlayState.update {
            it.copy(
                activeBookmark = null,
                cardOffset = null,
                cardSize = null,
                touchPositionInWindow = null,
                hoveredOption = null
            )
        }

        if (bookmark != null && option != null) {
            onActionSelected(bookmark, option)
        }
    }

    fun openDeleteDialog(bookmark: BookmarkEntity) {
        _overlayState.update { current ->
            current.copy(
                activeBookmark = null,
                cardOffset = null,
                cardSize = null,
                touchPositionInWindow = null,
                hoveredOption = null,
                bookmarkToDelete = bookmark
            )
        }
    }

    fun dismissDeleteDialog() {
        _overlayState.update { current ->
            current.copy(bookmarkToDelete = null)
        }
    }

    fun togglePin(bookmarkId: String) {
        viewModelScope.launch {
            bookmarkRepository.togglePin(bookmarkId)
        }
    }

    fun deleteBookmark(bookmarkId: String) {
        viewModelScope.launch {
            bookmarkRepository.deleteBookmark(bookmarkId)
            dismissDeleteDialog()
        }
    }
}
