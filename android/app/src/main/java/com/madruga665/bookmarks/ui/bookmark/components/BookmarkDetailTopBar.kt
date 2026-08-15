package com.madruga665.bookmarks.ui.bookmark.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DriveFileMove
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@Composable
fun BookmarkDetailTopBar(
    platformBadge: String,
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onShareClick: () -> Unit,
    onMoveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NeobrutalistButton(
                onClick = onBackClick,
                modifier = Modifier.size(38.dp),
                shape = RoundedCornerShape(8.dp),
                borderWidth = 2.dp,
                shadowOffset = 2.dp
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Voltar",
                    tint = NeobrutalismTheme.colors.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }

            Box(
                modifier = Modifier
                    .neobrutalistShadow(
                        shadowColor = NeobrutalismTheme.colors.shadow,
                        borderColor = NeobrutalismTheme.colors.border,
                        borderWidth = 2.dp,
                        shadowOffset = 2.dp,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(NeobrutalismTheme.colors.surface, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = platformBadge.uppercase(),
                    style = NeobrutalismTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    ),
                    color = NeobrutalismTheme.colors.onSurface
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NeobrutalistButton(
                onClick = onRefreshClick,
                modifier = Modifier.size(38.dp),
                shape = RoundedCornerShape(8.dp),
                borderWidth = 2.dp,
                shadowOffset = 2.dp
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = "Atualizar Metadados",
                    tint = NeobrutalismTheme.colors.onSurface,
                    modifier = Modifier.size(18.dp)
                )
            }

            NeobrutalistButton(
                onClick = onShareClick,
                modifier = Modifier.size(38.dp),
                shape = RoundedCornerShape(8.dp),
                borderWidth = 2.dp,
                shadowOffset = 2.dp
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Compartilhar",
                    tint = NeobrutalismTheme.colors.onSurface,
                    modifier = Modifier.size(18.dp)
                )
            }

            NeobrutalistButton(
                onClick = onMoveClick,
                modifier = Modifier.size(38.dp),
                shape = RoundedCornerShape(8.dp),
                borderWidth = 2.dp,
                shadowOffset = 2.dp
            ) {
                Icon(
                    imageVector = Icons.Outlined.DriveFileMove,
                    contentDescription = "Mover Coleção",
                    tint = NeobrutalismTheme.colors.onSurface,
                    modifier = Modifier.size(18.dp)
                )
            }

            NeobrutalistButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(38.dp),
                containerColor = Color(0xFFFF4B4B),
                shape = RoundedCornerShape(8.dp),
                borderWidth = 2.dp,
                shadowOffset = 2.dp
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Excluir",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
