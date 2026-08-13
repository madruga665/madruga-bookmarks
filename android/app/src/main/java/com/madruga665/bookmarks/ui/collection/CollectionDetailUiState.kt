package com.madruga665.bookmarks.ui.collection

import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity

data class CollectionDetailUiState(
    val isLoading: Boolean = true,
    val collection: CollectionEntity? = null,
    val bookmarks: List<BookmarkEntity> = emptyList(),
    val subcollections: List<CollectionEntity> = emptyList(),
    val error: String? = null
)
