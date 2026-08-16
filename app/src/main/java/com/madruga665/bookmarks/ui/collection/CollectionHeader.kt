package com.madruga665.bookmarks.ui.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AddLink
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme

@Composable
fun CollectionHeader(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    onAddLinkClick: () -> Unit,
    onOptionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Top-left: Back Button (arrow left icon inside rounded 12.dp box with 2.5.dp black border, white fill)
        NeobrutalistButton(
            onClick = onBackClick,
            shape = RoundedCornerShape(12.dp),
            containerColor = NeobrutalismTheme.colors.surface,
            borderColor = NeobrutalismTheme.colors.border,
            borderWidth = 2.5.dp,
            shadowOffset = 4.dp,
            modifier = Modifier.testTag("tag_collection_header_back")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = NeobrutalismTheme.colors.onSurface,
                modifier = Modifier.size(22.dp)
            )
        }

        // Center: Collection Title and Subtitle
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = NeobrutalismTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = NeobrutalismTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                style = NeobrutalismTheme.typography.bodySmall,
                color = NeobrutalismTheme.colors.subtext,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }

        // Top-right: Action buttons (Quick Add link + More options)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Quick Add link button (link icon inside rounded 12.dp box with 2.5.dp black border, yellow fill)
            NeobrutalistButton(
                onClick = onAddLinkClick,
                shape = RoundedCornerShape(12.dp),
                containerColor = NeobrutalismTheme.colors.accentYellow,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                modifier = Modifier.testTag("tag_collection_header_add_link")
            ) {
                Icon(
                    imageVector = Icons.Outlined.AddLink,
                    contentDescription = stringResource(R.string.collection_empty_add),
                    tint = NeobrutalismTheme.colors.border,
                    modifier = Modifier.size(22.dp)
                )
            }

            // More options button (three vertical dots icon inside rounded 12.dp box with 2.5.dp black border, white fill)
            NeobrutalistButton(
                onClick = onOptionsClick,
                shape = RoundedCornerShape(12.dp),
                containerColor = NeobrutalismTheme.colors.surface,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                modifier = Modifier.testTag("tag_collection_header_options")
            ) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = stringResource(R.string.collection_options_tooltip),
                    tint = NeobrutalismTheme.colors.onSurface,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
