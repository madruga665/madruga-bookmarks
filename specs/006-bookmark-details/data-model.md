# Data Model: Bookmark Details View

**Feature**: `006-bookmark-details`
**Date**: 2026-08-15

## Entities

### BookmarkEntity (`bookmarks_table`)

Represents a saved bookmark with rich metadata, categorization, personal notes, and sync metadata.

| Field Name | Type | Nullable | Default | Description |
|---|---|---|---|---|
| `id` | `String` (PK) | No | - | Unique UUID identifier |
| `url` | `String` | No | - | Web URL of the saved bookmark |
| `title` | `String` | Yes | `null` | Extracted or user-edited title |
| `description` | `String` | Yes | `null` | Extracted webpage summary / description |
| `favicon_url` | `String` | Yes | `null` | Favicon image URL |
| `thumbnail_url` | `String` | Yes | `null` | Hero preview image URL |
| `source_platform` | `String` | Yes | `null` | Source tag / platform badge (e.g. "@Instagram", "Web") |
| `collection_id` | `String` | No | `"col_unsorted"` | Foreign key to `collections_table.id` |
| `notes` | `String` | Yes | `null` | Personal user notes |
| `tags` | `String` | No | `""` | Comma-separated list of assigned tags (e.g. `"IA,Design"`) |
| `is_pinned` | `Boolean` | No | `false` | Flag indicating if bookmark is pinned to top of collection |
| `created_at` | `Long` | No | Current timestamp | Timestamp of creation (epoch millis) |
| `updated_at` | `Long` | No | Current timestamp | Timestamp of last modification (epoch millis) |
| `sync_status` | `String` | No | `"PENDING_SYNC"` | Sync status (`"PENDING_SYNC"`, `"SYNCED"`, `"ERROR"`) |

### CollectionEntity (`collections_table`)

Represents a folder/collection containing bookmarks.

| Field Name | Type | Nullable | Default | Description |
|---|---|---|---|---|
| `id` | `String` (PK) | No | - | Unique collection identifier |
| `name` | `String` | No | - | Name of the collection |
| `link_count` | `Int` | No | `0` | Count of bookmarks inside |
| `subcollection_count` | `Int` | No | `0` | Count of child subcollections |
| `parent_id` | `String` | Yes | `null` | Parent collection ID if nested |
| `icon_key` | `String` | No | - | Icon identifier for display |
| `color_accent` | `String` | No | - | Hex color code string (e.g. `"#FF6B6B"`) |
| `created_at` | `Long` | No | Current timestamp | Epoch millis |
| `updated_at` | `Long` | No | Current timestamp | Epoch millis |

---

## State Transitions & Actions

### 1. Pin / Unpin Bookmark
- Action: `togglePin()`
- Update: `is_pinned = !is_pinned`, `updated_at = now()`, `sync_status = "PENDING_SYNC"`

### 2. Edit Title
- Action: `saveTitle(newTitle: String)`
- Update: `title = newTitle.trim()`, `updated_at = now()`, `sync_status = "PENDING_SYNC"`

### 3. Edit Notes
- Action: `saveNotes(newNotes: String?)`
- Update: `notes = newNotes?.trim()`, `updated_at = now()`, `sync_status = "PENDING_SYNC"`

### 4. Add Tag
- Action: `addTag(tagName: String)`
- Update: Appends tag to CSV string `tags`, `updated_at = now()`, `sync_status = "PENDING_SYNC"`

### 5. Remove Tag
- Action: `removeTag(tagName: String)`
- Update: Removes tag from CSV string `tags`, `updated_at = now()`, `sync_status = "PENDING_SYNC"`

### 6. Move Collection
- Action: `moveCollection(newCollectionId: String)`
- Update: `collection_id = newCollectionId`, updates `link_count` on both old and new collections, `updated_at = now()`, `sync_status = "PENDING_SYNC"`

### 7. Delete Bookmark
- Action: `deleteBookmark()`
- Update: Removes record from `bookmarks_table`, decrements `link_count` on parent collection, queues deletion sync event.
