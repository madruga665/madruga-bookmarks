package com.madruga665.bookmarks.ui.navigation

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import com.madruga665.bookmarks.data.repository.SettingsRepository
import com.madruga665.bookmarks.ui.collection.CollectionDetailScreen
import com.madruga665.bookmarks.ui.collection.CollectionDetailViewModel
import com.madruga665.bookmarks.ui.home.HomeScreen
import com.madruga665.bookmarks.ui.home.HomeViewModel
import com.madruga665.bookmarks.ui.savemodal.SaveBookmarkBottomSheet
import com.madruga665.bookmarks.ui.savemodal.SaveBookmarkViewModel
import com.madruga665.bookmarks.ui.settings.SettingsScreen
import com.madruga665.bookmarks.ui.settings.SettingsViewModel
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme

object NavRoutes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val SETTINGS = "settings"
    const val MANAGE_COLLECTIONS = "manage_collections"
    const val FOLDER_DETAIL = "folder_detail/{folderId}"

    fun folderDetail(folderId: String) = "folder_detail/$folderId"
}

@Composable
fun BookmarksNavGraph(
    homeViewModel: HomeViewModel,
    saveBookmarkViewModel: SaveBookmarkViewModel,
    collectionRepository: CollectionRepository,
    bookmarkRepository: BookmarkRepository,
    settingsRepository: SettingsRepository,
    navController: NavHostController = rememberNavController()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            val context = LocalContext.current
            HomeScreen(
                uiState = uiState,
                saveBookmarkViewModel = saveBookmarkViewModel,
                onUrlInputChange = homeViewModel::onUrlInputChange,
                onPasteFromClipboard = homeViewModel::onPasteFromClipboard,
                onCollectionClick = { collectionId ->
                    navController.navigate(NavRoutes.folderDetail(collectionId))
                },
                onCollectionLongClick = homeViewModel::openActionsMenu,
                onLongPressStart = homeViewModel::onLongPressStart,
                onLongPressDrag = homeViewModel::onLongPressDrag,
                onLongPressRelease = {
                    homeViewModel.onLongPressRelease { collection ->
                        homeViewModel.shareCollection(context, collection)
                    }
                },
                onHoveredOptionChange = homeViewModel::onHoveredOptionChange,
                onDismissActionsMenu = homeViewModel::dismissActionsMenu,
                onEditCollectionClick = homeViewModel::openEditDialog,
                onShareCollectionClick = { collection ->
                    homeViewModel.shareCollection(context, collection)
                },
                onDeleteCollectionClick = homeViewModel::openDeleteDialog,
                onDismissEditDialog = homeViewModel::dismissEditDialog,
                onConfirmEditCollection = homeViewModel::updateCollection,
                onDismissDeleteDialog = homeViewModel::dismissDeleteDialog,
                onConfirmDeleteCollection = homeViewModel::deleteCollection,
                onNavigateToSearch = {
                    navController.navigate(NavRoutes.SEARCH)
                },
                onNavigateToSettings = {
                    navController.navigate(NavRoutes.SETTINGS)
                },
                onNavigateToManageCollections = {
                    navController.navigate(NavRoutes.MANAGE_COLLECTIONS)
                }
            )
        }

        composable(NavRoutes.SEARCH) {
            PlaceholderDestination(title = "Search Screen")
        }

        composable(NavRoutes.SETTINGS) {
            val context = LocalContext.current
            val settingsViewModel = remember {
                SettingsViewModel(
                    settingsRepository = settingsRepository,
                    bookmarkRepository = bookmarkRepository,
                    collectionRepository = collectionRepository
                )
            }
            val settingsUiState by settingsViewModel.uiState.collectAsState()

            SettingsScreen(
                uiState = settingsUiState,
                onBackClick = { navController.popBackStack() },
                onThemeSelect = settingsViewModel::setThemeMode,
                onLanguageSelect = settingsViewModel::setLanguage,
                onToggleHapticFeedback = settingsViewModel::toggleHapticFeedback,
                onExportBackupClick = {
                    Toast.makeText(context, "Exporting backup...", Toast.LENGTH_SHORT).show()
                },
                onRestoreBackupClick = {
                    Toast.makeText(context, "Restore Backup feature coming soon", Toast.LENGTH_SHORT).show()
                },
                onImportBookmarksClick = {
                    Toast.makeText(context, "Import Bookmarks feature coming soon", Toast.LENGTH_SHORT).show()
                }
            )
        }

        composable(NavRoutes.MANAGE_COLLECTIONS) {
            PlaceholderDestination(title = "Manage Collections Screen")
        }

        composable(NavRoutes.FOLDER_DETAIL) { backStackEntry ->
            val context = LocalContext.current
            val folderId = backStackEntry.arguments?.getString("folderId") ?: ""
            val viewModel = remember(folderId) {
                CollectionDetailViewModel(
                    collectionId = folderId,
                    collectionRepository = collectionRepository,
                    bookmarkRepository = bookmarkRepository
                )
            }
            val detailUiState by viewModel.uiState.collectAsState()
            val saveModalUiState by saveBookmarkViewModel.uiState.collectAsState()

            CollectionDetailScreen(
                uiState = detailUiState,
                onBackClick = {
                    navController.popBackStack()
                },
                onAddLinkClick = {
                    saveBookmarkViewModel.openSaveModal("https://")
                    saveBookmarkViewModel.onSelectCollection(folderId)
                },
                onOptionsClick = {
                    // Options click handler
                },
                onBookmarkClick = { bookmark ->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bookmark.url))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Could not open URL: ${bookmark.url}", Toast.LENGTH_SHORT).show()
                    }
                },
                onSubcollectionClick = { subcollectionId ->
                    navController.navigate(NavRoutes.folderDetail(subcollectionId))
                }
            )

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
                        Toast.makeText(context, "Bookmark saved successfully!", Toast.LENGTH_SHORT).show()
                    }
                },
                onDismiss = saveBookmarkViewModel::dismissModal
            )
        }
    }
}

@Composable
private fun PlaceholderDestination(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeobrutalismTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = NeobrutalismTheme.typography.headlineMedium,
            color = NeobrutalismTheme.colors.onSurface
        )
    }
}

