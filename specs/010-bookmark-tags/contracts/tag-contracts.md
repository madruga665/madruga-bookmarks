# Component & ViewModel Contracts: Bookmark Tagging System

**Feature Branch**: `010-bookmark-tags` | **Date**: 2026-08-16 | **Spec**: [spec.md](../spec.md)

## 1. `NeobrutalistTagChip` Composable Contract

```kotlin
@Composable
fun NeobrutalistTagChip(
    tag: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = TagPalette.getTagColor(tag),
    isSelected: Boolean = false,
    onTagClick: (() -> Unit)? = null,
    onRemoveClick: (() -> Unit)? = null
)
```

- **Styling**:
  - Border: 1.5dp black border (`NeobrutalismTheme.colors.border`).
  - Shape: `RoundedCornerShape(8.dp)` or pill `RoundedCornerShape(50)`.
  - Offset shadow: 2.dp offset shadow when selected or interactive.
  - Text: Bold 12.sp, high contrast against background color.
  - Optional 'X' button for removable state.

---

## 2. `NeobrutalistTagInput` Composable Contract

```kotlin
@Composable
fun NeobrutalistTagInput(
    tags: List<String>,
    tagInput: String,
    onTagInputChange: (String) -> Unit,
    onAddTag: (String) -> Unit,
    onRemoveTag: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxTags: Int = 10
)
```

- Renders:
  1. Flow row of existing tag chips with remove ('X') buttons.
  2. Text field for typing a tag, placeholder `e.g. android, design` (`R.string.tag_input_placeholder`), and "Add" button (`tag_btn_add_tag`).
  3. Enter/comma key listeners to auto-submit tag.

---

## 3. Search Screen Tag Filter Contract (`SearchScreen.kt`)

```kotlin
@Composable
fun SearchTagFilterRow(
    availableTags: List<TagItem>,
    selectedTags: Set<String>,
    onToggleTag: (String) -> Unit,
    onClearTags: () -> Unit,
    modifier: Modifier = Modifier
)
```

- Displays horizontal scrollable row of tag chips.
- Tapping toggles selection.
- An "All" chip is always displayed first; selecting it clears all tag filters (replaces the separate "Clear" button approach).

---

## 4. ViewModel State Contracts

### `SaveBookmarkViewModel`:
- `fun onTagInputChange(input: String)`
- `fun onAddTag(tag: String)`
- `fun onRemoveTag(tag: String)`
- `fun onConfirmSave(onSuccess: () -> Unit)` -> persists bookmark with `tags.toTagString()`

### `SearchViewModel`:
- `fun onToggleTagFilter(tag: String)`
- `fun onClearTagFilters()`

### `BookmarkDetailViewModel`:
- `fun onTagInputChange(input: String)`
- `fun onAddTag(tag: String)`
- `fun onRemoveTag(tag: String)`
