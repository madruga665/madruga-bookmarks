package com.madruga665.bookmarks.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import com.madruga665.bookmarks.ui.utils.TagItem
import com.madruga665.bookmarks.ui.utils.TagPalette
import com.madruga665.bookmarks.ui.utils.tagList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedTags = MutableStateFlow<Set<String>>(emptySet())

    val uiState: StateFlow<SearchUiState> = combine(
        bookmarkRepository.allBookmarks,
        collectionRepository.collections,
        _searchQuery,
        _selectedTags
    ) { bookmarks, collections, query, selectedTags ->
        val collectionsMap = collections.associateBy { it.id }

        val tagCounts = bookmarks
            .flatMap { it.tagList }
            .groupingBy { it }
            .eachCount()

        val availableTags = tagCounts.map { (tagName, count) ->
            TagItem(
                name = tagName,
                color = TagPalette.getTagColor(tagName),
                count = count
            )
        }.sortedWith(
            compareByDescending<TagItem> { it.count }
                .thenBy { it.name }
        )

        val uniqueTagsCount = availableTags.size

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

        val searchResults = if (cleanQuery.isBlank() && selectedTags.isEmpty()) {
            emptyList()
        } else {
            bookmarks.filter { bookmark ->
                val tagsMatchFilter = if (selectedTags.isNotEmpty()) {
                    val bTags = bookmark.tagList.toSet()
                    bTags.containsAll(selectedTags)
                } else {
                    true
                }

                val queryMatchFilter = if (cleanQuery.isNotBlank()) {
                    val titleMatches = bookmark.title?.lowercase()?.contains(cleanQuery) == true
                    val urlMatches = bookmark.url.lowercase().contains(cleanQuery)
                    val descriptionMatches = bookmark.description?.lowercase()?.contains(cleanQuery) == true
                    val collectionMatches = collectionsMap[bookmark.collectionId]?.name?.lowercase()?.contains(cleanQuery) == true
                    val tagsMatch = bookmark.tagList.any { it.contains(cleanQuery) }
                    val notesMatches = bookmark.notes?.lowercase()?.contains(cleanQuery) == true

                    titleMatches || urlMatches || descriptionMatches || collectionMatches || tagsMatch || notesMatches
                } else {
                    true
                }

                tagsMatchFilter && queryMatchFilter
            }.sortedWith(
                compareByDescending<BookmarkEntity> { it.isPinned }
                    .thenByDescending { maxOf(it.updatedAt, it.createdAt) }
            )
        }

        SearchUiState(
            isLoading = false,
            searchQuery = query,
            libraryStats = stats,
            availableTags = availableTags,
            selectedTags = selectedTags,
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

    fun onToggleTagFilter(tag: String) {
        val clean = tag.trim().removePrefix("#").lowercase()
        if (clean.isBlank()) return
        _selectedTags.update { current ->
            if (current.contains(clean)) current - clean else current + clean
        }
    }

    fun onClearTagFilters() {
        _selectedTags.value = emptySet()
    }
}
