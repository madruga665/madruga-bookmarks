# UI Contract: Native Android Neobrutalism Home Screen Composable

## Overview

This contract defines the public composable interface and event callbacks for the Neobrutalism Home Screen.

---

## Composable Signature

```kotlin
@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    onUrlInputChange: (String) -> Unit,
    onPasteFromClipboard: () -> Unit,
    onQuickSaveSubmit: () -> Unit,
    onCollectionClick: (collectionId: String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToManageCollections: () -> Unit,
    modifier: Modifier = Modifier
)
```

---

## UI Components & Event Map

### 1. Top Bar Actions
- **Settings Gear Button**: Triggers `onNavigateToSettings()`.
- **Manage Collections Button**: Triggers `onNavigateToManageCollections()`.
- **Search Magnifying Glass Button**: Triggers `onNavigateToSearch()`.

### 2. Hero Headline
- Static display text: `"Save now.\nFind anytime."`.

### 3. Quick Save Bar
- **URL Input Field**: Listens to `uiState.quickSaveUrlInput`. Triggers `onUrlInputChange(newText)`.
- **Clipboard Icon Button**: Triggers `onPasteFromClipboard()`.
- **Add (`+`) Action Button**: Triggers `onQuickSaveSubmit()`.

### 4. My Collections Section
- **Collections Grid**: Renders `uiState.collections` using `NeobrutalistFolderCard`.
- **Folder Card Click**: Triggers `onCollectionClick(collectionId)`.

---

## Neobrutalist Theme Provider Interface

```kotlin
@Composable
fun NeobrutalismTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
)

object NeobrutalismTheme {
    val colors: NeobrutalismColorScheme
        @Composable get() = LocalNeobrutalismColors.current

    val shapes: NeobrutalismShapes
        @Composable get() = LocalNeobrutalismShapes.current
}
```
