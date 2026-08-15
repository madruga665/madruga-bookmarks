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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.ui.components.NeobrutalistBookmarkCard
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme

@Composable
fun CollectionDetailScreen(
    uiState: CollectionDetailUiState,
    onBackClick: () -> Unit,
    onAddLinkClick: () -> Unit,
    onOptionsClick: () -> Unit,
    onBookmarkClick: (BookmarkEntity) -> Unit,
    onSubcollectionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val title = uiState.collection?.name ?: ""
    val linkCount = uiState.bookmarks.size
    val subcollectionCount = uiState.collection?.subcollectionCount ?: 0
    val subtitle = "$linkCount links · $subcollectionCount subcollections"

    val pinned = uiState.bookmarks.filter { it.isPinned }
    val allOthers = uiState.bookmarks.filter { !it.isPinned }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NeobrutalismTheme.colors.background)
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
                                text = "PINNED (${pinned.size})",
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
                                onClick = { onBookmarkClick(bookmark) }
                            )
                        }

                        item(span = { GridItemSpan(2) }) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    if (allOthers.isNotEmpty() || pinned.isEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Text(
                                text = if (pinned.isNotEmpty()) "ALL LINKS (${allOthers.size})" else "ALL LINKS ($linkCount)",
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
                                onClick = { onBookmarkClick(bookmark) }
                            )
                        }
                    }
                }
            }
        }
    }
}
