package com.madruga665.bookmarks.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.ui.home.components.HomeHeroHeadline
import com.madruga665.bookmarks.ui.home.components.HomeScreenTopBar
import com.madruga665.bookmarks.ui.home.components.MyCollectionsGrid
import com.madruga665.bookmarks.ui.home.components.QuickSaveBar
import com.madruga665.bookmarks.ui.savemodal.SaveBookmarkBottomSheet
import com.madruga665.bookmarks.ui.savemodal.SaveBookmarkViewModel
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme

@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    saveBookmarkViewModel: SaveBookmarkViewModel,
    onUrlInputChange: (String) -> Unit,
    onPasteFromClipboard: (String) -> Unit,
    onCollectionClick: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToManageCollections: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val saveModalUiState by saveBookmarkViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NeobrutalismTheme.colors.background)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Action Bar
        HomeScreenTopBar(
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToManageCollections = onNavigateToManageCollections,
            onNavigateToSearch = onNavigateToSearch
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Hero Headline Section
        HomeHeroHeadline()

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Save Bar Section
        when (uiState) {
            is HomeScreenUiState.Success -> {
                QuickSaveBar(
                    urlValue = uiState.quickSaveUrlInput,
                    onUrlChange = onUrlInputChange,
                    onPasteClick = {
                        val text = clipboardManager.getText()?.text ?: ""
                        if (text.isNotBlank()) {
                            onPasteFromClipboard(text)
                        } else {
                            Toast.makeText(context, "Clipboard is empty", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onAddClick = {
                        if (uiState.quickSaveUrlInput.isNotBlank()) {
                            saveBookmarkViewModel.openSaveModal(uiState.quickSaveUrlInput)
                        } else {
                            Toast.makeText(context, "Please enter a valid link", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                if (uiState.inputError != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = uiState.inputError,
                        color = NeobrutalismTheme.colors.accentOrange,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Collections Grid Section
                MyCollectionsGrid(
                    collections = uiState.collections,
                    onCollectionClick = onCollectionClick
                )
            }
            is HomeScreenUiState.Loading -> {
                CircularProgressIndicator(
                    color = NeobrutalismTheme.colors.onSurface,
                    modifier = Modifier.padding(32.dp)
                )
            }
            is HomeScreenUiState.Error -> {
                Text(
                    text = uiState.message,
                    color = NeobrutalismTheme.colors.accentOrange,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    // Save Bookmark Bottom Sheet Modal
    SaveBookmarkBottomSheet(
        uiState = saveModalUiState,
        onCollectionSelect = saveBookmarkViewModel::onSelectCollection,
        onTogglePin = saveBookmarkViewModel::onTogglePin,
        onToggleCreateFolder = saveBookmarkViewModel::onToggleCreateFolder,
        onNewFolderNameChange = saveBookmarkViewModel::onNewFolderNameChange,
        onNewFolderColorSelect = saveBookmarkViewModel::onNewFolderColorSelect,
        onCreateFolderSubmit = saveBookmarkViewModel::onCreateFolderSubmit,
        onConfirmSave = {
            saveBookmarkViewModel.onConfirmSave {
                onUrlInputChange("")
                Toast.makeText(context, "Bookmark saved successfully!", Toast.LENGTH_SHORT).show()
            }
        },
        onDismiss = saveBookmarkViewModel::dismissModal
    )
}
