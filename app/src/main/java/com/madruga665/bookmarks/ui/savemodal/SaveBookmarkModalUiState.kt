package com.madruga665.bookmarks.ui.savemodal

import com.madruga665.bookmarks.data.local.CollectionEntity

data class SaveBookmarkModalUiState(
    val isVisible: Boolean = false,
    val targetUrl: String = "",
    val availableCollections: List<CollectionEntity> = emptyList(),
    val selectedCollectionId: String = "col_unsorted",
    val isPinned: Boolean = false,
    val isCreatingFolder: Boolean = false,
    val newFolderNameInput: String = "",
    val newFolderColorAccent: String = "YELLOW",
    val folderInputError: String? = null,
    val isSaving: Boolean = false,
    val error: String? = null,
    val tags: List<String> = emptyList(),
    val tagInput: String = "",
    val existingTags: List<String> = emptyList()
) {
    val selectedCollection: CollectionEntity?
        get() = availableCollections.find { it.id == selectedCollectionId }

    val saveButtonText: String
        get() = "Save to \"${selectedCollection?.name ?: "Unsorted"}\""
}
