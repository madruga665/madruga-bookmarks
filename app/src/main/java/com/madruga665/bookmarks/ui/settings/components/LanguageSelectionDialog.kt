package com.madruga665.bookmarks.ui.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.data.repository.AppLanguage
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@Composable
fun LanguageSelectionDialog(
    currentLanguage: AppLanguage,
    onLanguageSelect: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        val dialogShape = RoundedCornerShape(16.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("tag_language_selection_dialog")
                .neobrutalistShadow(
                    shadowColor = NeobrutalismTheme.colors.shadow,
                    borderColor = NeobrutalismTheme.colors.border,
                    borderWidth = 2.5.dp,
                    shadowOffset = 6.dp,
                    shape = dialogShape
                )
                .background(NeobrutalismTheme.colors.surface, dialogShape)
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Title
                Text(
                    text = stringResource(R.string.lang_select_title),
                    style = NeobrutalismTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = NeobrutalismTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Options
                val languages = listOf(
                    AppLanguage.SYSTEM to stringResource(R.string.lang_system),
                    AppLanguage.EN to stringResource(R.string.lang_en),
                    AppLanguage.PT_BR to stringResource(R.string.lang_pt_br)
                )

                languages.forEach { (lang, label) ->
                    val isSelected = lang == currentLanguage
                    LanguageOptionRow(
                        label = label,
                        isSelected = isSelected,
                        onClick = {
                            onLanguageSelect(lang)
                            onDismiss()
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Cancel Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    NeobrutalistButton(
                        text = stringResource(R.string.dialog_cancel),
                        onClick = onDismiss,
                        containerColor = NeobrutalismTheme.colors.surface,
                        borderColor = NeobrutalismTheme.colors.border,
                        borderWidth = 2.dp,
                        shadowOffset = 3.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageOptionRow(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(10.dp)
    val interactionSource = remember { MutableInteractionSource() }

    val bgColor = if (isSelected) NeobrutalismTheme.colors.accentYellow else NeobrutalismTheme.colors.surface
    val contentColor = if (isSelected) NeobrutalismTheme.colors.border else NeobrutalismTheme.colors.onSurface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, NeobrutalismTheme.colors.border, shape)
            .background(bgColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = NeobrutalismTheme.typography.bodyLarge.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 15.sp
                ),
                color = contentColor
            )

            if (isSelected) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = stringResource(R.string.dialog_select),
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
