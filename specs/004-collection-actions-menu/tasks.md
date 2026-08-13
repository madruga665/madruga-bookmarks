# Tasks: Collection Long-Press Actions Menu

**Input**: Design documents from `/specs/004-collection-actions-menu/`

**Prerequisites**: [plan.md](./plan.md) (required), [spec.md](./spec.md) (required for user stories), [research.md](./research.md), [data-model.md](./data-model.md), [contracts/](./contracts/)

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3, US4)
- Exact file paths are specified in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Database Dao extensions for collection updates and deletion handling

- [x] T001 Extend `CollectionDao` in `android/app/src/main/java/com/madruga665/bookmarks/data/local/AppDatabase.kt` with `@Query("UPDATE collections_table SET name = :name, color_accent = :colorAccent, icon_key = :iconKey, updated_at = :updatedAt WHERE id = :id")` and `@Query("DELETE FROM collections_table WHERE id = :collectionId")`.
- [x] T002 Extend `BookmarkDao` in `android/app/src/main/java/com/madruga665/bookmarks/data/local/AppDatabase.kt` with `@Query("UPDATE bookmarks_table SET collection_id = 'col_unsorted' WHERE collection_id = :collectionId")` to safeguard saved links when a collection is deleted.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Repository and ViewModel state management required before UI actions can be connected

- [x] T003 [P] Extend `CollectionRepository` in `android/app/src/main/java/com/madruga665/bookmarks/data/repository/CollectionRepository.kt` with `updateCollection(id, name, colorAccent, iconKey)` and `deleteCollection(collectionId)` methods.
- [x] T004 [P] Update `HomeScreenUiState` in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/HomeScreenUiState.kt` to include `activeMenuCollection`, `collectionToEdit`, and `collectionToDelete` fields in `HomeScreenUiState.Success`.
- [x] T005 Update `HomeViewModel` in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/HomeViewModel.kt` with overlay/modal state functions: `openActionsMenu()`, `dismissActionsMenu()`, `openEditDialog()`, `dismissEditDialog()`, `openDeleteDialog()`, `dismissDeleteDialog()`, `updateCollection()`, and `deleteCollection()`.

**Checkpoint**: Foundation ready - long-press menu and user story implementations can now begin.

---

## Phase 3: User Story 1 - Long-Press Gesture & Floating Actions Menu Trigger (Priority: P1) 🎯 MVP

**Goal**: Long-pressing a collection card (>500ms) dims background backdrop and presents 3 circular floating action buttons (Edit, Share, Delete) matching `Screenshot_20260811_183702_Tuckii.jpg`.

**Independent Test**: Perform a long-press on any collection card on Home Screen; verify background dims and 3 floating action buttons pop up. Tapping backdrop or back press dismisses overlay.

### Implementation for User Story 1

- [x] T006 [P] [US1] Add `onLongClick: () -> Unit` parameter and `@OptIn(ExperimentalFoundationApi::class) Modifier.combinedClickable(onClick = onClick, onLongClick = onLongClick)` to `NeobrutalistFolderCard` in `android/app/src/main/java/com/madruga665/bookmarks/ui/components/NeobrutalistFolderCard.kt`.
- [x] T007 [P] [US1] Create `CollectionActionsOverlay` component in `android/app/src/main/java/com/madruga665/bookmarks/ui/components/CollectionActionsOverlay.kt` rendering dimmed backdrop, highlighted collection card, and 3 floating circular Neobrutalist buttons (Edit pencil, Share icon, Delete trash bin) positioned around top-right card corner per screenshot layout.
- [x] T008 [US1] Pass `onCollectionLongClick: (CollectionEntity) -> Unit` from `MyCollectionsGrid` in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/components/MyCollectionsGrid.kt` down to `NeobrutalistFolderCard`.
- [x] T009 [US1] Integrate `CollectionActionsOverlay` into `HomeScreen` in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/HomeScreen.kt` displaying when `uiState.activeMenuCollection != null` and dismissing on backdrop tap or back press.

**Checkpoint**: At this point, User Story 1 is fully functional and testable independently.

---

## Phase 4: User Story 2 - Functional Collection Editing (Priority: P1) 🎯 MVP

**Goal**: Tapping Edit (pencil icon) in actions overlay opens `EditCollectionDialog` allowing name, icon, and accent color updates that persist to Room DB and update UI in real time.

