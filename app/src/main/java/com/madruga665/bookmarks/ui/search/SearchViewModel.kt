package com.madruga665.bookmarks.ui.search

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import com.madruga665.bookmarks.ui.components.ArcGeometryCalculator
import com.madruga665.bookmarks.ui.components.BookmarkActionsOverlayState
import com.madruga665.bookmarks.ui.components.BookmarkOption
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedTags = MutableStateFlow<Set<String>>(emptySet())
    private val _overlayState = MutableStateFlow(BookmarkActionsOverlayState())

    private val _searchData = combine(
        bookmarkRepository.allBookmarks,
        collectionRepository.collections,
        _searchQuery,
        _selectedTags
    ) { bookmarks: List<BookmarkEntity>, collections: List<CollectionEntity>, query: String, selectedTags: Set<String> ->
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
    }

    val uiState: StateFlow<SearchUiState> = combine(
        _searchData,
        _overlayState
    ) { baseState, overlay ->
        baseState.copy(
            activeMenuBookmark = overlay.activeBookmark,
            activeCardOffset = overlay.cardOffset,
            activeCardSize = overlay.cardSize,
            touchPositionInWindow = overlay.touchPositionInWindow,
            dragPositionInWindow = overlay.dragPositionInWindow,
            hoveredOption = overlay.hoveredOption,
            bookmarkToDelete = overlay.bookmarkToDelete
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

    fun onLongPressStart(
        bookmark: BookmarkEntity,
        touchPosition: Offset,
        cardOffset: Offset,
        cardSize: IntSize
    ) {
        _overlayState.update { current ->
            current.copy(
                activeBookmark = bookmark,
                cardOffset = cardOffset,
                cardSize = cardSize,
                touchPositionInWindow = touchPosition,
                dragPositionInWindow = touchPosition,
                hoveredOption = null
            )
        }
    }

    fun onLongPressDrag(
        touchPosition: Offset,
        screenWidth: Float = 1080f,
        screenHeight: Float = 2400f,
        density: Float = 3.0f
    ) {
        _overlayState.update { current ->
            if (current.activeBookmark != null) {
                val anchor = current.touchPositionInWindow ?: touchPosition
                val radius = 100f * density
                val (startAngle, sweepAngle) = ArcGeometryCalculator.calculateSector(
                    anchor = anchor,
                    screenWidth = screenWidth,
                    screenHeight = screenHeight,
                    radius = radius
                )
                val itemPositions = ArcGeometryCalculator.calculateItemPositions(
                    anchor = anchor,
                    itemCount = 4,
                    radius = radius,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle
                )
                val hoveredIndex = ArcGeometryCalculator.findHoveredItemIndex(
                    touchPosition = touchPosition,
                    anchor = anchor,
                    itemPositions = itemPositions,
                    buttonRadius = 26f * density,
                    hitPadding = 54f * density
                )
                val options = listOf(
                    BookmarkOption.OPEN,
                    BookmarkOption.PIN,
                    BookmarkOption.SHARE,
                    BookmarkOption.DELETE
                )
                val hoveredOption = hoveredIndex?.let { options.getOrNull(it) }

                current.copy(
                    dragPositionInWindow = touchPosition,
                    hoveredOption = hoveredOption
                )
            } else {
                current
            }
        }
    }

    fun onHoveredOptionChange(option: BookmarkOption?) {
        _overlayState.update { current ->
            if (current.hoveredOption != option) {
                current.copy(hoveredOption = option)
            } else {
                current
            }
        }
    }

    fun dismissActionsMenu() {
        _overlayState.update { current ->
            current.copy(
                activeBookmark = null,
                cardOffset = null,
                cardSize = null,
                touchPositionInWindow = null,
                dragPositionInWindow = null,
                hoveredOption = null
            )
        }
    }

    fun onLongPressRelease(onActionSelected: (BookmarkEntity, BookmarkOption) -> Unit = { _, _ -> }) {
        val current = _overlayState.value
        val bookmark = current.activeBookmark
        val option = current.hoveredOption

        _overlayState.update {
            it.copy(
                activeBookmark = null,
                cardOffset = null,
                cardSize = null,
                touchPositionInWindow = null,
                dragPositionInWindow = null,
                hoveredOption = null
            )
        }

        if (bookmark != null && option != null) {
            onActionSelected(bookmark, option)
        }
    }

    fun openDeleteDialog(bookmark: BookmarkEntity) {
        _overlayState.update { current ->
            current.copy(
                activeBookmark = null,
                cardOffset = null,
                cardSize = null,
                touchPositionInWindow = null,
                hoveredOption = null,
                bookmarkToDelete = bookmark
            )
        }
    }

    fun dismissDeleteDialog() {
        _overlayState.update { current ->
            current.copy(bookmarkToDelete = null)
        }
    }

    fun togglePin(bookmarkId: String) {
        viewModelScope.launch {
            bookmarkRepository.togglePin(bookmarkId)
        }
    }

    fun deleteBookmark(bookmarkId: String) {
        viewModelScope.launch {
            bookmarkRepository.deleteBookmark(bookmarkId)
            dismissDeleteDialog()
        }
    }
}
