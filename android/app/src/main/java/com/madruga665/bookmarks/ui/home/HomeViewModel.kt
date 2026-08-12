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
                val input = if (current is HomeScreenUiState.Success) current.quickSaveUrlInput else ""
                _uiState.value = HomeScreenUiState.Success(
                    collections = list,
                    quickSaveUrlInput = input
                )
            }
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
