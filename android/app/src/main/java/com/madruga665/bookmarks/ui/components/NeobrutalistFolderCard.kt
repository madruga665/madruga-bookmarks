package com.madruga665.bookmarks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@Composable
fun NeobrutalistFolderCard(
    collection: CollectionEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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

    Column(
        modifier = modifier.clickable(onClick = onClick)
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
                        text = "${collection.linkCount} links",
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
}
