# Phase 0 Research: Collection Bookmarks List View

## Technical Context & Decisions

### 1. Data Query & Persistence Strategy
- **Decision**: Extend `BookmarkDao` with `@Query("SELECT * FROM bookmarks_table WHERE collection_id = :collectionId ORDER BY is_pinned DESC, created_at DESC") fun getBookmarksByCollection(collectionId: String): Flow<List<BookmarkEntity>>` and `CollectionDao` with `@Query("SELECT * FROM collections_table WHERE id = :collectionId") fun getCollectionById(collectionId: String): Flow<CollectionEntity?>`.
- **Rationale**: Direct Room `Flow` streams provide real-time updates when bookmarks are added to or modified in the current collection.
- **Alternatives Considered**: In-memory filtering of all bookmarks on UI side. Rejected because Room queries are index-optimized and scale better with large link counts.

### 2. UI Layout & Neobrutalism Design System Integration
- **Decision**: Build `CollectionDetailScreen` using `LazyVerticalGrid(GridCells.Fixed(2))` for the links grid ("ALL LINKS (N)") and a custom Neobrutalist top bar.
- **Rationale**: Matches exact layout requirements from `Screenshot_20260811_183643_Tuckii.jpg` with responsive 2-column card grid, high contrast borders (2.5dp solid stroke), rounded corners (16dp), thumbnail preview, and source platform pill badge.
- **Alternatives Considered**: Single column list. Rejected because 2-column card grid provides higher visual density and aligns with the design spec.

### 3. Quick Add Link Pre-Selection
- **Decision**: Tapping the top-right header add button in `CollectionDetailScreen` opens `SaveBookmarkModal` with `selectedCollectionId` pre-set to the active collection ID.
- **Rationale**: Seamlessly fulfills Constitution Principle III and feature requirement FR-003.

### 4. Subcollections Support
- **Decision**: Add optional `parent_id: String? = null` to `CollectionEntity` schema or model to support nested subcollections while providing 0 as default subcollection count for top-level folders.
- **Rationale**: Fulfills header subtitle counter requirement (`[N] links · [M] subcollections`) without breaking existing flat collection schemas.
