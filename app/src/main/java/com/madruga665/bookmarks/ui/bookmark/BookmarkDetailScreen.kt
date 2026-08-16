package com.madruga665.bookmarks.ui.bookmark

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.bookmark.components.AddTagDialog
import com.madruga665.bookmarks.ui.bookmark.components.BookmarkDescriptionSection
import com.madruga665.bookmarks.ui.bookmark.components.BookmarkDetailTopBar
import com.madruga665.bookmarks.ui.bookmark.components.BookmarkHeroSection
import com.madruga665.bookmarks.ui.bookmark.components.BookmarkNotesSection
import com.madruga665.bookmarks.ui.bookmark.components.BookmarkTagsSection
import com.madruga665.bookmarks.ui.bookmark.components.BookmarkTitleSection
import com.madruga665.bookmarks.ui.bookmark.components.BookmarkUrlCard
import com.madruga665.bookmarks.ui.bookmark.components.DeleteConfirmationDialog
import com.madruga665.bookmarks.ui.bookmark.components.MoveCollectionBottomSheet
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme

@Composable
fun BookmarkDetailScreen(
    uiState: BookmarkDetailUiState,
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onShareClick: () -> Unit,
    onMoveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onTogglePin: () -> Unit,
    onStartEditingTitle: () -> Unit,
    onTitleChange: (String) -> Unit,
    onSaveTitle: () -> Unit,
    onCancelEditingTitle: () -> Unit,
    onUrlClick: (String) -> Unit,
    onToggleDescriptionExpanded: () -> Unit = {},
    onStartEditingNotes: () -> Unit = {},
    onNotesChange: (String) -> Unit = {},
    onSaveNotes: () -> Unit = {},
    onCancelEditingNotes: () -> Unit = {},
    onOpenAddTagDialog: () -> Unit = {},
    onNewTagInputChange: (String) -> Unit = {},
    onSaveNewTag: () -> Unit = {},
    onDismissAddTagDialog: () -> Unit = {},
    onRemoveTag: (String) -> Unit = {},
    onOpenMoveCollectionSheet: () -> Unit = {},
    onDismissMoveCollectionSheet: () -> Unit = {},
    onSelectCollection: (String) -> Unit = {},
    onOpenDeleteDialog: () -> Unit = {},
    onDismissDeleteDialog: () -> Unit = {},
    onConfirmDelete: () -> Unit = {},
    onClearUserMessage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            onClearUserMessage()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NeobrutalismTheme.colors.background)
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = NeobrutalismTheme.colors.onSurface
                    )
                }
            }
            uiState.bookmark == null -> {
                BookmarkDetailTopBar(
                    platformBadge = stringResource(R.string.bookmark_platform_link),
                    onBackClick = onBackClick,
                    onRefreshClick = {},
                    onShareClick = {},
                    onMoveClick = {},
                    onDeleteClick = {}
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.bookmark_not_found),
                        style = NeobrutalismTheme.typography.titleMedium,
                        color = NeobrutalismTheme.colors.subtext
                    )
                }
            }
            else -> {
                val bookmark = uiState.bookmark
                val badgeText = bookmark.sourcePlatform
                    ?.takeIf { it.isNotBlank() }
                    ?.removePrefix("@")
                    ?: uiState.collection?.name
                    ?: stringResource(R.string.bookmark_platform_link)

                BookmarkDetailTopBar(
                    platformBadge = badgeText,
                    onBackClick = onBackClick,
                    onRefreshClick = onRefreshClick,
                    onShareClick = onShareClick,
                    onMoveClick = onMoveClick,
                    onDeleteClick = onDeleteClick
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(4.dp))

                    BookmarkHeroSection(
                        thumbnailUrl = bookmark.thumbnailUrl,
                        url = bookmark.url,
                        isPinned = bookmark.isPinned,
                        onTogglePin = onTogglePin
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    BookmarkTitleSection(
                        title = bookmark.title ?: "",
                        isEditing = uiState.isEditingTitle,
                        editedTitle = uiState.editedTitle,
                        categoryName = uiState.collection?.name,
                        createdAt = bookmark.createdAt,
                        onStartEditing = onStartEditingTitle,
                        onTitleChange = onTitleChange,
                        onSaveTitle = onSaveTitle,
                        onCancelEditing = onCancelEditingTitle
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    BookmarkUrlCard(
                        url = bookmark.url,
                        onUrlClick = onUrlClick
                    )

                    if (!bookmark.description.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(20.dp))

                        BookmarkDescriptionSection(
                            description = bookmark.description,
                            isExpanded = uiState.isDescriptionExpanded,
                            onToggleExpand = onToggleDescriptionExpanded
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    BookmarkTagsSection(
                        tags = bookmark.tags,
                        onOpenAddTagDialog = onOpenAddTagDialog,
                        onRemoveTag = onRemoveTag
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    BookmarkNotesSection(
                        notes = bookmark.notes,
                        isEditing = uiState.isEditingNotes,
                        editedNotes = uiState.editedNotes,
                        onStartEditing = onStartEditingNotes,
                        onNotesChange = onNotesChange,
                        onSaveNotes = onSaveNotes,
                        onCancelEditing = onCancelEditingNotes
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }

                if (uiState.isAddingTag) {
                    AddTagDialog(
                        tagInput = uiState.newTagInput,
                        onTagInputChange = onNewTagInputChange,
                        onSaveTag = onSaveNewTag,
                        onDismiss = onDismissAddTagDialog
                    )
                }

                if (uiState.isMoveSheetVisible) {
                    MoveCollectionBottomSheet(
                        isVisible = uiState.isMoveSheetVisible,
                        availableCollections = uiState.availableCollections,
                        currentCollectionId = bookmark.collectionId,
                        onSelectCollection = onSelectCollection,
                        onDismiss = onDismissMoveCollectionSheet
                    )
                }

                if (uiState.isConfirmingDelete) {
                    DeleteConfirmationDialog(
                        isVisible = uiState.isConfirmingDelete,
                        onConfirm = onConfirmDelete,
                        onDismiss = onDismissDeleteDialog
                    )
                }
            }
        }
    }
}

fun shareBookmark(context: Context, title: String?, url: String) {
    try {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title ?: context.getString(R.string.app_name))
            putExtra(Intent.EXTRA_TEXT, if (!title.isNullOrBlank()) "$title\n$url" else url)
        }
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.bookmark_share_chooser_title)))
    } catch (e: Exception) {
        Toast.makeText(context, context.getString(R.string.bookmark_share_error), Toast.LENGTH_SHORT).show()
    }
}

fun openBrowserUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, context.getString(R.string.bookmark_url_error_fmt, url), Toast.LENGTH_SHORT).show()
    }
}
