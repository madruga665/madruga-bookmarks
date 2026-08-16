package com.madruga665.bookmarks.ui.bookmark.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@Composable
fun BookmarkHeroSection(
    thumbnailUrl: String?,
    url: String,
    isPinned: Boolean,
    onTogglePin: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = NeobrutalismTheme.colors.accentYellow
) {
    val displayThumbnail = when {
        !thumbnailUrl.isNullOrBlank() -> thumbnailUrl
        else -> generateFallbackThumbnailUrl(url)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .neobrutalistShadow(
                shadowColor = NeobrutalismTheme.colors.shadow,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = accentColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (!displayThumbnail.isNullOrBlank()) {
            AsyncImage(
                model = displayThumbnail,
                contentDescription = stringResource(R.string.bookmark_hero_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .neobrutalistShadow(
                        shadowColor = NeobrutalismTheme.colors.shadow,
                        borderColor = NeobrutalismTheme.colors.border,
                        borderWidth = 2.dp,
                        shadowOffset = 3.dp,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(NeobrutalismTheme.colors.surface, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = stringResource(R.string.bookmark_placeholder_image),
                    tint = NeobrutalismTheme.colors.onSurface,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Top-right Overlay Pin Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            NeobrutalistButton(
                onClick = onTogglePin,
                modifier = Modifier.size(40.dp),
                containerColor = if (isPinned) NeobrutalismTheme.colors.accentYellow else NeobrutalismTheme.colors.surface,
                shape = RoundedCornerShape(10.dp),
                borderWidth = 2.dp,
                shadowOffset = 2.5.dp
            ) {
                Icon(
                    imageVector = if (isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                    contentDescription = if (isPinned) stringResource(R.string.bookmark_unpin) else stringResource(R.string.bookmark_pin),
                    tint = NeobrutalismTheme.colors.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun generateFallbackThumbnailUrl(url: String): String {
    val cleanHost = try {
        java.net.URI(url).host?.removePrefix("www.") ?: "default"
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
