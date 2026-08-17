package com.madruga665.bookmarks.ui.search

import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.ui.utils.TagItem

data class LibraryStats(
    val collectionsCount: Int = 0,
    val linksCount: Int = 0,
    val pinnedCount: Int = 0,
    val tagsCount: Int = 0
)

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val libraryStats: LibraryStats = LibraryStats(),
    val availableTags: List<TagItem> = emptyList(),
    val selectedTags: Set<String> = emptySet(),
    val recentlySavedBookmarks: List<BookmarkEntity> = emptyList(),
    val searchResults: List<BookmarkEntity> = emptyList(),
    val collectionsMap: Map<String, CollectionEntity> = emptyMap(),
    val userMessage: String? = null
) {
    val isSearching: Boolean
        get() = searchQuery.isNotBlank() || selectedTags.isNotEmpty()

    val hasSearchResults: Boolean
        get() = isSearching && searchResults.isNotEmpty()

    val isEmptySearchResult: Boolean
        get() = isSearching && searchResults.isEmpty() && !isLoading
}
