package com.madruga665.bookmarks.ui.bookmark.components

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
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@Composable
fun BookmarkUrlCard(
    url: String,
    onUrlClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .neobrutalistShadow(
                shadowColor = NeobrutalismTheme.colors.shadow,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.dp,
                shadowOffset = 3.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .background(NeobrutalismTheme.colors.surface, RoundedCornerShape(12.dp))
            .clickable { onUrlClick(url) }
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = url,
                style = NeobrutalismTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                color = NeobrutalismTheme.colors.accentPurple,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                contentDescription = stringResource(R.string.bookmark_url_card_open),
                tint = NeobrutalismTheme.colors.onSurface,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
