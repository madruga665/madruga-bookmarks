package com.madruga665.bookmarks.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import kotlin.math.roundToInt

@Composable
fun BookmarkActionsOverlay(
    bookmark: BookmarkEntity?,
    cardOffset: Offset?,
    cardSize: IntSize?,
    touchPositionInWindow: Offset?,
    dragPositionInWindow: Offset? = null,
    onHoveredOptionChange: (BookmarkOption?) -> Unit,
    hoveredOption: BookmarkOption? = null,
    onSelectItem: ((BookmarkOption) -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = bookmark != null && cardOffset != null && cardSize != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        if (bookmark == null || cardOffset == null || cardSize == null) return@AnimatedVisibility

        val cardWidthDp = with(density) { cardSize.width.toDp() }
        val anchorPosition = touchPositionInWindow ?: Offset(
            x = cardOffset.x + cardSize.width / 2f,
            y = cardOffset.y + cardSize.height / 2f
        )

        val openLabel = stringResource(R.string.bookmark_action_open)
        val pinLabel = if (bookmark.isPinned) stringResource(R.string.bookmark_action_unpin) else stringResource(R.string.bookmark_action_pin)
        val shareLabel = stringResource(R.string.bookmark_action_share)
        val deleteLabel = stringResource(R.string.bookmark_action_delete)
        val accentYellow = NeobrutalismTheme.colors.accentYellow
        val accentBlue = NeobrutalismTheme.colors.accentBlue

        val items = remember(
            bookmark.isPinned,
            openLabel, pinLabel, shareLabel, deleteLabel,
            accentYellow, accentBlue,
            onSelectItem
        ) {
            listOf(
                ArcActionItem(
                    id = BookmarkOption.OPEN,
                    icon = Icons.AutoMirrored.Outlined.OpenInNew,
                    label = openLabel,
                    contentDescription = openLabel,
                    activeColor = Color(0xFF4ADE80),
                    onClick = { onSelectItem?.invoke(BookmarkOption.OPEN) }
                ),
                ArcActionItem(
                    id = BookmarkOption.PIN,
                    icon = if (bookmark.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                    label = pinLabel,
                    contentDescription = pinLabel,
                    activeColor = accentYellow,
                    onClick = { onSelectItem?.invoke(BookmarkOption.PIN) }
                ),
                ArcActionItem(
                    id = BookmarkOption.SHARE,
                    icon = Icons.Outlined.Share,
                    label = shareLabel,
                    contentDescription = shareLabel,
                    activeColor = accentBlue,
                    onClick = { onSelectItem?.invoke(BookmarkOption.SHARE) }
                ),
                ArcActionItem(
                    id = BookmarkOption.DELETE,
                    icon = Icons.Outlined.Delete,
                    label = deleteLabel,
                    contentDescription = deleteLabel,
                    activeColor = Color(0xFFFF4B4B),
                    onClick = { onSelectItem?.invoke(BookmarkOption.DELETE) }
                )
            )
        }

        Box(modifier = modifier.fillMaxSize()) {
            // Render the highlighted NeobrutalistBookmarkCard at cardOffset with width cardSize.width
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = cardOffset.x.roundToInt(),
                            y = cardOffset.y.roundToInt()
                        )
                    }
                    .width(cardWidthDp)
            ) {
                NeobrutalistBookmarkCard(
                    bookmark = bookmark,
                    onClick = { onDismiss?.invoke() },
                    isActiveMenu = true
                )
            }

            // NeobrutalistArcActionsMenu
            NeobrutalistArcActionsMenu(
                items = items,
                anchorPosition = anchorPosition,
                dragPosition = dragPositionInWindow,
                hoveredItemId = hoveredOption,
                onHoveredItemChange = onHoveredOptionChange,
                onSelectItem = { option -> onSelectItem?.invoke(option) },
                onDismiss = { onDismiss?.invoke() }
            )
        }
    }
}
