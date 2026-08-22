package com.madruga665.bookmarks.ui.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.ui.components.BookmarkActionsOverlay
import com.madruga665.bookmarks.ui.components.BookmarkOption
import com.madruga665.bookmarks.ui.components.DeleteConfirmationDialog
import com.madruga665.bookmarks.ui.components.NeobrutalistBookmarkCard
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistGridBackground

@Composable
fun CollectionDetailScreen(
    uiState: CollectionDetailUiState,
    onBackClick: () -> Unit,
    onAddLinkClick: () -> Unit,
    onOptionsClick: () -> Unit,
    onBookmarkClick: (BookmarkEntity) -> Unit,
    onSubcollectionClick: (String) -> Unit,
    onBookmarkLongPressStart: ((BookmarkEntity, Offset, Offset, IntSize) -> Unit)? = null,
    onBookmarkLongPressDrag: ((Offset) -> Unit)? = null,
    onBookmarkLongPressRelease: (() -> Unit)? = null,
    onBookmarkHoveredOptionChange: (BookmarkOption?) -> Unit = {},
    onDismissBookmarkActionsMenu: () -> Unit = {},
    onDismissDeleteBookmarkDialog: () -> Unit = {},
    onConfirmDeleteBookmark: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val title = uiState.collection?.name ?: ""
    val linkCount = uiState.bookmarks.size
    val subcollectionCount = uiState.collection?.subcollectionCount ?: 0
    val subtitle = stringResource(R.string.collection_subtitle_fmt, linkCount, subcollectionCount)

    val pinned = uiState.bookmarks.filter { it.isPinned }
    val allOthers = uiState.bookmarks.filter { !it.isPinned }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NeobrutalismTheme.colors.background)
            .neobrutalistGridBackground(NeobrutalismTheme.colors.gridLine)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            CollectionHeader(
                title = title,
                subtitle = subtitle,
                onBackClick = onBackClick,
                onAddLinkClick = onAddLinkClick,
                onOptionsClick = onOptionsClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("tag_collection_detail_loading"),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = NeobrutalismTheme.colors.onSurface
                        )
                    }
                }
                uiState.bookmarks.isEmpty() && uiState.subcollections.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        EmptyCollectionContent(onAddLinkClick = onAddLinkClick)
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("tag_collection_detail_grid")
                    ) {
                        if (pinned.isNotEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                Text(
                                    text = stringResource(R.string.collection_pinned_fmt, pinned.size),
                                    style = NeobrutalismTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    ),
                                    color = NeobrutalismTheme.colors.onSurface,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }

                            items(
                                items = pinned,
                                key = { "pinned_${it.id}" }
                            ) { bookmark ->
                                NeobrutalistBookmarkCard(
                                    bookmark = bookmark,
                                    onClick = { onBookmarkClick(bookmark) },
                                    onLongPressStart = onBookmarkLongPressStart,
                                    onLongPressDrag = onBookmarkLongPressDrag,
                                    onLongPressRelease = onBookmarkLongPressRelease
                                )
                            }

                            item(span = { GridItemSpan(2) }) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        if (allOthers.isNotEmpty() || pinned.isEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                val count = if (pinned.isNotEmpty()) allOthers.size else linkCount
                                Text(
                                    text = stringResource(R.string.collection_all_links_fmt, count),
                                    style = NeobrutalismTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    ),
                                    color = NeobrutalismTheme.colors.onSurface,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }

                            items(
                                items = allOthers,
                                key = { it.id }
                            ) { bookmark ->
                                NeobrutalistBookmarkCard(
                                    bookmark = bookmark,
                                    onClick = { onBookmarkClick(bookmark) },
                                    onLongPressStart = onBookmarkLongPressStart,
                                    onLongPressDrag = onBookmarkLongPressDrag,
                                    onLongPressRelease = onBookmarkLongPressRelease
                                )
                            }
                        }
                    }
                }
            }
        }

        BookmarkActionsOverlay(
            bookmark = uiState.activeMenuBookmark,
            cardOffset = uiState.activeCardOffset,
            cardSize = uiState.activeCardSize,
            touchPositionInWindow = uiState.touchPositionInWindow,
            onHoveredOptionChange = onBookmarkHoveredOptionChange
        )

        DeleteConfirmationDialog(
            isVisible = uiState.bookmarkToDelete != null,
            onConfirm = { uiState.bookmarkToDelete?.let { onConfirmDeleteBookmark(it.id) } },
            onDismiss = onDismissDeleteBookmarkDialog
        )
    }
}
