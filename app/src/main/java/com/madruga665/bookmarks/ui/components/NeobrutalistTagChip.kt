package com.madruga665.bookmarks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow
import com.madruga665.bookmarks.ui.utils.TagPalette

/**
 * A Neobrutalism styled tag chip supporting badge, selectable, and removable states.
 *
 * @param tag Tag name to display
 * @param modifier Composable modifier
 * @param backgroundColor Background color for the tag
 * @param isSelected Whether the chip is actively selected in a filter bar
 * @param showHash Whether to prefix the tag with '#' if not already present
 * @param onTagClick Optional callback when the tag chip is tapped
 * @param onRemoveClick Optional callback when the remove icon ('X') is tapped
 */
@Composable
fun NeobrutalistTagChip(
    tag: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = TagPalette.getTagColor(tag),
    isSelected: Boolean = false,
    showHash: Boolean = true,
    onTagClick: (() -> Unit)? = null,
    onRemoveClick: (() -> Unit)? = null
) {
    val cleanTag = tag.removePrefix("#").lowercase()
    val displayText = if (showHash) {
        if (tag.startsWith("#")) tag else "#$tag"
    } else {
        tag.removePrefix("#")
    }

    val shape = RoundedCornerShape(8.dp)
    val isBadge = onTagClick == null && onRemoveClick == null

    val chipModifier = modifier
        .testTag("tag_chip_$cleanTag")
        .then(
            if (isSelected) {
                Modifier
                    .neobrutalistShadow(
                        shadowColor = NeobrutalismTheme.colors.shadow,
                        borderColor = NeobrutalismTheme.colors.border,
                        borderWidth = 2.dp,
                        shadowOffset = 2.dp,
                        shape = shape
                    )
                    .background(backgroundColor, shape)
            } else {
                val bg = if (onTagClick != null) backgroundColor.copy(alpha = 0.65f) else backgroundColor
                Modifier
                    .border(1.5.dp, NeobrutalismTheme.colors.border, shape)
                    .background(bg, shape)
            }
        )
        .then(
            if (onTagClick != null) {
                Modifier.clickable(onClick = onTagClick)
            } else {
                Modifier
            }
        )

    val horizontalPadding = if (isBadge) 6.dp else 8.dp
    val verticalPadding = if (isBadge) 2.dp else 4.dp

    Row(
        modifier = chipModifier.padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = displayText,
            fontSize = if (isBadge) 11.sp else 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        if (onRemoveClick != null) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(R.string.tag_remove_desc, tag),
                modifier = Modifier
                    .size(14.dp)
                    .clickable { onRemoveClick() },
                tint = Color.Black
            )
        }
    }
}
