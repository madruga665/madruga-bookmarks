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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookmarkTitleSection(
    title: String,
    isEditing: Boolean,
    editedTitle: String,
    categoryName: String?,
    createdAt: Long,
    onStartEditing: () -> Unit,
    onTitleChange: (String) -> Unit,
    onSaveTitle: () -> Unit,
    onCancelEditing: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val currentLocale = remember(configuration) {
        val locales = configuration.locales
        if (!locales.isEmpty) locales[0] ?: Locale.getDefault() else Locale.getDefault()
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
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
                    BasicTextField(
                        value = editedTitle,
                        onValueChange = onTitleChange,
                        textStyle = NeobrutalismTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = NeobrutalismTheme.colors.onSurface
                        ),
                        cursorBrush = SolidColor(NeobrutalismTheme.colors.onSurface),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NeobrutalistButton(
                        text = stringResource(R.string.dialog_cancel),
                        onClick = onCancelEditing,
                        modifier = Modifier.padding(end = 8.dp),
                        containerColor = NeobrutalismTheme.colors.surface,
                        shape = RoundedCornerShape(8.dp),
                        borderWidth = 2.dp,
                        shadowOffset = 2.dp
                    )

                    NeobrutalistButton(
                        text = stringResource(R.string.dialog_save),
                        onClick = onSaveTitle,
                        containerColor = NeobrutalismTheme.colors.accentYellow,
                        shape = RoundedCornerShape(8.dp),
                        borderWidth = 2.dp,
                        shadowOffset = 2.dp
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                val untitledText = stringResource(R.string.bookmark_untitled)
                Text(
                    text = title.ifBlank { untitledText },
                    style = NeobrutalismTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        lineHeight = 26.sp
                    ),
                    color = NeobrutalismTheme.colors.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp)
                )

                NeobrutalistButton(
                    onClick = onStartEditing,
                    modifier = Modifier.size(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    borderWidth = 2.dp,
                    shadowOffset = 2.dp
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.bookmark_edit_title_tooltip),
                        tint = NeobrutalismTheme.colors.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            val formattedDate = formatTimestamp(createdAt, currentLocale)
            val metadataSubtitle = if (!categoryName.isNullOrBlank()) {
                "$categoryName · $formattedDate"
            } else {
                formattedDate
            }

            Text(
                text = metadataSubtitle,
                style = NeobrutalismTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                ),
                color = NeobrutalismTheme.colors.subtext
            )
        }
    }
}

private fun formatTimestamp(timestamp: Long, locale: Locale): String {
    if (timestamp <= 0L) return ""
    return try {
        val sdf = SimpleDateFormat("d MMM yyyy, h:mm a", locale)
        sdf.format(Date(timestamp))
    } catch (e: Exception) {
        ""
    }
}
