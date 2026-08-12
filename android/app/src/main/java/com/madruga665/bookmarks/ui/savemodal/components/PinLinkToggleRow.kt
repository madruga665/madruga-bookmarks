package com.madruga665.bookmarks.ui.savemodal.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme

@Composable
fun PinLinkToggleRow(
    isPinned: Boolean,
    onTogglePin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onTogglePin)
            .padding(vertical = 10.dp)
            .testTag("tag_pin_link_toggle"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.PushPin,
            contentDescription = "Pin link",
            tint = if (isPinned) NeobrutalismTheme.colors.accentOrange else NeobrutalismTheme.colors.onSurface,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = "Pin this link",
            style = NeobrutalismTheme.typography.bodyMedium.copy(
                fontWeight = if (isPinned) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp
            ),
            color = NeobrutalismTheme.colors.onSurface,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}
