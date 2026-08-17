package com.madruga665.bookmarks.ui.collection.create

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.data.repository.CollectionRepository
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.ui.collection.create.components.CollectionColorPicker
import com.madruga665.bookmarks.ui.collection.create.components.CollectionIconPicker
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow
import com.madruga665.bookmarks.ui.utils.CollectionPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCollectionBottomSheet(
    onDismiss: () -> Unit,
    onCollectionCreated: (CollectionEntity) -> Unit = {},
    collectionRepository: CollectionRepository? = null,
    viewModel: CreateCollectionViewModel = remember(collectionRepository) {
        requireNotNull(collectionRepository) {
            "collectionRepository must be provided to CreateCollectionBottomSheet"
        }
        CreateCollectionViewModel(collectionRepository)
    },
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NeobrutalismTheme.colors.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 44.dp, height = 5.dp)
                    .background(NeobrutalismTheme.colors.onSurface, RoundedCornerShape(3.dp))
            )
        },
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier.testTag("tag_create_collection_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Row: Title & Close 'X' Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.collection_create_title),
                    style = NeobrutalismTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = NeobrutalismTheme.colors.onSurface
                )

                NeobrutalistButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("tag_create_collection_close_btn")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.collection_create_close),
                        tint = NeobrutalismTheme.colors.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // NAME Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.collection_create_name_label),
                    style = NeobrutalismTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    color = NeobrutalismTheme.colors.onSurface
                )

                Text(
                    text = "${uiState.characterCount}/40",
                    style = NeobrutalismTheme.typography.bodySmall.copy(
                        fontSize = 12.sp
                    ),
                    color = NeobrutalismTheme.colors.subtext
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .neobrutalistShadow(
                        shadowColor = NeobrutalismTheme.colors.shadow,
                        borderColor = NeobrutalismTheme.colors.border,
                        borderWidth = 2.5.dp,
                        shadowOffset = 3.dp,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(NeobrutalismTheme.colors.surface, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                if (uiState.nameInput.isEmpty()) {
                    Text(
                        text = stringResource(R.string.collection_create_name_hint),
                        color = NeobrutalismTheme.colors.subtext,
                        fontSize = 14.sp
                    )
                }
                BasicTextField(
                    value = uiState.nameInput,
                    onValueChange = viewModel::onNameChange,
                    singleLine = true,
                    textStyle = NeobrutalismTheme.typography.bodyMedium.copy(
                        color = NeobrutalismTheme.colors.onSurface,
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(NeobrutalismTheme.colors.onSurface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("tag_create_collection_name_input")
                )
            }

            if (uiState.errorMessage != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = uiState.errorMessage!!,
                    color = NeobrutalismTheme.colors.accentOrange,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // COLOR Section
            CollectionColorPicker(
                selectedColorHex = uiState.selectedColor,
                onColorSelect = viewModel::onColorSelect
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ICON Section
            CollectionIconPicker(
                selectedIconKey = uiState.selectedIconKey,
                selectedColor = CollectionPalette.getColor(uiState.selectedColor),
                onIconSelect = viewModel::onIconSelect
            )

            Spacer(modifier = Modifier.height(24.dp))

            // CTA Button
            NeobrutalistButton(
                onClick = {
                    viewModel.createCollection { created ->
                        onCollectionCreated(created)
                        onDismiss()
                    }
                },
                enabled = uiState.isSubmitEnabled,
                containerColor = if (uiState.isSubmitEnabled) NeobrutalismTheme.colors.accentYellow else NeobrutalismTheme.colors.surface,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tag_create_collection_submit")
            ) {
                Text(
                    text = stringResource(R.string.collection_create_button),
                    style = NeobrutalismTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = if (uiState.isSubmitEnabled) Color.Black else NeobrutalismTheme.colors.subtext,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
