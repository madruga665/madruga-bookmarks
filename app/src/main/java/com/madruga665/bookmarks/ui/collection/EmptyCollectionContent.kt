package com.madruga665.bookmarks.ui.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@Composable
fun EmptyCollectionContent(
    onAddLinkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tag_empty_collection_container")
            .neobrutalistShadow(
                shadowColor = NeobrutalismTheme.colors.shadow,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = NeobrutalismTheme.colors.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon Badge Box Container with Neobrutalist border
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .neobrutalistShadow(
                        shadowColor = NeobrutalismTheme.colors.shadow,
                        borderColor = NeobrutalismTheme.colors.border,
                        borderWidth = 2.5.dp,
                        shadowOffset = 3.dp,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .background(
                        color = NeobrutalismTheme.colors.accentYellow,
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = stringResource(R.string.collection_empty_title),
                    tint = NeobrutalismTheme.colors.border,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = stringResource(R.string.collection_empty_title),
                style = NeobrutalismTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = NeobrutalismTheme.colors.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = stringResource(R.string.collection_empty_desc),
                style = NeobrutalismTheme.typography.bodyMedium,
                color = NeobrutalismTheme.colors.subtext,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // CTA Button with yellow accent fill
            NeobrutalistButton(
                text = stringResource(R.string.collection_empty_add),
                onClick = onAddLinkClick,
                containerColor = NeobrutalismTheme.colors.accentYellow,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                modifier = Modifier.testTag("tag_empty_collection_add_button")
            )
        }
    }
}
