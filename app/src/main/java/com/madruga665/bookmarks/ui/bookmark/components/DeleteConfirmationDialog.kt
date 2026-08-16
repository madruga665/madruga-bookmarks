package com.madruga665.bookmarks.ui.bookmark.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@Composable
fun DeleteConfirmationDialog(
    isVisible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

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
                    text = stringResource(R.string.bookmark_delete_dialog_title),
                    style = NeobrutalismTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = NeobrutalismTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(R.string.bookmark_delete_dialog_msg),
                    style = NeobrutalismTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    ),
                    color = NeobrutalismTheme.colors.subtext
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    NeobrutalistButton(
                        text = stringResource(R.string.dialog_cancel),
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 8.dp),
                        containerColor = NeobrutalismTheme.colors.surface,
                        shape = RoundedCornerShape(8.dp),
                        borderWidth = 2.dp,
                        shadowOffset = 2.dp
                    )

                    NeobrutalistButton(
                        text = stringResource(R.string.dialog_delete),
                        onClick = onConfirm,
                        containerColor = Color(0xFFFF4B4B),
                        shape = RoundedCornerShape(8.dp),
                        borderWidth = 2.dp,
                        shadowOffset = 2.dp
                    )
                }
            }
        }
    }
}
