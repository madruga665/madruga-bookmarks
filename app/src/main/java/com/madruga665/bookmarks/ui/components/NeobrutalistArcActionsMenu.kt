package com.madruga665.bookmarks.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow
import kotlin.math.roundToInt

/**
 * Reusable Neobrutalist Arc / Satellite Actions Menu composable.
 *
 * Blossoms satellite action buttons outward along an adaptive radial arc centered directly
 * at the user's touch anchor coordinate. Supports boundary collision avoidance,
 * spring blossom animations, continuous pointer drag hit-testing, haptic feedback,
 * and discrete tap selection.
 *
 * @param items List of action items to display on the arc.
 * @param anchorPosition The touch position in window pixels where the gesture occurred.
 * @param hoveredItemId Identifier of the currently hovered item (if any).
 * @param onHoveredItemChange Callback fired when the hovered item changes during drag/interaction.
 * @param onSelectItem Callback fired when an action item is selected via release or tap.
 * @param onDismiss Callback fired when the menu is dismissed via backdrop tap or back press.
 * @param modifier Modifier applied to the root container.
 * @param radiusDp Radial distance from anchor to satellite buttons (default 100.dp).
 * @param isActive Whether the menu is currently active / visible.
 */
@Composable
fun <T> NeobrutalistArcActionsMenu(
    items: List<ArcActionItem<T>>,
    anchorPosition: Offset,
    hoveredItemId: T?,
    onHoveredItemChange: (T?) -> Unit,
    onSelectItem: (T) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dragPosition: Offset? = null,
    radiusDp: Dp = 100.dp,
    isActive: Boolean = true
) {
    if (items.isEmpty() || !anchorPosition.isSpecified) return

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(hoveredItemId) {
        if (hoveredItemId != null) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    BackHandler(enabled = isActive) {
        onDismiss()
    }

    val targetRadiusPx = with(density) { radiusDp.toPx() }
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val animatedRadiusPx by animateFloatAsState(
        targetValue = if (isActive) targetRadiusPx else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "arcRadius"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isActive) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "arcAlpha"
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isActive) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "arcScale"
    )

    if (!isActive && animatedAlpha == 0f && animatedRadiusPx == 0f) {
        return
    }

    val (startAngle, sweepAngle) = remember(anchorPosition, screenWidthPx, screenHeightPx, targetRadiusPx) {
        ArcGeometryCalculator.calculateSector(
            anchor = anchorPosition,
            screenWidth = screenWidthPx,
            screenHeight = screenHeightPx,
            radius = targetRadiusPx
        )
    }

    val targetItemPositions = remember(anchorPosition, items.size, targetRadiusPx, startAngle, sweepAngle) {
        ArcGeometryCalculator.calculateItemPositions(
            anchor = anchorPosition,
            itemCount = items.size,
            radius = targetRadiusPx,
            startAngle = startAngle,
            sweepAngle = sweepAngle
        )
    }

    val visualItemPositions = remember(anchorPosition, items.size, animatedRadiusPx, startAngle, sweepAngle) {
        ArcGeometryCalculator.calculateItemPositions(
            anchor = anchorPosition,
            itemCount = items.size,
            radius = animatedRadiusPx,
            startAngle = startAngle,
            sweepAngle = sweepAngle
        )
    }

    val buttonRadiusPx = with(density) { 26.dp.toPx() }
    val hitPaddingPx = with(density) { 54.dp.toPx() }

    LaunchedEffect(dragPosition, targetItemPositions) {
        if (dragPosition != null && anchorPosition.isSpecified) {
            val hoveredIndex = ArcGeometryCalculator.findHoveredItemIndex(
                touchPosition = dragPosition,
                anchor = anchorPosition,
                itemPositions = targetItemPositions,
                buttonRadius = buttonRadiusPx,
                hitPadding = hitPaddingPx
            )
            val newHovered = hoveredIndex?.let { items.getOrNull(it)?.id }
            if (newHovered != hoveredItemId) {
                onHoveredItemChange(newHovered)
            }
        }
    }

    val gestureModifier = Modifier.pointerInput(items, targetItemPositions, anchorPosition) {
        awaitEachGesture {
            val down = awaitFirstDown(requireUnconsumed = false)
            var currentHovered: T? = null

            val initialIndex = ArcGeometryCalculator.findHoveredItemIndex(
                touchPosition = down.position,
                anchor = anchorPosition,
                itemPositions = targetItemPositions,
                buttonRadius = buttonRadiusPx,
                hitPadding = hitPaddingPx
            )
            currentHovered = initialIndex?.let { items.getOrNull(it)?.id }
            if (currentHovered != null && currentHovered != hoveredItemId) {
                onHoveredItemChange(currentHovered)
            }

            val pointerId = down.id
            var isDragging = false
            val startPos = down.position

            while (true) {
                val event = awaitPointerEvent()
                val change = event.changes.firstOrNull { it.id == pointerId }

                if (change == null || !change.pressed) {
                    if (isDragging && currentHovered != null) {
                        onSelectItem(currentHovered)
                    } else {
                        onDismiss()
                    }
                    break
                }

                val movedDist = (change.position - startPos).getDistance()
                if (movedDist > 10f) {
                    isDragging = true
                    change.consume()
                }

                if (isDragging) {
                    val hoveredIndex = ArcGeometryCalculator.findHoveredItemIndex(
                        touchPosition = change.position,
                        anchor = anchorPosition,
                        itemPositions = targetItemPositions,
                        buttonRadius = buttonRadiusPx,
                        hitPadding = hitPaddingPx
                    )
                    val newHovered = hoveredIndex?.let { items.getOrNull(it)?.id }
                    if (newHovered != currentHovered) {
                        currentHovered = newHovered
                        onHoveredItemChange(currentHovered)
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(gestureModifier)
    ) {
        // Fullscreen Dimmed Backdrop
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.60f * animatedAlpha))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
        )

        // Radial Satellite Items
        items.forEachIndexed { index, item ->
            val position = visualItemPositions.getOrNull(index) ?: return@forEachIndexed
            val isHovered = (hoveredItemId == item.id)

            SatelliteItemRow(
                item = item,
                position = position,
                anchorPosition = anchorPosition,
                isHovered = isHovered,
                animatedAlpha = animatedAlpha,
                animatedScale = animatedScale,
                onSelectItem = onSelectItem
            )
        }
    }
}

