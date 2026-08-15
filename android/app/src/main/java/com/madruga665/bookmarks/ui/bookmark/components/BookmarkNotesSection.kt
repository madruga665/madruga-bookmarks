package com.madruga665.bookmarks.ui.bookmark.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@Composable
fun BookmarkNotesSection(
    notes: String?,
    isEditing: Boolean,
    editedNotes: String,
    onStartEditing: () -> Unit,
    onNotesChange: (String) -> Unit,
    onSaveNotes: () -> Unit,
    onCancelEditing: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "NOTES",
            style = NeobrutalismTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            ),
            color = NeobrutalismTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isEditing) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .neobrutalistShadow(
                            shadowColor = NeobrutalismTheme.colors.shadow,
                            borderColor = NeobrutalismTheme.colors.border,
                            borderWidth = 2.dp,
                            shadowOffset = 3.dp,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .background(NeobrutalismTheme.colors.surface, RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    if (editedNotes.isEmpty()) {
                        Text(
                            text = "Adicione suas notas pessoais aqui...",
                            style = NeobrutalismTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp
                            ),
                            color = NeobrutalismTheme.colors.subtext
                        )
                    }
                    BasicTextField(
                        value = editedNotes,
                        onValueChange = onNotesChange,
                        textStyle = NeobrutalismTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = NeobrutalismTheme.colors.onSurface
                        ),
                        cursorBrush = SolidColor(NeobrutalismTheme.colors.onSurface),
                        minLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 72.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NeobrutalistButton(
                        text = "Cancelar",
                        onClick = onCancelEditing,
                        modifier = Modifier.padding(end = 8.dp),
                        containerColor = NeobrutalismTheme.colors.surface,
                        shape = RoundedCornerShape(8.dp),
                        borderWidth = 2.dp,
                        shadowOffset = 2.dp
                    )

                    NeobrutalistButton(
                        text = "Salvar",
                        onClick = onSaveNotes,
                        containerColor = NeobrutalismTheme.colors.accentYellow,
                        shape = RoundedCornerShape(8.dp),
                        borderWidth = 2.dp,
                        shadowOffset = 2.dp
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 80.dp)
                    .neobrutalistShadow(
                        shadowColor = NeobrutalismTheme.colors.shadow,
                        borderColor = NeobrutalismTheme.colors.border,
                        borderWidth = 2.dp,
                        shadowOffset = 3.dp,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(NeobrutalismTheme.colors.surface, RoundedCornerShape(10.dp))
                    .clickable(onClick = onStartEditing)
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                if (notes.isNullOrBlank()) {
                    Text(
                        text = "Toque para adicionar notas...",
                        style = NeobrutalismTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp
                        ),
                        color = NeobrutalismTheme.colors.subtext
                    )
                } else {
                    Text(
                        text = notes,
                        style = NeobrutalismTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        ),
                        color = NeobrutalismTheme.colors.onSurface
                    )
                }
            }
        }
    }
}
