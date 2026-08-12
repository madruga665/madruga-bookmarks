package com.madruga665.bookmarks.ui.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.components.NeobrutalistTextField
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme

@Composable
fun QuickSaveBar(
    urlValue: String,
    onUrlChange: (String) -> Unit,
    onPasteClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Quick Save Input Field
        NeobrutalistTextField(
            value = urlValue,
            onValueChange = onUrlChange,
            placeholderText = "Paste link to quick save",
            onPasteClick = onPasteClick,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Yellow/Accent Add (+) Action Button
        NeobrutalistButton(
            onClick = onAddClick,
            containerColor = NeobrutalismTheme.colors.accentYellow,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.testTag("tag_quick_save_add_btn")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Link",
                tint = NeobrutalismTheme.colors.border,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
