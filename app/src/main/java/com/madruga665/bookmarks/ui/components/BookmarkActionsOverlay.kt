package com.madruga665.bookmarks.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.madruga665.bookmarks.data.local.BookmarkEntity
import kotlin.math.roundToInt

@Composable
fun BookmarkActionsOverlay(
    bookmark: BookmarkEntity?,
    cardOffset: Offset?,
    cardSize: IntSize?,
    touchPositionInWindow: Offset?,
    onHoveredOptionChange: (BookmarkOption?) -> Unit,
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

        Box(modifier = modifier.fillMaxSize()) {
            // Full-screen dimmed background layer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.60f))
            )

            // Fully highlighted, non-dimmed active bookmark card + action buttons above background layer
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
                    onClick = {},
                    isActiveMenu = true,
                    touchPositionInWindow = touchPositionInWindow,
                    onHoveredOptionChange = onHoveredOptionChange
                )
            }
        }
    }
}
