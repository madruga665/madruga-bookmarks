# Phase 0 Research: Collection Long-Press Actions Menu

## Technical Context & Decisions

### 1. Long-Press Gesture Detection
- **Decision**: Use `@OptIn(ExperimentalFoundationApi::class) Modifier.combinedClickable(onClick = onClick, onLongClick = onLongClick)` on `NeobrutalistFolderCard`.
- **Rationale**: `combinedClickable` is the official Compose Foundation API for handling tap and long-press gestures seamlessly without interfering with list scrolling or accessibility semantics.
- **Alternatives Considered**: Custom `pointerInput` with `detectTapGestures`. Rejected because custom pointer input breaks ripple animations, accessibility focus, and container click semantics.

### 2. Floating Context Menu Visual Layout & Anchoring
- **Decision**: Implement a custom `CollectionActionsOverlay` composable using a full-screen dimmed backdrop (`Box` with `Color.Black.copy(alpha = 0.6f)`) that renders the selected `NeobrutalistFolderCard` with three floating circular action buttons (Edit, Share, Delete) positioned around the top-right corner, matching `Screenshot_20260811_183702_Tuckii.jpg`.
- **Rationale**: Matches the exact custom Neobrutalist overlay design in the provided screenshot, where action buttons float directly above/around the card top-right border with 2.5dp black borders and offset shadows.
- **Alternatives Considered**: Standard Material3 `DropdownMenu` or `BottomSheet`. Rejected because standard dropdowns do not conform to the specified floating circle Neobrutalist design.

### 3. Collection Edit Functional Workflow
- **Decision**: Implement `EditCollectionDialog` modal allowing users to edit collection title, select color accent (Yellow, Purple, Orange, Blue), and select icon key (Code, Work, etc.), backed by `HomeViewModel.updateCollection(id, name, colorAccent, iconKey)`.
- **Rationale**: Direct persistence via `CollectionDao.updateCollection(...)` updates Room storage, automatically triggering UI updates across screens via reactive `Flow<List<CollectionEntity>>`.
- **Alternatives Considered**: Navigating to a separate full-screen page. Rejected because inline modal dialog provides faster micro-interactions.

### 4. Native OS Share Integration
- **Decision**: Trigger Android platform share sheet using `Intent(Intent.ACTION_SEND)` pre-loaded with `text/plain` MIME type, collection title, and share link (`https://tuckii.app/c/{id}`).
- **Rationale**: Integrates natively with Android OS share sheet (Constitution Principle II), enabling users to share collection links directly to any installed messaging, email, or social app.
- **Alternatives Considered**: Copy link to clipboard only. Rejected because native share sheet includes copy-to-clipboard along with direct app target sharing.

### 5. Collection Deletion & Safeguard Strategy
- **Decision**: Display `DeleteCollectionConfirmationDialog` upon tapping Delete. Upon user confirmation, `HomeViewModel.deleteCollection(collectionId)` executes `CollectionDao.deleteCollection(collectionId)` while updating associated bookmarks to `collection_id = NULL` (unorganized bookmarks).
- **Rationale**: Protects bookmarks from accidental deletion when deleting a folder container (Constitution Principle III).
- **Alternatives Considered**: Hard cascade delete of all bookmarks inside the collection without prompt. Rejected because deleting bookmarks without explicit user intent breaks data safety principles.
