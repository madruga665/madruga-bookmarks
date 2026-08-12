# UI Contract: Add Bookmark Bottom Sheet Composable

## Overview

This contract defines the public composable interface and event handlers for `SaveBookmarkBottomSheet`.

---

## Composable Signature

```kotlin
@Composable
fun SaveBookmarkBottomSheet(
    uiState: SaveBookmarkModalUiState,
    onCollectionSelect: (collectionId: String) -> Unit,
    onTogglePin: () -> Unit,
    onToggleCreateFolder: () -> Unit,
    onNewFolderNameChange: (String) -> Unit,
    onNewFolderColorSelect: (CollectionColorAccent) -> Unit,
    onCreateFolderSubmit: () -> Unit,
    onConfirmSave: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
)
```

---

## Component Layout & Interactions

1. **Header Row**:
   - Title: `"Save to Bookmarks"`
   - Subtitle: `uiState.targetUrl` (truncated if long)
   - Action Button: Top-Right "New Folder" icon button -> Triggers `onToggleCreateFolder()`.

2. **Pin Link Row**:
   - Pushpin Icon + Text `"Pin this link"` -> Triggers `onTogglePin()`.

3. **Collection List**:
   - Scrollable Column of `NeobrutalistSelectableFolderCard` items.
   - Click on folder -> Triggers `onCollectionSelect(collectionId)`.
   - Selected folder displays yellow background (`#FFD600` / `#F9E2AF`) and checkmark icon.

4. **Inline New Folder Form (If `uiState.isCreatingFolder == true`)**:
   - Folder Name Input (`onNewFolderNameChange`).
   - Color Accent Picker (`onNewFolderColorSelect`).
   - Create Button -> Triggers `onCreateFolderSubmit()`.

5. **Main Action Button**:
   - Full-width Neobrutalist Button labeled `uiState.saveButtonText` -> Triggers `onConfirmSave()`.
