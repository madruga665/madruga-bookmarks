# UI Actions Contract: Bookmark Long-Press Menu

**Feature**: `011-bookmark-actions-menu`  
**Date**: 2026-08-18

## 1. Composable Callbacks Contract (`NeobrutalistBookmarkCard`)

```kotlin
@Composable
fun NeobrutalistBookmarkCard(
    bookmark: BookmarkEntity,
    onClick: () -> Unit,
    onLongPressStart: ((BookmarkEntity, Offset, Offset, IntSize) -> Unit)? = null,
    onLongPressDrag: ((Offset) -> Unit)? = null,
    onLongPressRelease: (() -> Unit)? = null,
    isActiveMenu: Boolean = false,
    touchPositionInWindow: Offset? = null,
    onHoveredOptionChange: (BookmarkOption?) -> Unit = {},
    modifier: Modifier = Modifier
)
```

### Parameter Semantics
- `bookmark`: The bookmark data model to render.
- `onClick`: Invoked when user taps quickly (<350ms).
- `onLongPressStart`: Invoked at >=350ms hold, passing `(bookmark, touchPositionInWindow, cardPositionInWindow, cardSize)`.
- `onLongPressDrag`: Invoked continuously as finger moves, passing the latest `touchPositionInWindow`.
- `onLongPressRelease`: Invoked when touch lifts, triggering execution or dismissal.
- `isActiveMenu`: When `true`, card renders in elevated overlay state (tilt & scale) with radial buttons rendered around it.
- `touchPositionInWindow`: Coordinates used by overlay card to calculate distance to each radial action button.
- `onHoveredOptionChange`: Callback notifying parent of the currently hovered `BookmarkOption`.

---

## 2. Actions Execution Contract

| Action Option | Execution Mechanism | Expected UI / System Behavior |
|---|---|---|
| `OPEN` | `context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))` | Opens link in browser / custom tab. If invalid, displays error Toast. |
| `PIN` | `viewModel.togglePin(bookmark.id)` -> `repository.togglePin(id)` | Toggles `isPinned` in Room DB. Flows update collection grid automatically. |
| `SHARE` | `Intent.createChooser(Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, "$title - $url"), null)` | Opens native OS Share sheet populated with title and URL. |
| `DELETE` | Triggers `bookmarkToDelete = bookmark` state | Displays `DeleteConfirmationDialog`. On confirm, calls `viewModel.deleteBookmark(id)`. |

---

## 3. String Resources Contract

| Resource Key | Default (PT-BR) | English (EN) |
|---|---|---|
| `bookmark_action_open` | Abrir | Open |
| `bookmark_action_pin` | Fixar | Pin |
| `bookmark_action_unpin` | Desafixar | Unpin |
| `bookmark_action_share` | Compartilhar | Share |
| `bookmark_action_delete` | Excluir | Delete |
| `bookmark_toast_pinned` | Link fixado com sucesso | Bookmark pinned successfully |
| `bookmark_toast_unpinned` | Link desafixado | Bookmark unpinned |
| `bookmark_toast_deleted` | Link excluído | Bookmark deleted |
