package com.madruga665.bookmarks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.components.NeobrutalistTextField
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow
import com.madruga665.bookmarks.ui.utils.CollectionPalette

@Composable
fun EditCollectionDialog(
    collection: CollectionEntity?,
    onDismiss: () -> Unit,
    onConfirmSave: (id: String, name: String, colorAccent: String, iconKey: String) -> Unit
) {
    if (collection == null) return

    var name by remember(collection) { mutableStateOf(collection.name) }
    var selectedColor by remember(collection) { mutableStateOf(collection.colorAccent.uppercase()) }
    var selectedIcon by remember(collection) { mutableStateOf(collection.iconKey.lowercase()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val emptyErrorMessage = stringResource(R.string.collection_edit_error_empty)

    val colors = listOf("YELLOW", "PURPLE", "ORANGE", "BLUE")
    val icons = listOf("code" to Icons.Outlined.Code, "work" to Icons.Outlined.WorkOutline, "folder" to Icons.Outlined.Folder)

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .neobrutalistShadow(
                    shadowColor = NeobrutalismTheme.colors.shadow,
                    borderColor = NeobrutalismTheme.colors.border,
                    borderWidth = 2.5.dp,
                    shadowOffset = 6.dp,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(NeobrutalismTheme.colors.surface, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.collection_edit_dialog_title),
                    style = NeobrutalismTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = NeobrutalismTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Name Input Field
                Text(
                    text = stringResource(R.string.collection_edit_name_label),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeobrutalismTheme.colors.subtext
                )
                Spacer(modifier = Modifier.height(6.dp))

                NeobrutalistTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    placeholderText = stringResource(R.string.collection_edit_name_hint),
                    onPasteClick = {}
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = errorMessage!!,
                        color = NeobrutalismTheme.colors.accentOrange,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Accent Color Selector
                Text(
                    text = stringResource(R.string.collection_edit_color_label),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeobrutalismTheme.colors.subtext
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    colors.forEach { colorToken ->
                        val color = CollectionPalette.getColor(colorToken)
                        val isSelected = selectedColor == colorToken

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .neobrutalistShadow(
                                    shadowColor = Color.Black,
                                    borderColor = Color.Black,
                                    borderWidth = if (isSelected) 3.dp else 2.dp,
                                    shadowOffset = if (isSelected) 3.dp else 1.dp,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(color, RoundedCornerShape(8.dp))
                                .clickable { selectedColor = colorToken }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Icon Key Selector
                Text(
                    text = stringResource(R.string.collection_edit_icon_label),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeobrutalismTheme.colors.subtext
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    icons.forEach { (iconKey, vector) ->
                        val isSelected = selectedIcon == iconKey
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .neobrutalistShadow(
                                    shadowColor = Color.Black,
                                    borderColor = Color.Black,
                                    borderWidth = if (isSelected) 3.dp else 2.dp,
                                    shadowOffset = if (isSelected) 3.dp else 1.dp,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    if (isSelected) NeobrutalismTheme.colors.accentYellow else Color.White,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedIcon = iconKey },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = vector,
                                contentDescription = iconKey,
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons Row
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NeobrutalistButton(
                        text = stringResource(R.string.dialog_cancel),
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    NeobrutalistButton(
                        text = stringResource(R.string.dialog_save),
                        onClick = {
                            if (name.isBlank()) {
                                errorMessage = emptyErrorMessage
                            } else {
                                onConfirmSave(collection.id, name.trim(), selectedColor, selectedIcon)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
