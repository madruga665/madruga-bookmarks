package com.madruga665.bookmarks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@Composable
fun DeleteCollectionDialog(
    collection: CollectionEntity?,
    onDismiss: () -> Unit,
    onConfirmDelete: (collectionId: String) -> Unit
) {
    if (collection == null) return

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
                    text = "Delete Collection",
                    style = NeobrutalismTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = NeobrutalismTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Are you sure you want to delete \"${collection.name}\"?",
                    fontSize = 14.sp,
                    color = NeobrutalismTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "This action will permanently delete this collection and all links inside it.",
                    fontSize = 12.sp,
                    color = NeobrutalismTheme.colors.subtext
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NeobrutalistButton(
                        text = "Cancel",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    NeobrutalistButton(
                        text = "Delete",
                        onClick = { onConfirmDelete(collection.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
