package com.madruga665.bookmarks.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow
import com.madruga665.bookmarks.ui.utils.BookmarkDisplayUtils
import com.madruga665.bookmarks.ui.utils.tagList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NeobrutalistBookmarkCard(
    bookmark: BookmarkEntity,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    isActiveMenu: Boolean = false,
    touchPositionInWindow: Offset? = null,
    onHoveredOptionChange: (BookmarkOption?) -> Unit = {},
    onLongPressStart: ((BookmarkEntity, Offset, Offset, IntSize) -> Unit)? = null,
    onLongPressDrag: ((Offset) -> Unit)? = null,
    onLongPressRelease: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    var cardWindowOffset by remember { mutableStateOf(Offset.Zero) }
    var cardSize by remember { mutableStateOf(IntSize.Zero) }

    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val isRightColumn = remember(cardWindowOffset, cardSize, screenWidthPx) {
        if (cardWindowOffset == Offset.Zero) false
        else (cardWindowOffset.x + cardSize.width / 2f) > (screenWidthPx / 2f)
    }

    var openRect by remember { mutableStateOf<Rect?>(null) }
    var pinRect by remember { mutableStateOf<Rect?>(null) }
    var shareRect by remember { mutableStateOf<Rect?>(null) }
    var deleteRect by remember { mutableStateOf<Rect?>(null) }

    val hoveredOption = remember(touchPositionInWindow, openRect, pinRect, shareRect, deleteRect, isActiveMenu) {
        if (!isActiveMenu) null
        else {
            val pos = touchPositionInWindow ?: return@remember null
            val hitPadding = 40f
            when {
                openRect?.inflate(hitPadding)?.contains(pos) == true -> BookmarkOption.OPEN
                pinRect?.inflate(hitPadding)?.contains(pos) == true -> BookmarkOption.PIN
                shareRect?.inflate(hitPadding)?.contains(pos) == true -> BookmarkOption.SHARE
                deleteRect?.inflate(hitPadding)?.contains(pos) == true -> BookmarkOption.DELETE
                else -> null
            }
        }
    }

    LaunchedEffect(hoveredOption) {
        if (isActiveMenu) {
            onHoveredOptionChange(hoveredOption)
            if (hoveredOption != null) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }
        }
    }

    val cardRotation by animateFloatAsState(
        targetValue = if (isActiveMenu) -3.5f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardRotation"
    )

    val cardScale by animateFloatAsState(
        targetValue = if (isActiveMenu) 1.03f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardScale"
    )

    val gestureModifier = if (onLongPressStart != null) {
        Modifier.pointerInput(bookmark.id) {
            awaitEachGesture {
                val down = awaitFirstDown(requireUnconsumed = false)
                val downTime = System.currentTimeMillis()
                var isLongPressActive = false
                val pointerId = down.id

                while (true) {
                    val event = awaitPointerEvent()
                    val change = event.changes.firstOrNull { it.id == pointerId }

                    if (change == null || !change.pressed) {
                        if (isLongPressActive) {
                            onLongPressRelease?.invoke()
                        } else {
                            val duration = System.currentTimeMillis() - downTime
                            if (duration < 350) {
                                onClick()
                            }
                        }
                        break
                    }

                    val elapsed = System.currentTimeMillis() - downTime
                    if (!isLongPressActive && elapsed >= 350) {
                        isLongPressActive = true
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        val touchInWindow = cardWindowOffset + change.position
                        onLongPressStart(bookmark, touchInWindow, cardWindowOffset, cardSize)
                    }

                    if (isLongPressActive) {
                        change.consume()
                        val touchInWindow = cardWindowOffset + change.position
                        onLongPressDrag?.invoke(touchInWindow)
                    }
                }
            }
        }
    } else if (onLongClick != null) {
        Modifier.combinedClickable(
            onClick = onClick,
            onLongClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onLongClick()
            }
        )
    } else {
        Modifier.clickable(onClick = onClick)
    }

    val displayTitle = BookmarkDisplayUtils.getDisplayTitle(bookmark.title, bookmark.url)
    val displayThumbnail = BookmarkDisplayUtils.getThumbnailUrl(bookmark.thumbnailUrl, bookmark.url)
    val sourceLabel = BookmarkDisplayUtils.getSourceLabel(bookmark.sourcePlatform, bookmark.url)
    val faviconModel = BookmarkDisplayUtils.getFaviconUrl(bookmark.faviconUrl, bookmark.url)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tag_bookmark_card_${bookmark.id}")
            .onGloballyPositioned { coordinates ->
                cardWindowOffset = coordinates.positionInWindow()
                cardSize = coordinates.size
            }
            .graphicsLayer(
                rotationZ = cardRotation,
                scaleX = cardScale,
                scaleY = cardScale
            )
    ) {
        // Main Bookmark Card Body
        Box(
            modifier = Modifier
                .fillMaxWidth()
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
                .then(gestureModifier)
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

        // Inline Floating Action Buttons in a Clean Vertical Column
        if (isActiveMenu) {
            Column(
                modifier = Modifier
                    .align(if (isRightColumn) Alignment.CenterStart else Alignment.CenterEnd)
                    .offset(x = if (isRightColumn) (-44).dp else 44.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = if (isRightColumn) Alignment.End else Alignment.Start
            ) {
                InlineBookmarkActionButtonItem(
                    icon = Icons.AutoMirrored.Outlined.OpenInNew,
                    labelText = stringResource(R.string.bookmark_action_open),
                    contentDescription = stringResource(R.string.bookmark_action_open),
                    isHovered = hoveredOption == BookmarkOption.OPEN,
                    activeColor = Color(0xFF4ADE80),
                    textOnLeft = isRightColumn,
                    onPositioned = { openRect = it }
                )

                InlineBookmarkActionButtonItem(
                    icon = if (bookmark.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                    labelText = if (bookmark.isPinned) stringResource(R.string.bookmark_action_unpin) else stringResource(R.string.bookmark_action_pin),
                    contentDescription = if (bookmark.isPinned) stringResource(R.string.bookmark_action_unpin) else stringResource(R.string.bookmark_action_pin),
                    isHovered = hoveredOption == BookmarkOption.PIN,
                    activeColor = NeobrutalismTheme.colors.accentYellow,
                    textOnLeft = isRightColumn,
                    onPositioned = { pinRect = it }
                )

                InlineBookmarkActionButtonItem(
                    icon = Icons.Outlined.Share,
                    labelText = stringResource(R.string.bookmark_action_share),
                    contentDescription = stringResource(R.string.bookmark_action_share),
                    isHovered = hoveredOption == BookmarkOption.SHARE,
                    activeColor = NeobrutalismTheme.colors.accentBlue,
                    textOnLeft = isRightColumn,
                    onPositioned = { shareRect = it }
                )

                InlineBookmarkActionButtonItem(
                    icon = Icons.Outlined.Delete,
                    labelText = stringResource(R.string.bookmark_action_delete),
                    contentDescription = stringResource(R.string.bookmark_action_delete),
                    isHovered = hoveredOption == BookmarkOption.DELETE,
                    activeColor = Color(0xFFFF4B4B),
                    textOnLeft = isRightColumn,
                    onPositioned = { deleteRect = it }
                )
            }
        }
    }
}

@Composable
private fun InlineBookmarkActionButtonItem(
    icon: ImageVector,
    labelText: String,
    contentDescription: String,
    isHovered: Boolean,
    activeColor: Color,
    textOnLeft: Boolean,
    onPositioned: (Rect) -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isHovered) 1.25f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val backgroundColor = if (isHovered) activeColor else Color.White

    val textBadge = @Composable {
        Box(
            modifier = Modifier
                .neobrutalistShadow(
                    shadowColor = Color.Black,
                    borderColor = Color.Black,
                    borderWidth = 2.dp,
                    shadowOffset = 2.dp,
                    shape = RoundedCornerShape(6.dp)
                )
                .background(if (isHovered) activeColor else Color.White, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = labelText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                letterSpacing = 0.5.sp
            )
        }
    }

    val iconButton = @Composable {
        Box(
            modifier = Modifier
                .size(46.dp)
                .neobrutalistShadow(
                    shadowColor = Color.Black,
                    borderColor = Color.Black,
                    borderWidth = if (isHovered) 3.5.dp else 2.5.dp,
                    shadowOffset = if (isHovered) 4.dp else 2.5.dp,
                    shape = CircleShape
                )
                .background(backgroundColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    Row(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                onPositioned(coordinates.boundsInWindow())
            }
            .graphicsLayer(
                scaleX = animatedScale,
                scaleY = animatedScale
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (textOnLeft) {
            textBadge()
            iconButton()
        } else {
            iconButton()
            textBadge()
        }
    }
}
