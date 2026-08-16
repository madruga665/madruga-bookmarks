package com.madruga665.bookmarks.ui.bookmark.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme

@Composable
fun BookmarkDescriptionSection(
    description: String?,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (description.isNullOrBlank()) return

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.bookmark_description_heading),
            style = NeobrutalismTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            ),
            color = NeobrutalismTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = description,
            style = NeobrutalismTheme.typography.bodyMedium.copy(
                lineHeight = 20.sp,
                fontSize = 14.sp
            ),
            color = NeobrutalismTheme.colors.onSurface,
            maxLines = if (isExpanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis
        )

        if (description.length > 120) {
            Text(
                text = if (isExpanded) stringResource(R.string.bookmark_show_less) else stringResource(R.string.bookmark_show_more),
                style = NeobrutalismTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                color = NeobrutalismTheme.colors.accentPurple,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable { onToggleExpand() }
            )
        }
    }
}
