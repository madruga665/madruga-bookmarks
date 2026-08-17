package com.madruga665.bookmarks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow
import com.madruga665.bookmarks.ui.utils.TagPalette

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NeobrutalistTagInput(
    tags: List<String>,
    tagInput: String,
    onTagInputChange: (String) -> Unit,
    onAddTag: (String) -> Unit,
    onRemoveTag: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxTags: Int = 10,
    existingTags: List<String> = emptyList()
) {
    val suggestions by remember(tagInput, tags, existingTags) {
        derivedStateOf {
            if (tagInput.isBlank() || tags.size >= maxTags) {
                emptyList()
            } else {
                val cleanInput = tagInput.trim().removePrefix("#").lowercase()
                existingTags
                    .filter { it.isNotBlank() && !tags.contains(it) }
                    .filter { it.contains(cleanInput, ignoreCase = true) }
                    .take(5)
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Label Row: TAGS label and counter (tags.size / maxTags)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.tag_label),
                style = NeobrutalismTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                color = NeobrutalismTheme.colors.onSurface
            )
            Text(
                text = "${tags.size}/$maxTags",
                style = NeobrutalismTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = NeobrutalismTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }

        // Active Tags Flow Row (FlowRow)
        if (tags.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                tags.forEach { tag ->
                    NeobrutalistTagChip(
                        tag = tag,
                        onRemoveClick = { onRemoveTag(tag) }
                    )
                }
            }
        }

        // Autocomplete Suggestions
        if (suggestions.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                suggestions.forEach { suggestion ->
                    NeobrutalistTagChip(
                        tag = suggestion,
                        showHash = true,
                        backgroundColor = TagPalette.getTagColor(suggestion).copy(alpha = 0.5f),
                        onTagClick = { onAddTag(suggestion) },
                        modifier = Modifier.testTag("tag_suggestion_$suggestion")
                    )
                }
            }
        }

        // Input row with text input field and Add button
        if (tags.size < maxTags) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Text field box with neobrutalist styling
                val inputShape = RoundedCornerShape(10.dp)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .neobrutalistShadow(
                            shadowColor = NeobrutalismTheme.colors.shadow,
                            borderColor = NeobrutalismTheme.colors.border,
                            borderWidth = 2.dp,
                            shadowOffset = 2.dp,
                            shape = inputShape
                        )
                        .background(NeobrutalismTheme.colors.surface, inputShape)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    if (tagInput.isEmpty()) {
                        Text(
                            text = stringResource(R.string.tag_input_placeholder),
                            color = NeobrutalismTheme.colors.onSurface.copy(alpha = 0.45f),
                            fontSize = 14.sp
                        )
                    }

                    BasicTextField(
                        value = tagInput,
                        onValueChange = { newText ->
                            if (newText.endsWith(",") || newText.endsWith(" ")) {
                                val cleanTag = newText.trimEnd(',', ' ').trim()
                                if (cleanTag.isNotBlank()) {
                                    onAddTag(cleanTag)
                                }
                            } else {
                                onTagInputChange(newText.take(25))
                            }
                        },
                        singleLine = true,
                        textStyle = NeobrutalismTheme.typography.bodyMedium.copy(
                            color = NeobrutalismTheme.colors.onSurface,
                            fontSize = 14.sp
                        ),
                        cursorBrush = SolidColor(NeobrutalismTheme.colors.onSurface),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            if (tagInput.isNotBlank()) {
                                onAddTag(tagInput)
                            }
                        }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tag_input_field")
                    )
                }

                // Add NeobrutalistButton
                NeobrutalistButton(
                    onClick = {
                        if (tagInput.isNotBlank()) {
                            onAddTag(tagInput)
                        }
                    },
                    enabled = tagInput.isNotBlank(),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("tag_btn_add_tag")
                ) {
                    Text(
                        text = stringResource(R.string.tag_add_button),
                        style = NeobrutalismTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
