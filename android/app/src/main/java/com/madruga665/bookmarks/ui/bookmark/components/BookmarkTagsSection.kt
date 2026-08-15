package com.madruga665.bookmarks.ui.bookmark.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookmarkTagsSection(
    tags: String,
    onOpenAddTagDialog: () -> Unit,
    onRemoveTag: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tagList = remember(tags) {
        if (tags.isBlank()) {
            emptyList()
        } else {
            tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "TAGS",
            style = NeobrutalismTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            ),
            color = NeobrutalismTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tagList.forEach { tag ->
                TagChip(
                    tag = tag,
                    onRemove = { onRemoveTag(tag) }
                )
            }

            // + Add Tag Button
            Box(
                modifier = Modifier
                    .neobrutalistShadow(
                        shadowColor = NeobrutalismTheme.colors.shadow,
                        borderColor = NeobrutalismTheme.colors.border,
                        borderWidth = 2.dp,
                        shadowOffset = 2.dp,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(NeobrutalismTheme.colors.accentYellow, RoundedCornerShape(8.dp))
                    .clickable(onClick = onOpenAddTagDialog)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar Tag",
                        tint = NeobrutalismTheme.colors.border,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add",
                        style = NeobrutalismTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        color = NeobrutalismTheme.colors.border
                    )
                }
            }
        }
    }
}

@Composable
fun TagChip(
    tag: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .neobrutalistShadow(
                shadowColor = NeobrutalismTheme.colors.shadow,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.dp,
                shadowOffset = 2.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .background(NeobrutalismTheme.colors.surface, RoundedCornerShape(8.dp))
            .padding(start = 10.dp, end = 6.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tag,
                style = NeobrutalismTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = NeobrutalismTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clickable(onClick = onRemove),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remover tag $tag",
                    tint = NeobrutalismTheme.colors.subtext,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun AddTagDialog(
    tagInput: String,
    onTagInputChange: (String) -> Unit,
    onSaveTag: () -> Unit,
    onDismiss: () -> Unit
) {
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
                    text = "Adicionar Tag",
                    style = NeobrutalismTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = NeobrutalismTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .neobrutalistShadow(
                            shadowColor = NeobrutalismTheme.colors.shadow,
                            borderColor = NeobrutalismTheme.colors.border,
                            borderWidth = 2.dp,
                            shadowOffset = 2.dp,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .background(NeobrutalismTheme.colors.background, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    if (tagInput.isEmpty()) {
                        Text(
                            text = "Digite o nome da tag...",
                            style = NeobrutalismTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp
                            ),
                            color = NeobrutalismTheme.colors.subtext
                        )
                    }
                    BasicTextField(
                        value = tagInput,
                        onValueChange = onTagInputChange,
                        singleLine = true,
                        textStyle = NeobrutalismTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = NeobrutalismTheme.colors.onSurface
                        ),
                        cursorBrush = SolidColor(NeobrutalismTheme.colors.onSurface),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    NeobrutalistButton(
                        text = "Cancelar",
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 8.dp),
                        containerColor = NeobrutalismTheme.colors.surface,
                        shape = RoundedCornerShape(8.dp),
                        borderWidth = 2.dp,
                        shadowOffset = 2.dp
                    )

                    NeobrutalistButton(
                        text = "Adicionar",
                        onClick = onSaveTag,
                        containerColor = NeobrutalismTheme.colors.accentYellow,
                        shape = RoundedCornerShape(8.dp),
                        borderWidth = 2.dp,
                        shadowOffset = 2.dp
                    )
                }
            }
        }
    }
}
