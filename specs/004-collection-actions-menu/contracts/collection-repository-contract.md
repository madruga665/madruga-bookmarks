# Contract: Collection Repository Operations

## Methods

```kotlin
interface BookmarkRepository {
    // Existing methods...
    
    suspend fun updateCollection(collection: CollectionEntity)
    
    suspend fun deleteCollection(collectionId: String, keepBookmarks: Boolean = true)
}
```

### 1. `updateCollection(collection: CollectionEntity)`
- **Input**: Updated `CollectionEntity` instance.
- **Behavior**: Calls `CollectionDao.update(collection)`. Room notifies reactive `Flow<List<CollectionEntity>>`.
- **Error Handling**: Throws `IllegalArgumentException` if collection `name` is blank.

### 2. `deleteCollection(collectionId: String, keepBookmarks: Boolean)`
- **Input**: `collectionId` (String), `keepBookmarks` (Boolean, default `true`).
- **Behavior**:
  - Executes `CollectionDao.deleteCollectionById(collectionId)`.
  - If `keepBookmarks` is `true`: Executes `BookmarkDao.clearCollectionIdForBookmarks(collectionId)` (sets `collection_id = NULL`).
  - If `keepBookmarks` is `false`: Executes `BookmarkDao.deleteBookmarksByCollectionId(collectionId)`.
