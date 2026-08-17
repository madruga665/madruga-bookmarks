package com.madruga665.bookmarks.ui.collection.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
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
import com.madruga665.bookmarks.ui.utils.CollectionColorItem
import com.madruga665.bookmarks.ui.utils.CollectionPalette

/**
 * 16-color Neobrutalism palette selector arranged in a responsive 8-column grid (2 rows of 8).
 * Selected color displays an outline checkmark icon and 2.dp offset shadow.
 */
@Composable
fun CollectionColorPicker(
    selectedColorHex: String,
    onColorSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    colors: List<CollectionColorItem> = CollectionPalette.colors
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tag_collection_color_picker"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.collection_create_color_label),
            style = NeobrutalismTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            ),
            color = NeobrutalismTheme.colors.onSurface
        )

        colors.chunked(8).forEach { rowColors ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                rowColors.forEach { colorItem ->
                    val isSelected = isColorSelected(colorItem, selectedColorHex)
                    ColorSwatch(
                        colorItem = colorItem,
                        isSelected = isSelected,
                        onSelect = { onColorSelect(colorItem.hex) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorSwatch(
    colorItem: CollectionColorItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val swatchShape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .size(36.dp)
            .testTag("tag_color_swatch_${colorItem.id}")
            .neobrutalistShadow(
                shadowColor = NeobrutalismTheme.colors.shadow,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.5.dp,
                shadowOffset = if (isSelected) 2.dp else 0.dp,
                shape = swatchShape
            )
            .background(colorItem.color, swatchShape)
            .clickable(onClick = onSelect),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = if (colorItem.color.luminance() > 0.45f) Color.Black else Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

private fun isColorSelected(colorItem: CollectionColorItem, selectedColorHex: String): Boolean {
    val cleanSelected = selectedColorHex.trim()
    val cleanSelectedWithHash = if (cleanSelected.startsWith("#")) cleanSelected else "#$cleanSelected"
    return colorItem.hex.equals(cleanSelectedWithHash, ignoreCase = true) ||
        colorItem.id.equals(cleanSelected, ignoreCase = true) ||
        colorItem.hex.removePrefix("#").equals(cleanSelected.removePrefix("#"), ignoreCase = true)
}
