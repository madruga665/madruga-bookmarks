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
import java.net.URI

@Composable
fun NeobrutalistBookmarkCard(
    bookmark: BookmarkEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayTitle = when {
        !bookmark.title.isNullOrBlank() && !bookmark.title.startsWith("@") -> bookmark.title
        else -> parseTitleFromUrl(bookmark.url)
    }

    val displayThumbnail = when {
        !bookmark.thumbnailUrl.isNullOrBlank() -> bookmark.thumbnailUrl
        else -> generateFallbackThumbnailUrl(bookmark.url)
    }

    val sourceLabel = parseSourceLabel(bookmark.sourcePlatform, bookmark.url)
    val faviconModel = bookmark.faviconUrl?.takeIf { it.isNotBlank() }
        ?: getFaviconUrlFromDomain(bookmark.url)

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
            }
        }
    }
}

private fun parseTitleFromUrl(url: String): String {
    return try {
        val uri = URI(url)
        val host = uri.host?.removePrefix("www.") ?: ""
        val pathParts = uri.path?.split("/")?.filter { it.isNotBlank() } ?: emptyList()

        when {
            host.contains("instagram.com", ignoreCase = true) -> {
                val username = pathParts.firstOrNull { it != "p" && it != "reel" } ?: "devemdobro"
                "${username.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }} | Programação on Instagram"
            }
            host.contains("github.com", ignoreCase = true) -> {
                val repo = pathParts.take(2).joinToString("/")
                if (repo.isNotBlank()) "$repo: GitHub Repository" else "GitHub"
            }
            host.contains("youtube.com", ignoreCase = true) || host.contains("youtu.be", ignoreCase = true) -> "YouTube Video"
            host.contains("linkedin.com", ignoreCase = true) -> "LinkedIn Post"
            else -> {
                val cleanDomain = host.split(".").firstOrNull()?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } ?: "Web"
                val firstPath = pathParts.firstOrNull()?.replace("-", " ")?.replace("_", " ")
                if (firstPath != null) "$firstPath on $cleanDomain" else cleanDomain
            }
        }
    } catch (e: Exception) {
        url
    }
}

private fun generateFallbackThumbnailUrl(url: String): String {
    val cleanHost = try {
        URI(url).host?.removePrefix("www.") ?: "default"
    } catch (e: Exception) {
        "default"
    }

    return when {
        cleanHost.contains("instagram.com", ignoreCase = true) ->
            "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80"
        cleanHost.contains("github.com", ignoreCase = true) ->
            "https://images.unsplash.com/photo-1618401471353-b98afee0b2eb?w=600&auto=format&fit=crop&q=80"
        cleanHost.contains("youtube.com", ignoreCase = true) || cleanHost.contains("youtu.be", ignoreCase = true) ->
            "https://images.unsplash.com/photo-1611162617213-7d7a39e9b1d7?w=600&auto=format&fit=crop&q=80"
        else ->
            "https://picsum.photos/seed/${cleanHost.hashCode()}/600/400"
    }
}

private fun parseSourceLabel(sourcePlatform: String?, url: String): String {
    if (!sourcePlatform.isNullOrBlank()) {
        val clean = sourcePlatform.removePrefix("@").trim()
        if (clean.equals("instagram", ignoreCase = true)) return "Instagram"
        if (clean.equals("github", ignoreCase = true)) return "GitHub"
        if (clean.equals("linkedin", ignoreCase = true)) return "LinkedIn"
        if (clean.equals("youtube", ignoreCase = true)) return "YouTube"
        if (clean.equals("twitter", ignoreCase = true) || clean.equals("x", ignoreCase = true)) return "X / Twitter"
        return clean.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }

    return try {
        val uri = URI(url)
        val host = uri.host?.removePrefix("www.") ?: ""
        when {
            host.contains("instagram.com", ignoreCase = true) -> "Instagram"
            host.contains("linkedin.com", ignoreCase = true) -> "LinkedIn"
            host.contains("github.com", ignoreCase = true) -> "GitHub"
            host.contains("twitter.com", ignoreCase = true) || host.contains("x.com", ignoreCase = true) -> "X / Twitter"
            host.contains("youtube.com", ignoreCase = true) || host.contains("youtu.be", ignoreCase = true) -> "YouTube"
            host.contains("medium.com", ignoreCase = true) -> "Medium"
            else -> host.split(".").firstOrNull()?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } ?: "Web"
        }
    } catch (e: Exception) {
        "Web"
    }
}

private fun getFaviconUrlFromDomain(url: String): String? {
    return try {
        val uri = URI(url)
        val host = uri.host?.removePrefix("www.") ?: return null
        "https://www.google.com/s2/favicons?domain=$host&sz=128"
    } catch (e: Exception) {
        null
    }
}
