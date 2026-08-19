package com.madruga665.bookmarks.ui.bookmark.components

import androidx.compose.runtime.Composable
import com.madruga665.bookmarks.ui.components.DeleteConfirmationDialog as SharedDeleteConfirmationDialog

@Composable
fun DeleteConfirmationDialog(
    isVisible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    SharedDeleteConfirmationDialog(
        isVisible = isVisible,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

