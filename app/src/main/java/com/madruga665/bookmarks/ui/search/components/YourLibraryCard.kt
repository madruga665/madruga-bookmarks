package com.madruga665.bookmarks.ui.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.search.LibraryStats
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

/**
 * Neobrutalist 4-column metric summary card displaying live counts for
 * Collections, Links, Pinned items, and Tags.
 */
@Composable
fun YourLibraryCard(
    stats: LibraryStats,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tag_search_your_library_card")
            .neobrutalistShadow(
                shadowColor = NeobrutalismTheme.colors.shadow,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.dp,
                shadowOffset = 4.dp,
                shape = cardShape
            )
            .background(NeobrutalismTheme.colors.accentYellow, cardShape)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.search_your_library).uppercase(),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = NeobrutalismTheme.colors.border
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatColumn(
                    icon = Icons.Outlined.Folder,
                    count = stats.collectionsCount,
                    label = stringResource(R.string.search_collections),
                    modifier = Modifier.weight(1f)
                )

                StatDivider()

                StatColumn(
                    icon = Icons.Outlined.Link,
                    count = stats.linksCount,
                    label = stringResource(R.string.search_links),
                    modifier = Modifier.weight(1f)
                )

                StatDivider()

                StatColumn(
                    icon = Icons.Outlined.PushPin,
                    count = stats.pinnedCount,
                    label = stringResource(R.string.search_pinned),
                    modifier = Modifier.weight(1f)
                )

                StatDivider()

                StatColumn(
                    icon = Icons.Outlined.LocalOffer,
                    count = stats.tagsCount,
                    label = stringResource(R.string.search_tags),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatColumn(
    icon: ImageVector,
    count: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NeobrutalismTheme.colors.border,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "$count",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = NeobrutalismTheme.colors.border
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = label,
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeobrutalismTheme.colors.border.copy(alpha = 0.75f)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(32.dp)
            .background(NeobrutalismTheme.colors.border.copy(alpha = 0.2f))
    )
}
