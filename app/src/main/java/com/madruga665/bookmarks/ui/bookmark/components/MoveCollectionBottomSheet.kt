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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.ui.components.NeobrutalistSelectableFolderCard
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoveCollectionBottomSheet(
    isVisible: Boolean,
    availableCollections: List<CollectionEntity>,
    currentCollectionId: String?,
    onSelectCollection: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isVisible) return

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
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = modifier.testTag("tag_move_collection_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.bookmark_move_sheet_title),
                style = NeobrutalismTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = NeobrutalismTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (availableCollections.isEmpty()) {
                Text(
                    text = stringResource(R.string.bookmark_no_folders_available),
                    style = NeobrutalismTheme.typography.bodyMedium,
                    color = NeobrutalismTheme.colors.subtext,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    availableCollections.forEach { collection ->
                        NeobrutalistSelectableFolderCard(
                            collection = collection,
                            isSelected = collection.id == currentCollectionId,
                            onSelect = { onSelectCollection(collection.id) }
                        )
                    }
                }
            }
        }
    }
}