**Independent Test**: Open actions menu, tap Edit, change name to "Inteligência Artificial", save, and verify collection name updates on card and database.

### Implementation for User Story 2

- [x] T010 [P] [US2] Create `EditCollectionDialog` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/components/EditCollectionDialog.kt` with fields for collection title, color accent picker, icon picker, and validation error messages.
- [x] T011 [US2] Connect Edit action button in `CollectionActionsOverlay` to trigger `HomeViewModel.openEditDialog(collection)`.
- [x] T012 [US2] Integrate `EditCollectionDialog` in `HomeScreen` in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/HomeScreen.kt` handling edit submission and real-time state updates.

**Checkpoint**: At this point, User Story 2 editing is fully functional.

---

## Phase 5: User Story 3 - Functional Collection Sharing (Priority: P1) 🎯 MVP

**Goal**: Tapping Share icon in actions overlay launches Android OS native share sheet containing collection title and link.

**Independent Test**: Open actions menu on any collection, tap Share, and verify Android OS Share Sheet appears with collection text and URL payload.

### Implementation for User Story 3

- [x] T013 [P] [US3] Create `ShareUtils.kt` utility in `android/app/src/main/java/com/madruga665/bookmarks/ui/utils/ShareUtils.kt` to build and launch Android native `Intent.ACTION_SEND` share sheet for a collection.
- [x] T014 [US3] Connect Share action button in `CollectionActionsOverlay` and `HomeScreen` in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/HomeScreen.kt` to invoke `ShareUtils.shareCollection(context, collection)`.

**Checkpoint**: At this point, User Story 3 sharing is fully functional.

---

## Phase 6: User Story 4 - Functional Collection Deletion with Confirmation (Priority: P1) 🎯 MVP

**Goal**: Tapping Delete (trash icon) in actions overlay opens confirmation dialog. Confirming deletes collection from database and removes card from grid.

**Independent Test**: Long-press collection card, tap Delete, confirm in dialog, and verify card disappears from grid and cannot be retrieved from storage.

### Implementation for User Story 4

- [x] T015 [P] [US4] Create `DeleteCollectionDialog` confirmation modal in `android/app/src/main/java/com/madruga665/bookmarks/ui/components/DeleteCollectionDialog.kt` featuring Neobrutalist design, warning prompt, and "Cancel" / "Delete" buttons.
- [x] T016 [US4] Connect Delete action button in `CollectionActionsOverlay` to trigger `HomeViewModel.openDeleteDialog(collection)`.
- [x] T017 [US4] Integrate `DeleteCollectionDialog` in `HomeScreen` in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/HomeScreen.kt` executing `HomeViewModel.deleteCollection(collectionId)` upon confirmation and updating collections grid.

**Checkpoint**: All user stories (US1, US2, US3, US4) are fully functional.

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Verification and final validation

- [x] T018 Run unit tests via `./gradlew testDebugUnitTest` in `android/` directory.
- [x] T019 Execute manual validation scenarios defined in `quickstart.md`.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: Can start immediately.
- **Foundational (Phase 2)**: Depends on Phase 1 completion - BLOCKS all User Stories.
- **User Story 1 (Phase 3)**: Depends on Phase 2 completion.
- **User Story 2 (Phase 4)**: Depends on Phase 2 completion & US1 overlay trigger.
- **User Story 3 (Phase 5)**: Depends on Phase 2 completion & US1 overlay trigger.
- **User Story 4 (Phase 6)**: Depends on Phase 2 completion & US1 overlay trigger.
- **Polish (Phase 7)**: Depends on completion of all user story phases.

### Parallel Opportunities

- T003 & T004 in Foundational phase can run in parallel.
- T006, T007 in User Story 1 can run in parallel.
- T010 in User Story 2, T013 in User Story 3, T015 in User Story 4 can run in parallel.

---

## Implementation Strategy

### MVP Scope (User Story 1)

1. Phase 1 Setup + Phase 2 Foundational.
2. Phase 3 User Story 1 (Long-press gesture + floating overlay trigger).
3. Validate gesture detection and floating overlay menu layout.

### Incremental Delivery

1. Deliver US1 (Overlay Menu Trigger).
2. Deliver US2 (Edit Collection Modal).
3. Deliver US3 (Share Intent Trigger).
4. Deliver US4 (Delete Confirmation Modal).
