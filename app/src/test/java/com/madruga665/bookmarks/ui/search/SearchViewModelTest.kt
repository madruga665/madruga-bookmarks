package com.madruga665.bookmarks.ui.search

import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity
import com.madruga665.bookmarks.data.repository.BookmarkRepository
import com.madruga665.bookmarks.data.repository.CollectionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val bookmarkRepository: BookmarkRepository = mockk(relaxed = true)
    private val collectionRepository: CollectionRepository = mockk(relaxed = true)

    private val bookmarksFlow = MutableStateFlow<List<BookmarkEntity>>(emptyList())
    private val collectionsFlow = MutableStateFlow<List<CollectionEntity>>(emptyList())

    private lateinit var viewModel: SearchViewModel

    private val collection1 = CollectionEntity(
        id = "col_ia",
        name = "Artificial Intelligence",
        linkCount = 2,
        subcollectionCount = 0,
        parentId = null,
        iconKey = "code",
        colorAccent = "YELLOW",
        createdAt = 1000L,
        updatedAt = 1000L
    )

    private val collection2 = CollectionEntity(
        id = "col_design",
        name = "Design Inspiration",
        linkCount = 1,
        subcollectionCount = 0,
        parentId = null,
        iconKey = "folder",
        colorAccent = "PURPLE",
        createdAt = 2000L,
        updatedAt = 2000L
    )

    private val bookmark1 = BookmarkEntity(
        id = "bm_1",
        url = "https://openai.com",
        title = "OpenAI Research",
        description = "Pioneering artificial intelligence research",
        faviconUrl = "https://openai.com/favicon.ico",
        thumbnailUrl = "https://openai.com/thumb.png",
        sourcePlatform = "Web",
        collectionId = "col_ia",
        notes = "Important LLM provider",
        tags = "AI, LLM, Research",
        isPinned = false,
        createdAt = 1000L,
        updatedAt = 3000L
    )

    private val bookmark2 = BookmarkEntity(
        id = "bm_2",
        url = "https://github.com/madruga665",
        title = "GitHub Profile",
        description = "Developer portfolio and code repositories",
        faviconUrl = "https://github.com/favicon.ico",
        thumbnailUrl = "https://github.com/thumb.png",
        sourcePlatform = "GitHub",
        collectionId = "col_ia",
        notes = "My open source projects",
        tags = "Code, Kotlin, Android",
        isPinned = true,
        createdAt = 2000L,
        updatedAt = 4000L
    )

    private val bookmark3 = BookmarkEntity(
        id = "bm_3",
        url = "https://dribbble.com/shots",
        title = "Dribbble UI Concepts",
        description = "Neobrutalism design exploration and shots",
        faviconUrl = "https://dribbble.com/favicon.ico",
        thumbnailUrl = "https://dribbble.com/thumb.png",
        sourcePlatform = "Dribbble",
        collectionId = "col_design",
        notes = "Brutalist styles",
        tags = "Design, UI, Brutalism",
        isPinned = false,
        createdAt = 3000L,
        updatedAt = 5000L
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { bookmarkRepository.allBookmarks } returns bookmarksFlow
        coEvery { collectionRepository.collections } returns collectionsFlow

        viewModel = SearchViewModel(
            bookmarkRepository = bookmarkRepository,
            collectionRepository = collectionRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state and loading verification`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        // Before flows emit data
        bookmarksFlow.value = emptyList()
        collectionsFlow.value = emptyList()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("", state.searchQuery)
        assertFalse(state.isSearching)
        assertFalse(state.hasSearchResults)
        assertFalse(state.isEmptySearchResult)
        assertEquals(0, state.searchResults.size)
        assertEquals(0, state.recentlySavedBookmarks.size)
        assertEquals(LibraryStats(0, 0, 0, 0), state.libraryStats)
    }

    @Test
    fun `library statistics calculation aggregates counts accurately`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        collectionsFlow.value = listOf(collection1, collection2)
        bookmarksFlow.value = listOf(bookmark1, bookmark2, bookmark3)

        val state = viewModel.uiState.value
        val stats = state.libraryStats

        assertEquals(2, stats.collectionsCount)
        assertEquals(3, stats.linksCount)
        assertEquals(1, stats.pinnedCount) // bookmark2 is pinned
        // Tags: bookmark1: [AI, LLM, Research], bookmark2: [Code, Kotlin, Android], bookmark3: [Design, UI, Brutalism] -> 9 unique tags
        assertEquals(9, stats.tagsCount)
    }

    @Test
    fun `library statistics ignores duplicate and blank tags case-insensitively`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val bmWithDupTags1 = bookmark1.copy(tags = "AI,  ai, , Tech")
        val bmWithDupTags2 = bookmark2.copy(tags = "tech, TECH, Kotlin")

        collectionsFlow.value = listOf(collection1)
        bookmarksFlow.value = listOf(bmWithDupTags1, bmWithDupTags2)

        val state = viewModel.uiState.value
        // Unique tags: "ai", "tech", "kotlin" -> 3
        assertEquals(3, state.libraryStats.tagsCount)
    }

    @Test
    fun `recently saved bookmarks extraction is ordered by timestamp descending and capped at 10`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val bookmarksList = (1..15).map { index ->
            BookmarkEntity(
                id = "bm_$index",
                url = "https://example.com/$index",
                title = "Bookmark $index",
                description = "Description $index",
                faviconUrl = null,
                thumbnailUrl = null,
                sourcePlatform = "Web",
                collectionId = "col_ia",
                notes = null,
                tags = "tag$index",
                isPinned = false,
                createdAt = index * 1000L,
                updatedAt = index * 1000L
            )
        }

        collectionsFlow.value = listOf(collection1)
        bookmarksFlow.value = bookmarksList

        val state = viewModel.uiState.value
        val recents = state.recentlySavedBookmarks

        assertEquals(10, recents.size)
        // Most recent first (bm_15 down to bm_6)
        assertEquals("bm_15", recents[0].id)
        assertEquals("bm_14", recents[1].id)
        assertEquals("bm_6", recents[9].id)
    }

    @Test
    fun `search query filtering matches by title case-insensitively`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        collectionsFlow.value = listOf(collection1, collection2)
        bookmarksFlow.value = listOf(bookmark1, bookmark2, bookmark3)

        viewModel.onQueryChange("openai")

        val state = viewModel.uiState.value
        assertTrue(state.isSearching)
        assertTrue(state.hasSearchResults)
        assertFalse(state.isEmptySearchResult)
        assertEquals(1, state.searchResults.size)
        assertEquals("bm_1", state.searchResults.first().id)
    }

    @Test
    fun `search query filtering matches by url case-insensitively`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        collectionsFlow.value = listOf(collection1, collection2)
        bookmarksFlow.value = listOf(bookmark1, bookmark2, bookmark3)

        viewModel.onQueryChange("GITHUB.COM")

        val state = viewModel.uiState.value
        assertEquals(1, state.searchResults.size)
        assertEquals("bm_2", state.searchResults.first().id)
    }

    @Test
    fun `search query filtering matches by description case-insensitively`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        collectionsFlow.value = listOf(collection1, collection2)
        bookmarksFlow.value = listOf(bookmark1, bookmark2, bookmark3)

        viewModel.onQueryChange("neobrutalism")

        val state = viewModel.uiState.value
        assertEquals(1, state.searchResults.size)
        assertEquals("bm_3", state.searchResults.first().id)
    }

    @Test
    fun `search query filtering matches by tags case-insensitively`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        collectionsFlow.value = listOf(collection1, collection2)
        bookmarksFlow.value = listOf(bookmark1, bookmark2, bookmark3)

        viewModel.onQueryChange("kotlin")

        val state = viewModel.uiState.value
        assertEquals(1, state.searchResults.size)
        assertEquals("bm_2", state.searchResults.first().id)
    }

    @Test
    fun `search query filtering matches by collection name case-insensitively`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        collectionsFlow.value = listOf(collection1, collection2)
        bookmarksFlow.value = listOf(bookmark1, bookmark2, bookmark3)

        viewModel.onQueryChange("inspiration")

        val state = viewModel.uiState.value
        assertEquals(1, state.searchResults.size)
        assertEquals("bm_3", state.searchResults.first().id)
    }

    @Test
    fun `pinned items are ordered first in search results`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        // Both bookmark1 and bookmark2 are in collection "col_ia" (Artificial Intelligence)
        // bookmark1 has higher updatedAt (3000L) but is not pinned
        // bookmark2 has updatedAt (4000L) and isPinned = true
        // Let's create an unpinned bookmark with even higher updatedAt
        val bookmark4 = BookmarkEntity(
            id = "bm_4",
            url = "https://anthropic.com",
            title = "Anthropic Claude",
            description = "Artificial intelligence research lab",
            faviconUrl = null,
            thumbnailUrl = null,
            sourcePlatform = "Web",
            collectionId = "col_ia",
            notes = null,
            tags = "AI, Claude",
            isPinned = false,
            createdAt = 10000L,
            updatedAt = 10000L
        )

        collectionsFlow.value = listOf(collection1)
        bookmarksFlow.value = listOf(bookmark1, bookmark2, bookmark4)

        viewModel.onQueryChange("intelligence")

        val state = viewModel.uiState.value
        assertEquals(3, state.searchResults.size)
        // bookmark2 is pinned -> must be first even though bookmark4 has higher updatedAt
        assertEquals("bm_2", state.searchResults[0].id)
        assertTrue(state.searchResults[0].isPinned)
        assertEquals("bm_4", state.searchResults[1].id)
        assertEquals("bm_1", state.searchResults[2].id)
    }

    @Test
    fun `empty or blank query restores discovery state and clears search results`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        collectionsFlow.value = listOf(collection1, collection2)
        bookmarksFlow.value = listOf(bookmark1, bookmark2, bookmark3)

        // Perform search
        viewModel.onQueryChange("openai")
        assertEquals(1, viewModel.uiState.value.searchResults.size)
        assertTrue(viewModel.uiState.value.isSearching)

        // Reset to empty string
        viewModel.onQueryChange("")
        val emptyState = viewModel.uiState.value
        assertFalse(emptyState.isSearching)
        assertFalse(emptyState.hasSearchResults)
        assertFalse(emptyState.isEmptySearchResult)
        assertEquals(0, emptyState.searchResults.size)
        assertEquals(3, emptyState.recentlySavedBookmarks.size)

        // Reset to whitespace string
        viewModel.onQueryChange("   ")
        val whitespaceState = viewModel.uiState.value
        assertFalse(whitespaceState.isSearching)
        assertEquals(0, whitespaceState.searchResults.size)
    }

    @Test
    fun `clear query action resets search query to empty string`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        collectionsFlow.value = listOf(collection1, collection2)
        bookmarksFlow.value = listOf(bookmark1, bookmark2, bookmark3)

        viewModel.onQueryChange("design")
        assertEquals("design", viewModel.uiState.value.searchQuery)
        assertEquals(1, viewModel.uiState.value.searchResults.size)

        viewModel.onClearQuery()
        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertFalse(state.isSearching)
        assertEquals(0, state.searchResults.size)
    }

    @Test
    fun `non-matching query sets isEmptySearchResult flag to true`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        collectionsFlow.value = listOf(collection1, collection2)
        bookmarksFlow.value = listOf(bookmark1, bookmark2, bookmark3)

        viewModel.onQueryChange("non_existent_keyword_xyz")
        val state = viewModel.uiState.value

        assertTrue(state.isSearching)
        assertFalse(state.hasSearchResults)
        assertTrue(state.isEmptySearchResult)
        assertEquals(0, state.searchResults.size)
    }

    @Test
    fun `collections map correctly maps collection IDs`() = runTest(testDispatcher) {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        collectionsFlow.value = listOf(collection1, collection2)
        bookmarksFlow.value = listOf(bookmark1)

        val state = viewModel.uiState.value
        assertEquals(2, state.collectionsMap.size)
        assertEquals("Artificial Intelligence", state.collectionsMap["col_ia"]?.name)
        assertEquals("Design Inspiration", state.collectionsMap["col_design"]?.name)
    }
}
