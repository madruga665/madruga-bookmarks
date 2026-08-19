# Bookmark Repository Contract

**Feature**: `011-bookmark-actions-menu`  
**Date**: 2026-08-18

## Repository Interface & Methods

The feature leverages the existing `BookmarkRepository` asynchronous methods:

```kotlin
interface IBookmarkRepository {
    val allBookmarks: Flow<List<BookmarkEntity>>
    fun getBookmarksByCollection(collectionId: String): Flow<List<BookmarkEntity>>
    fun getBookmarkById(bookmarkId: String): Flow<BookmarkEntity?>
    suspend fun togglePin(bookmarkId: String)
    suspend fun deleteBookmark(bookmarkId: String)
}
```

### Method Guarantees:
1. `togglePin(bookmarkId: String)`:
   - Reads the current `BookmarkEntity` direct from DAO.
   - Flips `isPinned` (`!current.isPinned`) and updates `updatedAt = System.currentTimeMillis()`.
   - Triggers emission on all reactive `Flow<List<BookmarkEntity>>` queries.

2. `deleteBookmark(bookmarkId: String)`:
   - Removes the bookmark record by ID from Room database.
   - Automatically drops the bookmark from active collections, search lists, and recents flows.
