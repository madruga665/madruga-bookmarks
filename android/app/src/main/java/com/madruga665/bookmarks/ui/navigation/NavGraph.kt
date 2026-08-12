package com.madruga665.bookmarks.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.madruga665.bookmarks.ui.home.HomeScreen
import com.madruga665.bookmarks.ui.home.HomeViewModel
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
    navController: NavHostController = rememberNavController()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                uiState = uiState,
                onUrlInputChange = homeViewModel::onUrlInputChange,
                onPasteFromClipboard = homeViewModel::onPasteFromClipboard,
                onQuickSaveSubmit = homeViewModel::onQuickSaveSubmit,
                onCollectionClick = { collectionId ->
                    navController.navigate(NavRoutes.folderDetail(collectionId))
                },
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
            PlaceholderDestination(title = "Settings Screen")
        }

        composable(NavRoutes.MANAGE_COLLECTIONS) {
            PlaceholderDestination(title = "Manage Collections Screen")
        }

        composable(NavRoutes.FOLDER_DETAIL) { backStackEntry ->
            val folderId = backStackEntry.arguments?.getString("folderId") ?: ""
            PlaceholderDestination(title = "Folder Detail: $folderId")
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
