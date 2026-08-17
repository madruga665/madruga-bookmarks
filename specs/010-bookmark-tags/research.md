# Research & Technical Decisions: Bookmark Tagging System

**Feature Branch**: `010-bookmark-tags` | **Date**: 2026-08-16 | **Spec**: [spec.md](./spec.md)

## Decision 1: Tag Storage & Invariant Serialization

- **Decision**: Store tags as a comma-separated normalized string in `BookmarkEntity.tags` (e.g., `"design,kotlin,compose"`), with helper extension functions `BookmarkEntity.tagList: List<String>` and `List<String>.toTagString(): String`.
- **Rationale**: 
  - `BookmarkEntity` in Room already contains `@ColumnInfo(name = "tags") val tags: String = ""` and DAO methods `updateBookmarkTags` exist.
  - Zero Room schema migrations required, avoiding schema breakages or migration overhead.
  - Straightforward mapping to future JSON arrays for API sync (`["design", "kotlin", "compose"]`).
- **Alternatives Considered**:
  - Separate `tags_table` and `bookmark_tag_cross_ref` table: Adds unnecessary relational complexity and join queries for a lightweight tag taxonomy in v1.

---

## Decision 2: Tag Color Assignment Strategy

- **Decision**: Deterministic hashing of tag names to a curated 10-color Neobrutalism pastel palette in `TagPalette` (e.g., `abs(tagName.hashCode()) % palette.size`).
- **Rationale**:
  - Ensures `#kotlin` or `#design` always gets the same distinct vibrant color throughout the app (cards, search filters, detail screen) without needing to store color hex per tag in the database.
  - Consistent visual identity with Neobrutalism high-contrast design system.
- **Alternatives Considered**:
  - User-configurable color per tag: Adds unnecessary configuration overhead for users when creating quick tags.

---

## Decision 3: Tag Normalization and Validation

- **Decision**: 
  - Normalize tags to lowercase, trimmed, with leading `#` stripped (displayed with `#` in UI).
  - Enforce max 25 characters per tag, max 10 tags per bookmark.
  - Disallow empty/blank tags and reject case-insensitive duplicates.
  - Delimiters: Enter key, comma (`,`), and space can trigger tag addition in input fields.
- **Rationale**:
  - Prevents fragmented tags (e.g. `#Design` vs `#design`).
  - Protects card layouts from overflowing.

---

## Decision 4: Search & Tag Filtering Pipeline

- **Decision**: Reactive in-memory Flow filtering in `SearchViewModel` combining text query and active selected tag set:
  - `val uiState: StateFlow<SearchUiState> = combine(allBookmarks, collections, searchQuery, selectedTags) { ... }`
  - Tag filter chip bar rendered below search bar on `SearchScreen`.
  - Bookmarks match if `selectedTags.isEmpty() || bookmark.tagList.containsAll(selectedTags)` AND text query matches title/url/description/tags.
- **Rationale**:
  - Instantaneous (<50ms) reactivity, no disk round-trips for tag toggling, seamlessly combines with existing search logic.

---

## Decision 5: Tag Input Component (`NeobrutalistTagInput`)

- **Decision**: Create a reusable composable `NeobrutalistTagInput` used in both `SaveBookmarkBottomSheet` and `BookmarkDetailScreen`.
  - Displays existing active tag chips with delete ('X') buttons.
  - Single-line `BasicTextField` with placeholder `Add tag...` (localized).
  - Tapping "Add" button, pressing Enter, or typing a comma creates the chip and clears text.