@Composable
private fun <T> SatelliteItemRow(
    item: ArcActionItem<T>,
    position: ArcItemPosition,
    anchorPosition: Offset,
    isHovered: Boolean,
    animatedAlpha: Float,
    animatedScale: Float,
    onSelectItem: (T) -> Unit
) {
    var itemSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    val buttonRadiusPx = with(density) { 23.dp.toPx() } // Half of 46.dp

    val itemHoverScale by animateFloatAsState(
        targetValue = if (isHovered) 1.25f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "itemHoverScale"
    )

    val targetX = anchorPosition.x + position.offset.x
    val targetY = anchorPosition.y + position.offset.y

    val offsetX = if (itemSize.width > 0) {
        if (position.badgeOnLeft) {
            targetX - (itemSize.width - buttonRadiusPx)
        } else {
            targetX - buttonRadiusPx
        }
    } else {
        targetX - buttonRadiusPx
    }

    val offsetY = if (itemSize.height > 0) {
        targetY - itemSize.height / 2f
    } else {
        targetY - buttonRadiusPx
    }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = offsetX.roundToInt(),
                    y = offsetY.roundToInt()
                )
            }
            .onSizeChanged { itemSize = it }
            .graphicsLayer {
                scaleX = animatedScale * itemHoverScale
                scaleY = animatedScale * itemHoverScale
                alpha = animatedAlpha
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (position.badgeOnLeft) {
                SatelliteBadge(
                    label = item.label,
                    isHovered = isHovered,
                    activeColor = item.activeColor,
                    onClick = { onSelectItem(item.id) }
                )
                SatelliteButton(
                    icon = item.icon,
                    contentDescription = item.contentDescription,
                    isHovered = isHovered,
                    activeColor = item.activeColor,
                    onClick = { onSelectItem(item.id) }
                )
            } else {
                SatelliteButton(
                    icon = item.icon,
                    contentDescription = item.contentDescription,
                    isHovered = isHovered,
                    activeColor = item.activeColor,
                    onClick = { onSelectItem(item.id) }
                )
                SatelliteBadge(
                    label = item.label,
                    isHovered = isHovered,
                    activeColor = item.activeColor,
                    onClick = { onSelectItem(item.id) }
                )
            }
        }
    }
}

@Composable
private fun SatelliteButton(
    icon: ImageVector,
    contentDescription: String,
    isHovered: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isHovered) activeColor else Color.White
    val borderWidth = if (isHovered) 3.5.dp else 2.5.dp
    val shadowOffset = if (isHovered) 4.dp else 2.5.dp

    Box(
        modifier = modifier
            .size(46.dp)
            .neobrutalistShadow(
                shadowColor = Color.Black,
                borderColor = Color.Black,
                borderWidth = borderWidth,
                shadowOffset = shadowOffset,
                shape = CircleShape
            )
            .background(backgroundColor, CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
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

@Composable
private fun SatelliteBadge(
    label: String,
    isHovered: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isHovered) activeColor else Color.White

    Box(
        modifier = modifier
            .neobrutalistShadow(
                shadowColor = Color.Black,
                borderColor = Color.Black,
                borderWidth = 2.dp,
                shadowOffset = 2.dp,
                shape = RoundedCornerShape(6.dp)
            )
            .background(backgroundColor, RoundedCornerShape(6.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            letterSpacing = 0.5.sp
        )
    }
}
