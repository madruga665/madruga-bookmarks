# Data Model Specification: Native Android Neobrutalism Home Screen

## Overview

This document defines the data structures, entities, and UI state models for the Native Android Neobrutalism Home Screen feature.

---

## Domain Entities

### 1. Collection (Folder)

Represents a user folder used to organize bookmarks.

```kotlin
data class Collection(
    val id: String,
    val name: String,
    val linkCount: Int,
    val iconKey: String, // e.g., "code", "briefcase", "ai", "folder"
    val colorAccentToken: CollectionColorAccent,
    val createdAt: Long,
    val updatedAt: Long
)

enum class CollectionColorAccent {
    YELLOW,
    PURPLE,
    ORANGE,
    BLUE,
    GREEN
}
```

**Validation Rules**:
- `id`: Non-blank string (UUID or server ID).
- `name`: Max 50 characters, trimmed.
- `linkCount`: Non-negative integer (`>= 0`).

---

### 2. Bookmark

Represents a saved web link entity.

```kotlin
data class Bookmark(
    val id: String,
    val url: String,
    val title: String?,
    val faviconUrl: String?,
    val collectionId: String?, // Nullable for unorganized bookmarks
    val createdAt: Long,
    val syncStatus: SyncStatus
)

enum class SyncStatus {
    SYNCED,
    PENDING_SYNC,
    FAILED
}
```

**Validation Rules**:
- `url`: Must be a valid Web URL schema (`http://` or `https://`).
- `title`: Extracted metadata title or fallback to domain name.

---

### 3. ThemeConfig

Represents the user's active theme selection.

```kotlin
enum class AppThemeMode {
    LIGHT,             // Neobrutalism Reference Light
    CATPPUCCIN_MOCHA,  // Catppuccin Mocha Dark
    SYSTEM             // Follow Android System Theme
}

data class ThemeConfig(
    val mode: AppThemeMode,
    val isHighContrast: Boolean = true
)
```

---

## UI State Models

### HomeScreenUiState

State container exposed by `HomeViewModel` to `HomeScreen` composable.

```kotlin
sealed interface HomeScreenUiState {
    object Loading : HomeScreenUiState

    data class Success(
        val collections: List<Collection>,
        val quickSaveUrlInput: String = "",
        val inputError: String? = null,
        val isSaving: Boolean = false,
        val themeMode: AppThemeMode = AppThemeMode.SYSTEM
    ) : HomeScreenUiState

    data class Error(val message: String) : HomeScreenUiState
}
```

### QuickSaveFormState

Transient form state for the home quick-save bar.

```kotlin
data class QuickSaveFormState(
    val inputUrl: String = "",
    val errorMessage: String? = null,
    val isValid: Boolean = false,
    val isClipboardPasted: Boolean = false
)
```

---

## Local Persistence Schema (Room Database Concept)

### Entity: `collections_table`
- `id`: TEXT PRIMARY KEY
- `name`: TEXT NOT NULL
- `link_count`: INTEGER NOT NULL DEFAULT 0
- `icon_key`: TEXT NOT NULL
- `color_accent`: TEXT NOT NULL
- `created_at`: INTEGER NOT NULL
- `updated_at`: INTEGER NOT NULL

### Entity: `bookmarks_table`
- `id`: TEXT PRIMARY KEY
- `url`: TEXT NOT NULL
- `title`: TEXT
- `favicon_url`: TEXT
- `collection_id`: TEXT FOREIGN KEY REFERENCES `collections_table(id)` ON DELETE SET NULL
- `created_at`: INTEGER NOT NULL
- `sync_status`: TEXT NOT NULL DEFAULT 'PENDING_SYNC'
