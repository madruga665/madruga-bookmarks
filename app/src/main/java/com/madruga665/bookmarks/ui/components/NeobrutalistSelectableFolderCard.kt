package com.madruga665.bookmarks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow
import com.madruga665.bookmarks.ui.utils.CollectionIconRegistry
import com.madruga665.bookmarks.ui.utils.CollectionPalette

@Composable
fun NeobrutalistSelectableFolderCard(
    collection: CollectionEntity,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBackgroundColor = if (isSelected) {
        NeobrutalismTheme.colors.accentYellow
    } else {
        NeobrutalismTheme.colors.surface
    }

    val iconBoxColor = CollectionPalette.getColor(collection.colorAccent)

    val iconVector: ImageVector = CollectionIconRegistry.getIcon(collection.iconKey)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tag_selectable_folder_${collection.id}")
            .neobrutalistShadow(
                shadowColor = NeobrutalismTheme.colors.shadow,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.5.dp,
                shadowOffset = if (isSelected) 2.dp else 4.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .background(cardBackgroundColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onSelect)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Folder Icon Container Square
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .neobrutalistShadow(
                            shadowColor = NeobrutalismTheme.colors.shadow,
                            borderColor = NeobrutalismTheme.colors.border,
                            borderWidth = 2.dp,
                            shadowOffset = 2.dp,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(iconBoxColor, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = collection.name,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = collection.name,
                    style = NeobrutalismTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }

            // Selection Checkmark Icon
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
