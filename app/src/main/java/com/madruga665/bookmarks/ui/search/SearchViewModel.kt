package com.madruga665.bookmarks.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<SearchUiState> = combine(
        bookmarkRepository.allBookmarks,
        collectionRepository.collections,
        _searchQuery
    ) { bookmarks, collections, query ->
        val collectionsMap = collections.associateBy { it.id }

        val uniqueTagsCount = bookmarks
            .flatMap { it.tags.split(",") }
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinctBy { it.lowercase() }
            .size

        val stats = LibraryStats(
            collectionsCount = collections.size,
            linksCount = bookmarks.size,
            pinnedCount = bookmarks.count { it.isPinned },
            tagsCount = uniqueTagsCount
        )

        val recentBookmarks = bookmarks
            .sortedByDescending { maxOf(it.updatedAt, it.createdAt) }
            .take(10)

        val cleanQuery = query.trim().lowercase()

        val searchResults = if (cleanQuery.isBlank()) {
            emptyList()
        } else {
            bookmarks.filter { bookmark ->
                val titleMatches = bookmark.title?.lowercase()?.contains(cleanQuery) == true
                val urlMatches = bookmark.url.lowercase().contains(cleanQuery)
                val descriptionMatches = bookmark.description?.lowercase()?.contains(cleanQuery) == true
                val collectionMatches = collectionsMap[bookmark.collectionId]?.name?.lowercase()?.contains(cleanQuery) == true
                val tagsMatch = bookmark.tags.split(",")
                    .map { it.trim().lowercase() }
                    .any { it.contains(cleanQuery) }
                val notesMatches = bookmark.notes?.lowercase()?.contains(cleanQuery) == true

                titleMatches || urlMatches || descriptionMatches || collectionMatches || tagsMatch || notesMatches
            }.sortedWith(
                compareByDescending<BookmarkEntity> { it.isPinned }
                    .thenByDescending { maxOf(it.updatedAt, it.createdAt) }
            )
        }

        SearchUiState(
            isLoading = false,
            searchQuery = query,
            libraryStats = stats,
            recentlySavedBookmarks = recentBookmarks,
            searchResults = searchResults,
            collectionsMap = collectionsMap,
            userMessage = null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState(isLoading = true)
    )

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onClearQuery() {
        _searchQuery.value = ""
    }
}
