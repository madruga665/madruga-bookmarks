package com.madruga665.bookmarks.ui.home

import com.madruga665.bookmarks.data.local.CollectionEntity

sealed interface HomeScreenUiState {
    object Loading : HomeScreenUiState

    data class Success(
        val collections: List<CollectionEntity>,
        val quickSaveUrlInput: String = "",
        val inputError: String? = null,
        val isSaving: Boolean = false
    ) : HomeScreenUiState

    data class Error(val message: String) : HomeScreenUiState
}
