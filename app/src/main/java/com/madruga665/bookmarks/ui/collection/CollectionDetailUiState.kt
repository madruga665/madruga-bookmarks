package com.madruga665.bookmarks.ui.collection

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.ui.components.BookmarkOption

data class CollectionDetailUiState(
    val isLoading: Boolean = true,
    val collection: CollectionEntity? = null,
    val bookmarks: List<BookmarkEntity> = emptyList(),
    val subcollections: List<CollectionEntity> = emptyList(),
    val error: String? = null,
    val activeMenuBookmark: BookmarkEntity? = null,
    val activeCardOffset: Offset? = null,
    val activeCardSize: IntSize? = null,
    val touchPositionInWindow: Offset? = null,
    val dragPositionInWindow: Offset? = null,
    val hoveredOption: BookmarkOption? = null,
    val bookmarkToDelete: BookmarkEntity? = null
) {
    val isMenuVisible: Boolean
        get() = activeMenuBookmark != null && activeCardOffset != null && activeCardSize != null
}
