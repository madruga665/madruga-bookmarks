package com.madruga665.bookmarks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow
import com.madruga665.bookmarks.ui.utils.BookmarkDisplayUtils
import com.madruga665.bookmarks.ui.utils.tagList

@Composable
fun NeobrutalistBookmarkCard(
    bookmark: BookmarkEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayTitle = BookmarkDisplayUtils.getDisplayTitle(bookmark.title, bookmark.url)
    val displayThumbnail = BookmarkDisplayUtils.getThumbnailUrl(bookmark.thumbnailUrl, bookmark.url)
    val sourceLabel = BookmarkDisplayUtils.getSourceLabel(bookmark.sourcePlatform, bookmark.url)
    val faviconModel = BookmarkDisplayUtils.getFaviconUrl(bookmark.faviconUrl, bookmark.url)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tag_bookmark_card_${bookmark.id}")
            .neobrutalistShadow(
                shadowColor = NeobrutalismTheme.colors.shadow,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = NeobrutalismTheme.colors.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Top Preview Image Container (116.dp height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(116.dp)
                    .background(NeobrutalismTheme.colors.accentYellow.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                if (!displayThumbnail.isNullOrBlank()) {
                    AsyncImage(
                        model = displayThumbnail,
                        contentDescription = "Bookmark thumbnail preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .neobrutalistShadow(
                                shadowColor = NeobrutalismTheme.colors.shadow,
                                borderColor = NeobrutalismTheme.colors.border,
                                borderWidth = 2.dp,
                                shadowOffset = 2.dp,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(NeobrutalismTheme.colors.surface, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Link,
                            contentDescription = "Bookmark preview",
                            tint = NeobrutalismTheme.colors.onSurface,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // Card Body: Title + Origin Platform Metadata Row
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Link Title (Bold, max 2 lines)
                Text(
                    text = displayTitle,
                    style = NeobrutalismTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        lineHeight = 17.sp
                    ),
                    color = NeobrutalismTheme.colors.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Metadata Row: Origin Icon + Platform Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!faviconModel.isNullOrBlank()) {
                            AsyncImage(
                                model = faviconModel,
                                contentDescription = "Source Icon",
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.Language,
                                contentDescription = "Source",
                                tint = NeobrutalismTheme.colors.onSurface,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = sourceLabel,
                        style = NeobrutalismTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = NeobrutalismTheme.colors.subtext,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Tag Badges Row
                val tags = bookmark.tagList
                if (tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val maxDisplay = 3
                        tags.take(maxDisplay).forEach { tag ->
                            NeobrutalistTagChip(
                                tag = tag,
                                showHash = true
                            )
                        }
                        if (tags.size > maxDisplay) {
                            Text(
                                text = "+${tags.size - maxDisplay}",
                                style = NeobrutalismTheme.typography.bodySmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = NeobrutalismTheme.colors.subtext
                            )
                        }
                    }
                }
            }
        }
    }
}

