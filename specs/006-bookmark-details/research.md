# Technical Research & Architectural Decisions: Bookmark Details View

**Feature**: `006-bookmark-details`
**Date**: 2026-08-15

## 1. Navigation Architecture

- **Decision**: Full-screen Compose navigation destination `bookmark/{bookmarkId}` registered in `NavRoutes` (`NavRoutes.BOOKMARK_DETAIL` with helper `NavRoutes.bookmarkDetail(bookmarkId: String)`).
- **Rationale**: Provides native back-stack integration, system gesture navigation support, and deep-linking capability into a bookmark's details without modal overlay constraints.
- **Alternatives Considered**:
  - Modal Bottom Sheet: Rejected because rich content (hero image, description, notes, tags, action buttons) requires full viewport scrolling and standard back-stack lifecycle.

## 2. Room Data Model & Persistence

- **Decision**: Extend `BookmarkEntity` with:
  - `description: String? = null`
  - `notes: String? = null`
  - `tags: String = ""` (comma-separated tag list for simple SQLite storage & querying)
  - `updated_at: Long = System.currentTimeMillis()`
- **Database Migration**: Increment `AppDatabase` version from 3 to 4 with schema migration / fallback to destructive migration for dev environments and test seeds.
- **DAO Queries**:
  - `getBookmarkById(bookmarkId: String): Flow<BookmarkEntity?>`
  - `updateBookmark(bookmark: BookmarkEntity): Unit`
  - `updateBookmarkCollection(bookmarkId: String, collectionId: String, updatedAt: Long): Unit`
  - `deleteBookmarkById(bookmarkId: String): Unit`
- **Rationale**: Aligns local schema with Constitution Principle I (API-first sync schema) and Constitution Principle V (offline resiliency).

## 3. Metadata Extraction Enhancement

- **Decision**: Extend `LinkMetadata` in `LinkMetadataExtractor` with `description: String?`. Extract from `meta[property=og:description]`, `meta[name=twitter:description]`, and `meta[name=description]`.
- **Rationale**: Automatically populates the DESCRIPTION section on quick-save and metadata refresh.

## 4. UI Layer & Neobrutalism Design Tokens

- **Decision**: Build `BookmarkDetailScreen` using existing Neobrutalism design tokens (`ui/theme/Color.kt`, 2.5dp black borders, 4dp sharp shadow offset, bold uppercase typography).
- **Components**:
  - `BookmarkDetailTopBar`: Platform/collection badge + 4 action buttons (Reload, Share, Move, Delete).
  - `BookmarkDetailHero`: Preview thumbnail with top-right overlay Pin/Unpin button.
  - `BookmarkDetailTitleSection`: Display text + Edit pencil button switching to inline `TextField` with "Salvar" & "Cancelar" buttons.
  - `BookmarkDetailDescriptionSection`: Collapsible description text with "Show more" / "Show less" toggle.
  - `BookmarkDetailUrlCard`: Yellow Neobrutalist card with external link icon launching `Intent.ACTION_VIEW`.
  - `BookmarkDetailTagsSection`: Tag chips with 'X' remove icon and "+ Add" button opening a Neobrutalist input dialog.
  - `BookmarkDetailNotesSection`: Multi-line text field with "Salvar" & "Cancelar" buttons when editing.
  - `MoveCollectionBottomSheet`: Bottom sheet displaying available collections to move the link.
  - `DeleteConfirmationDialog`: Neobrutalist alert dialog confirming deletion.

## 5. Collection Screen Pinned Section

- **Decision**: In `CollectionDetailScreen`, partition bookmarks into `pinnedBookmarks` (`isPinned == true`) and `otherBookmarks`. If `pinnedBookmarks` is not empty, render a "PINNED ([count])" section above the "ALL LINKS ([count])" section.
- **Rationale**: Fulfills the user requirement that pinned links have prominence in the collection view.
