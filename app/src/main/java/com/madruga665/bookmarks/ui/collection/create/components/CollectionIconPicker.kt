package com.madruga665.bookmarks.ui.collection.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow
import com.madruga665.bookmarks.ui.utils.CollectionIconItem
import com.madruga665.bookmarks.ui.utils.CollectionIconRegistry

/**
 * 8-column icon picker grid for selecting a collection / folder icon.
 * Highlights the active selected icon with the selected color fill, a 2.5dp border, and an offset shadow.
 */
@Composable
fun CollectionIconPicker(
    selectedIconKey: String,
    selectedColor: Color,
    onIconSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    icons: List<CollectionIconItem> = CollectionIconRegistry.icons
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tag_collection_icon_picker")
    ) {
        Text(
            text = stringResource(R.string.collection_create_icon_label),
            style = NeobrutalismTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            ),
            color = NeobrutalismTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        val rows = icons.chunked(8)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rows.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { item ->
                        val isSelected = item.key.equals(selectedIconKey, ignoreCase = true)
                        IconTile(
                            item = item,
                            isSelected = isSelected,
                            selectedColor = selectedColor,
                            onClick = { onIconSelect(item.key) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Fill remaining columns in incomplete rows to keep item sizes uniform
                    val remaining = 8 - rowItems.size
                    repeat(remaining) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun IconTile(
    item: CollectionIconItem,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)

    val tileModifier = if (isSelected) {
        Modifier
            .neobrutalistShadow(
                shadowColor = NeobrutalismTheme.colors.shadow,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.5.dp,
                shadowOffset = 2.dp,
                shape = shape
            )
            .background(selectedColor, shape)
    } else {
        Modifier
            .border(
                width = 1.5.dp,
                color = NeobrutalismTheme.colors.border,
                shape = shape
            )
            .background(NeobrutalismTheme.colors.surface, shape)
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .then(tileModifier)
            .testTag("tag_icon_tile_${item.key}")
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        val iconTint = if (isSelected) {
            if (selectedColor.luminance() > 0.45f) Color.Black else Color.White
        } else {
            NeobrutalismTheme.colors.onSurface
        }

        Icon(
            imageVector = item.icon,
            contentDescription = item.key,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
    }
}
