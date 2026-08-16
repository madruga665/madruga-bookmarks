package com.madruga665.bookmarks.ui.bookmark

import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity

data class BookmarkDetailUiState(
    val bookmark: BookmarkEntity? = null,
    val collection: CollectionEntity? = null,
    val availableCollections: List<CollectionEntity> = emptyList(),
    val isLoading: Boolean = true,
    val isEditingTitle: Boolean = false,
    val editedTitle: String = "",
    val isEditingNotes: Boolean = false,
    val editedNotes: String = "",
    val isAddingTag: Boolean = false,
    val newTagInput: String = "",
    val isConfirmingDelete: Boolean = false,
    val isMoveSheetVisible: Boolean = false,
    val isDescriptionExpanded: Boolean = false,
    val userMessage: String? = null
)
