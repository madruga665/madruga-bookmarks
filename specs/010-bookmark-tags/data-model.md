# Data Model: Bookmark Tagging System

**Feature Branch**: `010-bookmark-tags` | **Date**: 2026-08-16 | **Spec**: [spec.md](./spec.md)

## Domain Models & Utilities

### 1. `TagItem` (Presentation Model)
```kotlin
package com.madruga665.bookmarks.ui.utils

import androidx.compose.ui.graphics.Color

data class TagItem(
    val name: String,
    val color: Color,
    val count: Int = 0
) {
    val displayName: String
        get() = if (name.startsWith("#")) name else "#$name"
}
```

### 2. `TagPalette` (Color Resolver)
```kotlin
package com.madruga665.bookmarks.ui.utils

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

object TagPalette {
    val colors = listOf(
        Color(0xFFFFE600), // Yellow
        Color(0xFFFF4B8B), // Pink
        Color(0xFF9B51E0), // Purple
        Color(0xFF2F80ED), // Blue
        Color(0xFF00C49F), // Mint
        Color(0xFFA0E040), // Lime
        Color(0xFFFF7700), // Orange
        Color(0xFFBA68C8), // Mauve
        Color(0xFF6C88A8), // Slate Blue
        Color(0xFFFF6B6B)  // Coral
    )

    fun getTagColor(tagName: String): Color {
        val clean = tagName.trim().removePrefix("#").lowercase()
        if (clean.isBlank()) return colors.first()
        val index = abs(clean.hashCode()) % colors.size
        return colors[index]
    }
}
```

### 3. Tag Extension Functions on `BookmarkEntity`
```kotlin
package com.madruga665.bookmarks.ui.utils

import com.madruga665.bookmarks.data.local.BookmarkEntity

val BookmarkEntity.tagList: List<String>
    get() = tags.split(",")
        .map { it.trim().removePrefix("#").lowercase() }
        .filter { it.isNotBlank() }

fun List<String>.toTagString(): String =
    this.map { it.trim().removePrefix("#").lowercase() }
        .filter { it.isNotBlank() }
        .distinct()
        .take(10)
        .joinToString(",")
```

---

## UI State Models

### 1. `SearchUiState` (Updated)
```kotlin
data class SearchUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val libraryStats: LibraryStats = LibraryStats(),
    val availableTags: List<TagItem> = emptyList(),
    val selectedTags: Set<String> = emptySet(),
    val recentlySavedBookmarks: List<BookmarkEntity> = emptyList(),
    val searchResults: List<BookmarkEntity> = emptyList(),
    val collectionsMap: Map<String, CollectionEntity> = emptyMap(),
    val userMessage: String? = null
)
```

### 2. `SaveBookmarkModalUiState` (Updated)
```kotlin
data class SaveBookmarkModalUiState(
    val isVisible: Boolean = false,
    val url: String = "",
    val title: String = "",
    val description: String? = null,
    val faviconUrl: String? = null,
    val thumbnailUrl: String? = null,
    val sourcePlatform: String? = null,
    val selectedCollection: CollectionEntity? = null,
    val collections: List<CollectionEntity> = emptyList(),
    val tags: List<String> = emptyList(),
    val tagInput: String = "",
    val isPinned: Boolean = false,
    val isSaving: Boolean = false,
    val isInlineCreatingFolder: Boolean = false,
    val inlineFolderName: String = "",
    val inlineFolderColor: String = "#FFE600"
)
```

### 3. `BookmarkDetailUiState` (Updated)
```kotlin
data class BookmarkDetailUiState(
    val isLoading: Boolean = true,
    val bookmark: BookmarkEntity? = null,
    val collection: CollectionEntity? = null,
    val tags: List<String> = emptyList(),
    val isEditingTitle: Boolean = false,
    val titleInput: String = "",
    val isEditingNotes: Boolean = false,
    val notesInput: String = "",
    val tagInput: String = "",
    val isRefreshingMetadata: Boolean = false,
    val userMessage: String? = null
)
```
