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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.WorkOutline
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NeobrutalistFolderCard(
    collection: CollectionEntity,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    isActiveMenu: Boolean = false,
    touchPositionInWindow: Offset? = null,
    onHoveredOptionChange: (CollectionOption?) -> Unit = {},
    onLongPressStart: ((CollectionEntity, Offset, Offset, IntSize) -> Unit)? = null,
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

    var editRect by remember { mutableStateOf<Rect?>(null) }
    var shareRect by remember { mutableStateOf<Rect?>(null) }
    var deleteRect by remember { mutableStateOf<Rect?>(null) }


    val hoveredOption = remember(touchPositionInWindow, editRect, shareRect, deleteRect, isActiveMenu) {
        if (!isActiveMenu) null
        else {
            val pos = touchPositionInWindow ?: return@remember null
            val hitPadding = 40f
            when {
                editRect?.inflate(hitPadding)?.contains(pos) == true -> CollectionOption.EDIT
                shareRect?.inflate(hitPadding)?.contains(pos) == true -> CollectionOption.SHARE
                deleteRect?.inflate(hitPadding)?.contains(pos) == true -> CollectionOption.DELETE
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

    val tabColor = when (collection.colorAccent.uppercase()) {
        "YELLOW" -> NeobrutalismTheme.colors.accentYellow
        "PURPLE" -> NeobrutalismTheme.colors.accentPurple
        "ORANGE" -> NeobrutalismTheme.colors.accentOrange
        "BLUE" -> NeobrutalismTheme.colors.accentBlue
        else -> NeobrutalismTheme.colors.accentYellow
    }

    val iconVector: ImageVector = when (collection.iconKey.lowercase()) {
        "code", "programacao" -> Icons.Outlined.Code
        "work", "vagas" -> Icons.Outlined.WorkOutline
        else -> Icons.Outlined.Code
    }

    val gestureModifier = if (onLongPressStart != null) {
        Modifier.pointerInput(collection.id) {
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
                        onLongPressStart(collection, touchInWindow, cardWindowOffset, cardSize)
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

    Box(
        modifier = modifier
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(gestureModifier)
        ) {
            // Colored Top Tab Header
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(18.dp)
                    .neobrutalistShadow(
                        shadowColor = NeobrutalismTheme.colors.shadow,
                        borderColor = NeobrutalismTheme.colors.border,
                        borderWidth = 2.5.dp,
                        shadowOffset = 2.dp,
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
                    .background(tabColor, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )

            // Main Folder Card Body
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .neobrutalistShadow(
                        shadowColor = NeobrutalismTheme.colors.shadow,
                        borderColor = NeobrutalismTheme.colors.border,
                        borderWidth = 2.5.dp,
                        shadowOffset = 4.dp,
                        shape = RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
                    )
                    .background(NeobrutalismTheme.colors.surface, RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        // Internal Colored Icon Box
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .neobrutalistShadow(
                                    shadowColor = NeobrutalismTheme.colors.shadow,
                                    borderColor = NeobrutalismTheme.colors.border,
                                    borderWidth = 2.dp,
                                    shadowOffset = 2.dp,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(tabColor, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = iconVector,
                                contentDescription = collection.name,
                                tint = Color.Black,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        // Link Count Subtext
                        Text(
                            text = stringResource(R.string.collection_links_count, collection.linkCount),
                            fontSize = 12.sp,
                            color = NeobrutalismTheme.colors.subtext,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Collection Title
                    Text(
                        text = collection.name,
                        style = NeobrutalismTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = NeobrutalismTheme.colors.onSurface
                    )
                }
            }
        }

        // Inline Floating Action Buttons when menu is active
        if (isActiveMenu) {
            if (isRightColumn) {
                // Folder is on the RIGHT column -> pop out to the LEFT of the folder card
                InlineFloatingActionButtonItem(
                    icon = Icons.Outlined.Edit,
                    labelText = stringResource(R.string.collection_action_edit_short),
                    contentDescription = stringResource(R.string.collection_action_edit),
                    isHovered = hoveredOption == CollectionOption.EDIT,
                    activeColor = NeobrutalismTheme.colors.accentYellow,
                    textOnLeft = true,
                    onPositioned = { editRect = it },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = (-40).dp, y = (-16).dp)
                )

                InlineFloatingActionButtonItem(
                    icon = Icons.Outlined.Share,
                    labelText = stringResource(R.string.collection_action_share_short),
                    contentDescription = stringResource(R.string.collection_action_share),
                    isHovered = hoveredOption == CollectionOption.SHARE,
                    activeColor = NeobrutalismTheme.colors.accentBlue,
                    textOnLeft = true,
                    onPositioned = { shareRect = it },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = (-48).dp, y = 8.dp)
                )

                InlineFloatingActionButtonItem(
                    icon = Icons.Outlined.Delete,
                    labelText = stringResource(R.string.collection_action_delete_short),
                    contentDescription = stringResource(R.string.collection_action_delete),
                    isHovered = hoveredOption == CollectionOption.DELETE,
                    activeColor = NeobrutalismTheme.colors.accentOrange,
                    textOnLeft = true,
                    onPositioned = { deleteRect = it },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (-40).dp, y = 20.dp)
                )
            } else {
                // Folder is on the LEFT column -> pop out to the RIGHT of the folder card
                InlineFloatingActionButtonItem(
                    icon = Icons.Outlined.Edit,
                    labelText = stringResource(R.string.collection_action_edit_short),
                    contentDescription = stringResource(R.string.collection_action_edit),
                    isHovered = hoveredOption == CollectionOption.EDIT,
                    activeColor = NeobrutalismTheme.colors.accentYellow,
                    textOnLeft = false,
                    onPositioned = { editRect = it },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 40.dp, y = (-16).dp)
                )

                InlineFloatingActionButtonItem(
                    icon = Icons.Outlined.Share,
                    labelText = stringResource(R.string.collection_action_share_short),
                    contentDescription = stringResource(R.string.collection_action_share),
                    isHovered = hoveredOption == CollectionOption.SHARE,
                    activeColor = NeobrutalismTheme.colors.accentBlue,
                    textOnLeft = false,
                    onPositioned = { shareRect = it },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = 48.dp, y = 8.dp)
                )

                InlineFloatingActionButtonItem(
                    icon = Icons.Outlined.Delete,
                    labelText = stringResource(R.string.collection_action_delete_short),
                    contentDescription = stringResource(R.string.collection_action_delete),
                    isHovered = hoveredOption == CollectionOption.DELETE,
                    activeColor = NeobrutalismTheme.colors.accentOrange,
                    textOnLeft = false,
                    onPositioned = { deleteRect = it },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 40.dp, y = 20.dp)
                )
            }
        }
    }
}

@Composable
private fun InlineFloatingActionButtonItem(
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


