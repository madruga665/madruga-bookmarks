# UI & Component Contract: Search Screen with Library Statistics & Discovery

**Feature Branch**: `008-search-screen-discovery`
**Spec Reference**: [spec.md](../spec.md)

## 1. Composable Function Signatures

### `SearchScreen`
Top-level screen composable for the Search feature.

```kotlin
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onCancelClick: () -> Unit,
    onBookmarkClick: (String) -> Unit,
    modifier: Modifier = Modifier
)
```

### `YourLibraryCard`
Neobrutalist yellow card rendering 4 stat columns.

```kotlin
@Composable
fun YourLibraryCard(
    stats: LibraryStats,
    modifier: Modifier = Modifier
)
```

### `RecentlySavedSection`
Horizontal carousel displaying recent bookmarks.

```kotlin
@Composable
fun RecentlySavedSection(
    bookmarks: List<BookmarkEntity>,
    collectionsMap: Map<String, CollectionEntity>,
    onBookmarkClick: (String) -> Unit,
    modifier: Modifier = Modifier
)
```

### `RecentlySavedBookmarkCard`
Card representing a single bookmark in the carousel.

```kotlin
@Composable
fun RecentlySavedBookmarkCard(
    bookmark: BookmarkEntity,
    collectionName: String?,
    collectionColor: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
)
```

### `SearchIdlePrompt`
Idle discovery state prompt with search icon and message.

```kotlin
@Composable
fun SearchIdlePrompt(
    modifier: Modifier = Modifier
)
```

---

## 2. ViewModel Contract

### `SearchViewModel`
Hilt-injected ViewModel managing search state and subscriptions.

```kotlin
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    val uiState: StateFlow<SearchUiState>

    fun onQueryChange(query: String)
    fun onClearQuery()
}
```

---

## 3. Navigation Contract

- **Route**: `NavRoutes.SEARCH` ("search")
- **Cancel Action**: Navigates back (`navController.popBackStack()`)
- **Item Click Action**: Navigates to Bookmark Detail (`NavRoutes.bookmarkDetail(bookmarkId)`)
