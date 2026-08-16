package com.madruga665.bookmarks.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class CollectionDetailViewModel(
    private val collectionId: String,
    private val collectionRepository: CollectionRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    val uiState: StateFlow<CollectionDetailUiState> = combine(
        collectionRepository.getCollectionById(collectionId),
        bookmarkRepository.getBookmarksByCollection(collectionId)
    ) { collection, bookmarks ->
        CollectionDetailUiState(
            isLoading = false,
            collection = collection,
            bookmarks = bookmarks,
            subcollections = emptyList(),
            error = null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = CollectionDetailUiState(isLoading = true)
    )
}
