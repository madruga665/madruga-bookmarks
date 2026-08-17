package com.madruga665.bookmarks.ui.collection.create

data class CreateCollectionUiState(
    val nameInput: String = "",
    val selectedColor: String = "#FFE600",
    val selectedIconKey: String = "folder",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) {
    val characterCount: Int
        get() = nameInput.length

    val isSubmitEnabled: Boolean
        get() = nameInput.isNotBlank() && nameInput.length <= 40 && !isSubmitting
}
