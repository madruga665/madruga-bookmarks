package com.madruga665.bookmarks.ui.collection.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.data.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCollectionViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateCollectionUiState())
    val uiState: StateFlow<CreateCollectionUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                nameInput = name.take(40),
                errorMessage = null
            )
        }
    }

    fun onColorSelect(hexColor: String) {
        _uiState.update {
            it.copy(selectedColor = hexColor)
        }
    }

    fun onIconSelect(iconKey: String) {
        _uiState.update {
            it.copy(selectedIconKey = iconKey)
        }
    }

    fun createCollection(onSuccess: (CollectionEntity) -> Unit = {}) {
        if (_uiState.value.nameInput.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Collection name cannot be empty")
            }
            return
        }

        _uiState.update {
            it.copy(isSubmitting = true, errorMessage = null)
        }

        viewModelScope.launch {
            try {
                val currentState = _uiState.value
                val result = collectionRepository.createCollection(
                    name = currentState.nameInput,
                    colorAccent = currentState.selectedColor,
                    iconKey = currentState.selectedIconKey
                )
                if (result != null) {
                    _uiState.update {
                        it.copy(
                            isSuccess = true,
                            isSubmitting = false
                        )
                    }
                    onSuccess(result)
                } else {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = "Failed to create collection"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = "Failed to create collection"
                    )
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = CreateCollectionUiState()
    }
}
