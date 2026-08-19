package com.madruga665.bookmarks.ui.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.ui.components.NeobrutalistBookmarkCard
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme

/**
 * Vertical LazyColumn displaying search matches with result count header
 * and NeobrutalistBookmarkCard items.
 */
@Composable
fun SearchResultsList(
    searchResults: List<BookmarkEntity>,
    collectionsMap: Map<String, CollectionEntity>,
    onBookmarkClick: (String) -> Unit,
    onLongPressStart: ((BookmarkEntity, Offset, Offset, IntSize) -> Unit)? = null,
    onLongPressDrag: ((Offset) -> Unit)? = null,
    onLongPressRelease: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tag_search_results_list"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp)
    ) {
        item(key = "search_results_header") {
            Text(
                text = stringResource(R.string.search_results_count_fmt, searchResults.size).uppercase(),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = NeobrutalismTheme.colors.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .testTag("tag_search_results_count")
            )
        }

        items(
            items = searchResults,
            key = { it.id }
        ) { bookmark ->
            NeobrutalistBookmarkCard(
                bookmark = bookmark,
                onClick = { onBookmarkClick(bookmark.id) },
                onLongPressStart = onLongPressStart,
                onLongPressDrag = onLongPressDrag,
                onLongPressRelease = onLongPressRelease,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
