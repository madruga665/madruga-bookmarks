package com.madruga665.bookmarks.ui.savemodal.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.components.NeobrutalistTextField
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@Composable
fun InlineCreateFolderForm(
    folderNameInput: String,
    onFolderNameChange: (String) -> Unit,
    selectedColorAccent: String,
    onColorAccentSelect: (String) -> Unit,
    onCreateFolderSubmit: () -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    val availableColors = listOf(
        "YELLOW" to NeobrutalismTheme.colors.accentYellow,
        "PURPLE" to NeobrutalismTheme.colors.accentPurple,
        "ORANGE" to NeobrutalismTheme.colors.accentOrange,
        "BLUE" to NeobrutalismTheme.colors.accentBlue
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tag_inline_create_folder_form")
            .neobrutalistShadow(
                shadowColor = NeobrutalismTheme.colors.shadow,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .background(NeobrutalismTheme.colors.surface, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Create New Collection",
                style = NeobrutalismTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = NeobrutalismTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            NeobrutalistTextField(
                value = folderNameInput,
                onValueChange = onFolderNameChange,
                placeholderText = "Folder name (e.g. Design)",
                onPasteClick = {}
            )

            if (error != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = error,
                    color = NeobrutalismTheme.colors.accentOrange,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Color Accent Selection Circles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Color:",
                    fontSize = 14.sp,
                    color = NeobrutalismTheme.colors.onSurface,
                    fontWeight = FontWeight.Medium
                )

                availableColors.forEach { (colorName, colorValue) ->
                    val isSelected = selectedColorAccent.equals(colorName, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .neobrutalistShadow(
                                shadowColor = NeobrutalismTheme.colors.shadow,
                                borderColor = NeobrutalismTheme.colors.border,
                                borderWidth = if (isSelected) 3.dp else 1.5.dp,
                                shadowOffset = if (isSelected) 2.dp else 1.dp,
                                shape = CircleShape
                            )
                            .background(colorValue, CircleShape)
                            .clickable { onColorAccentSelect(colorName) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            NeobrutalistButton(
                onClick = onCreateFolderSubmit,
                containerColor = NeobrutalismTheme.colors.accentYellow,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tag_create_folder_submit")
            ) {
                Text(
                    text = "Create Folder",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}
