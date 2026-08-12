# Data Model Specification: Add Bookmark Bottom Sheet Modal

## Overview

This document defines data structures, domain model updates, and UI state models for the Add Bookmark Bottom Sheet Modal feature.

---

## Domain Entity Updates

### Bookmark Entity (Updated)

```kotlin
data class Bookmark(
    val id: String,
    val url: String,
    val title: String?,
    val faviconUrl: String?,
    val collectionId: String, // Defaults to "col_unsorted"
    val isPinned: Boolean = false,
    val createdAt: Long,
    val syncStatus: SyncStatus = SyncStatus.PENDING_SYNC
)
```

**Validation Rules**:
- `collectionId`: Non-empty string. If null or empty, defaults to `"col_unsorted"`.
- `isPinned`: Boolean flag indicating if bookmark is pinned to top.

---

## UI State Models

### SaveBookmarkModalUiState

State container managed by `SaveBookmarkViewModel`.

```kotlin
data class SaveBookmarkModalUiState(
    val targetUrl: String = "",
    val availableCollections: List<Collection> = emptyList(),
    val selectedCollectionId: String = "col_unsorted",
    val isPinned: Boolean = false,
    val isCreatingFolder: Boolean = false,
    val newFolderNameInput: String = "",
    val newFolderColorAccent: CollectionColorAccent = CollectionColorAccent.YELLOW,
    val folderInputError: String? = null,
    val isSaving: Boolean = false,
    val error: String? = null
) {
    val selectedCollection: Collection?
        get() = availableCollections.find { it.id == selectedCollectionId }

    val saveButtonText: String
        get() = "Save to \"${selectedCollection?.name ?: "Unsorted"}\""
}
```

---

## Local Persistence Schema Updates (Room Database)

### Updated Table: `bookmarks_table`
- `id`: TEXT PRIMARY KEY
- `url`: TEXT NOT NULL
- `title`: TEXT
- `favicon_url`: TEXT
- `collection_id`: TEXT NOT NULL DEFAULT 'col_unsorted'
- `is_pinned`: INTEGER NOT NULL DEFAULT 0
- `created_at`: INTEGER NOT NULL
- `sync_status`: TEXT NOT NULL DEFAULT 'PENDING_SYNC'
