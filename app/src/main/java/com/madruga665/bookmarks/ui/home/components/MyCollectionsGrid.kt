package com.madruga665.bookmarks.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.ui.components.CollectionOption
import com.madruga665.bookmarks.ui.components.NeobrutalistFolderCard
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme

@Composable
fun MyCollectionsGrid(
    collections: List<CollectionEntity>,
    onCollectionClick: (String) -> Unit,
    activeMenuCollection: CollectionEntity? = null,
    touchPositionInWindow: Offset? = null,
    onHoveredOptionChange: (CollectionOption?) -> Unit = {},
    onCollectionLongClick: ((CollectionEntity) -> Unit)? = null,
    onLongPressStart: ((CollectionEntity, Offset, Offset, IntSize) -> Unit)? = null,
    onLongPressDrag: ((Offset) -> Unit)? = null,
    onLongPressRelease: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.home_my_collections),
            style = NeobrutalismTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = NeobrutalismTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 2-Column Grid Layout for Folder Cards
        val rows = collections.chunked(2)
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                rowItems.forEach { collection ->
                    val isActive = activeMenuCollection?.id == collection.id
                    NeobrutalistFolderCard(
                        collection = collection,
                        onClick = { onCollectionClick(collection.id) },
                        onLongClick = if (onCollectionLongClick != null) {
                            { onCollectionLongClick(collection) }
                        } else null,
                        isActiveMenu = isActive,
                        touchPositionInWindow = if (isActive) touchPositionInWindow else null,
                        onHoveredOptionChange = if (isActive) onHoveredOptionChange else { _ -> },
                        onLongPressStart = onLongPressStart,
                        onLongPressDrag = onLongPressDrag,
                        onLongPressRelease = onLongPressRelease,
                        modifier = Modifier
                            .weight(1f)
                            .graphicsLayer {
                                alpha = if (isActive) 0f else 1f
                            }
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
