package com.madruga665.bookmarks.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.madruga665.bookmarks.data.local.BookmarkEntity

enum class BookmarkOption {
    OPEN,
    PIN,
    SHARE,
    DELETE
}

data class BookmarkActionsOverlayState(
    val activeBookmark: BookmarkEntity? = null,
    val cardOffset: Offset? = null,
    val cardSize: IntSize? = null,
    val touchPositionInWindow: Offset? = null,
    val hoveredOption: BookmarkOption? = null,
    val bookmarkToDelete: BookmarkEntity? = null
) {
    val isMenuVisible: Boolean
        get() = activeBookmark != null && cardOffset != null && cardSize != null
}
