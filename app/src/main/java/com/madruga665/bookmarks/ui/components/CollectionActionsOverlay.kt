package com.madruga665.bookmarks.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import kotlin.math.roundToInt

enum class CollectionOption {
    EDIT,
    SHARE,
    DELETE
}

@Composable
fun CollectionActionsOverlay(
    collection: CollectionEntity?,
    cardOffset: Offset?,
    cardSize: IntSize?,
    touchPositionInWindow: Offset?,
    dragPositionInWindow: Offset? = null,
    onHoveredOptionChange: (CollectionOption?) -> Unit,
    hoveredOption: CollectionOption? = null,
    onSelectItem: ((CollectionOption) -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = collection != null && cardOffset != null && cardSize != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        if (collection == null || cardOffset == null || cardSize == null) return@AnimatedVisibility

        val cardWidthDp = with(density) { cardSize.width.toDp() }
        val anchorPosition = touchPositionInWindow ?: Offset(
            x = cardOffset.x + cardSize.width / 2f,
            y = cardOffset.y + cardSize.height / 2f
        )

        val editLabel = stringResource(R.string.collection_action_edit_short)
        val editDesc = stringResource(R.string.collection_action_edit)
        val shareLabel = stringResource(R.string.collection_action_share_short)
        val shareDesc = stringResource(R.string.collection_action_share)
        val deleteLabel = stringResource(R.string.collection_action_delete_short)
        val deleteDesc = stringResource(R.string.collection_action_delete)
        val accentYellow = NeobrutalismTheme.colors.accentYellow
        val accentBlue = NeobrutalismTheme.colors.accentBlue
        val accentOrange = NeobrutalismTheme.colors.accentOrange

        val items = remember(
            editLabel, editDesc,
            shareLabel, shareDesc,
            deleteLabel, deleteDesc,
            accentYellow, accentBlue, accentOrange,
            onSelectItem
        ) {
            listOf(
                ArcActionItem(
                    id = CollectionOption.EDIT,
                    icon = Icons.Outlined.Edit,
                    label = editLabel,
                    contentDescription = editDesc,
                    activeColor = accentYellow,
                    onClick = { onSelectItem?.invoke(CollectionOption.EDIT) }
                ),
                ArcActionItem(
                    id = CollectionOption.SHARE,
                    icon = Icons.Outlined.Share,
                    label = shareLabel,
                    contentDescription = shareDesc,
                    activeColor = accentBlue,
                    onClick = { onSelectItem?.invoke(CollectionOption.SHARE) }
                ),
                ArcActionItem(
                    id = CollectionOption.DELETE,
                    icon = Icons.Outlined.Delete,
                    label = deleteLabel,
                    contentDescription = deleteDesc,
                    activeColor = accentOrange,
                    onClick = { onSelectItem?.invoke(CollectionOption.DELETE) }
                )
            )
        }

        Box(modifier = modifier.fillMaxSize()) {
            // Render the highlighted NeobrutalistFolderCard at cardOffset with width cardSize.width
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
                NeobrutalistFolderCard(
                    collection = collection,
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
