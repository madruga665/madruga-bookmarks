package com.madruga665.bookmarks.ui.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme

/**
 * Section displaying the "RECENTLY SAVED" header and a horizontal carousel (LazyRow)
 * of recently saved bookmark cards.
 */
@Composable
fun RecentlySavedSection(
    bookmarks: List<BookmarkEntity>,
    collectionsMap: Map<String, CollectionEntity>,
    onBookmarkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (bookmarks.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tag_recently_saved_section")
    ) {
        // Section Header: Clock Icon + Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = null,
                tint = NeobrutalismTheme.colors.onSurface,
                modifier = Modifier.size(18.dp)
            )

            Text(
                text = stringResource(R.string.search_recently_saved).uppercase(),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = NeobrutalismTheme.colors.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal Carousel of Recent Bookmarks
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("tag_recently_saved_carousel")
        ) {
            items(
                items = bookmarks,
                key = { it.id }
            ) { bookmark ->
                val collection = collectionsMap[bookmark.collectionId]
                RecentlySavedBookmarkCard(
                    bookmark = bookmark,
                    collectionName = collection?.name,
                    collectionColor = collection?.colorAccent,
                    onClick = { onBookmarkClick(bookmark.id) }
                )
            }
        }
    }
}
