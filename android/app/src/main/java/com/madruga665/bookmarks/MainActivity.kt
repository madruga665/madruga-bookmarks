package com.madruga665.bookmarks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.madruga665.bookmarks.data.repository.AppThemeMode
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import com.madruga665.bookmarks.data.repository.SettingsRepository
import com.madruga665.bookmarks.ui.home.HomeViewModel
import com.madruga665.bookmarks.ui.navigation.BookmarksNavGraph
import com.madruga665.bookmarks.ui.savemodal.SaveBookmarkViewModel
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import android.content.Intent

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val saveBookmarkViewModel: SaveBookmarkViewModel by viewModels()

    @Inject
    lateinit var collectionRepository: CollectionRepository

    @Inject
    lateinit var bookmarkRepository: BookmarkRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleShareIntent(intent)
        setContent {
            val themeMode by settingsRepository.themeMode.collectAsState(initial = AppThemeMode.SYSTEM)
            val isSystemDark = isSystemInDarkTheme()
            val darkTheme = when (themeMode) {
                AppThemeMode.LIGHT -> false
                AppThemeMode.CATPPUCCIN_MOCHA -> true
                AppThemeMode.SYSTEM -> isSystemDark
            }

            NeobrutalismTheme(darkTheme = darkTheme) {
                BookmarksNavGraph(
                    homeViewModel = homeViewModel,
                    saveBookmarkViewModel = saveBookmarkViewModel,
                    collectionRepository = collectionRepository,
                    bookmarkRepository = bookmarkRepository,
                    settingsRepository = settingsRepository
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleShareIntent(intent)
    }

    private fun handleShareIntent(intent: Intent?) {
        if (intent == null) return
        if (intent.action == Intent.ACTION_SEND && intent.type?.startsWith("text/") == true) {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            val extractedUrl = extractUrlFromText(sharedText)
            if (!extractedUrl.isNullOrBlank()) {
                saveBookmarkViewModel.openSaveModal(extractedUrl)
            }
        }
    }

    private fun extractUrlFromText(text: String?): String? {
        if (text.isNullOrBlank()) return null
        val urlRegex = Regex("""(https?://[^\s]+)""", RegexOption.IGNORE_CASE)
        val match = urlRegex.find(text)
        return match?.value ?: if (text.trim().startsWith("http://") || text.trim().startsWith("https://")) text.trim() else null
    }
}

