# Tasks: Collection Bookmarks List View

**Input**: Design documents from `specs/003-collection-bookmarks-list/`

**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Unit tests for ViewModel and DAO logic.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3, US4)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and navigation setup for collection detail view.

- [x] T001 Create collection feature package directory structure at `android/app/src/main/java/com/madruga665/bookmarks/ui/collection/`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Database DAO & Repository methods required for fetching collection & bookmark data.

**⚠️ CRITICAL**: No user story UI work can begin until database queries are available.

- [x] T002 Extend `BookmarkDao` in `android/app/src/main/java/com/madruga665/bookmarks/data/local/AppDatabase.kt` with `@Query("SELECT * FROM bookmarks_table WHERE collection_id = :collectionId ORDER BY is_pinned DESC, created_at DESC") fun getBookmarksByCollection(collectionId: String): Flow<List<BookmarkEntity>>`
- [x] T003 [P] Extend `CollectionDao` in `android/app/src/main/java/com/madruga665/bookmarks/data/local/AppDatabase.kt` with `@Query("SELECT * FROM collections_table WHERE id = :collectionId") fun getCollectionById(collectionId: String): Flow<CollectionEntity?>`
- [x] T004 Extend `BookmarkRepository` in `android/app/src/main/java/com/madruga665/bookmarks/data/repository/BookmarkRepository.kt` to expose `getBookmarksByCollection(collectionId: String)` (depends on T002)
- [x] T005 [P] Extend `CollectionRepository` in `android/app/src/main/java/com/madruga665/bookmarks/data/repository/CollectionRepository.kt` to expose `getCollectionById(collectionId: String)` (depends on T003)

**Checkpoint**: Foundation database layer ready - user story implementation can now begin.

---

## Phase 3: User Story 1 - Collection Header & Navigation (Priority: P1) 🎯 MVP

**Goal**: Render collection details top navigation bar with back action, title, subtitle counters ("2 links · 0 subcollections"), quick add link action pre-selecting the collection ID, and overflow options menu.

**Independent Test**: Navigate to `folder_detail/col_vagas` and confirm header displays "Vagas", "2 links · 0 subcollections", functional back button, and quick-add link button opening the modal.

### Implementation for User Story 1

- [x] T006 [P] [US1] Create UI state data class in `android/app/src/main/java/com/madruga665/bookmarks/ui/collection/CollectionDetailUiState.kt`
- [x] T007 [US1] Create `CollectionDetailViewModel` in `android/app/src/main/java/com/madruga665/bookmarks/ui/collection/CollectionDetailViewModel.kt` combining collection and bookmarks flows (depends on T004, T005, T006)
- [x] T008 [P] [US1] Create `CollectionHeader` composable component in `android/app/src/main/java/com/madruga665/bookmarks/ui/collection/CollectionHeader.kt` with Neobrutalist back button, title, subtitle counters, yellow quick-add link button, and options menu
- [x] T009 [US1] Wire `NavRoutes.FOLDER_DETAIL` in `android/app/src/main/java/com/madruga665/bookmarks/ui/navigation/NavGraph.kt` to instantiate `CollectionDetailViewModel` and render the collection header layout (depends on T007, T008)

**Checkpoint**: User Story 1 complete and testable independently.

---

## Phase 4: User Story 2 - Bookmark Cards Grid View inside Collection (Priority: P1) 🎯 MVP

**Goal**: Display all bookmarks within the collection inside a 2-column Neobrutalist grid labeled "ALL LINKS ([N])" with thumbnail previews, titles, platform badges (`@LinkedIn`), and tap-to-open URL capability.

**Independent Test**: Open a collection containing bookmarks and verify 2-column grid renders cards with Neobrutalist borders, thumbnails, titles, source badges, and tapping a card opens the link.

### Implementation for User Story 2

- [x] T010 [P] [US2] Create `NeobrutalistBookmarkCard` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/components/NeobrutalistBookmarkCard.kt` with 2.5dp solid black borders, 16dp rounded corners, shadow offset, top preview image container, title text, and source badge
- [x] T011 [US2] Build `CollectionDetailScreen` in `android/app/src/main/java/com/madruga665/bookmarks/ui/collection/CollectionDetailScreen.kt` incorporating `CollectionHeader`, "ALL LINKS (N)" section title, and a 2-column `LazyVerticalGrid` rendering `NeobrutalistBookmarkCard` items (depends on T008, T010)
- [x] T012 [US2] Add URL tap handler in `CollectionDetailScreen.kt` launching an external browser Intent or custom tab when a bookmark card is clicked
- [x] T013 [US2] Connect `CollectionDetailScreen` to `SaveBookmarkModal` in `android/app/src/main/java/com/madruga665/bookmarks/ui/navigation/NavGraph.kt` so tapping the header quick-add button opens the bottom sheet pre-filled with the active `collectionId` (depends on T009, T011)

**Checkpoint**: User Stories 1 AND 2 complete (MVP fully functional).

---

## Phase 5: User Story 3 - Empty Collection View (Priority: P2)

**Goal**: Render a Neobrutalist empty state illustration with "No bookmarks yet" text and an "Add Link" button when a collection contains 0 links.

**Independent Test**: Navigate to an empty collection and verify empty state banner is displayed with a call-to-action button that opens the bookmark modal.

### Implementation for User Story 3

- [x] T014 [P] [US3] Create `EmptyCollectionContent` composable component in `android/app/src/main/java/com/madruga665/bookmarks/ui/collection/EmptyCollectionContent.kt` with Neobrutalist empty state artwork, helper text, and CTA button
- [x] T015 [US3] Integrate `EmptyCollectionContent` into `CollectionDetailScreen.kt` when `bookmarks.isEmpty()` and `subcollections.isEmpty()` (depends on T011, T014)

**Checkpoint**: User Story 3 complete.

---

## Phase 6: User Story 4 - Subcollection Navigation & Filtering (Priority: P3)

**Goal**: Render nested subcollection cards in a "SUBCOLLECTIONS" section above links when `subcollectionCount > 0`, allowing users to tap into nested collections.

**Independent Test**: View a collection with subcollections, confirm subcollections section displays above links, and tapping a subcollection navigates to that subcollection's details view.

### Implementation for User Story 4

- [x] T016 [P] [US4] Add `subcollectionCount` and `parentId` support to `CollectionEntity` mapping in `android/app/src/main/java/com/madruga665/bookmarks/data/local/Entities.kt`
- [x] T017 [US4] Add subcollections rendering section in `CollectionDetailScreen.kt` above the "ALL LINKS" grid when subcollections are present, with click handler navigating to `NavRoutes.folderDetail(subcollectionId)` (depends on T011, T016)

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Unit testing, Neobrutalism theme verification, and quickstart validation.

- [x] T018 [P] Add unit test suite in `android/app/src/test/java/com/madruga665/bookmarks/ui/collection/CollectionDetailViewModelTest.kt` verifying state loading and bookmark filtering by collection ID
- [x] T019 Execute manual validation scenarios in `specs/003-collection-bookmarks-list/quickstart.md` and verify clean build with `./gradlew testDebugUnitTest`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: Can start immediately.
- **Foundational (Phase 2)**: Depends on Phase 1 - BLOCKS UI user stories.
- **User Story 1 (Phase 3)**: Depends on Phase 2.
- **User Story 2 (Phase 4)**: Depends on Phase 3 (Header & Navigation context).
- **User Story 3 (Phase 5)**: Depends on Phase 4 (Grid Screen).
- **User Story 4 (Phase 6)**: Depends on Phase 4 (Grid Screen).
- **Polish (Phase 7)**: Depends on User Stories 1-3 completion.

### Parallel Opportunities

- T003, T005 (Collection queries) can run in parallel with T002, T004 (Bookmark queries).
- T006 (UiState), T008 (Header component) can run in parallel.
- T010 (`NeobrutalistBookmarkCard`) can be implemented in parallel with ViewModel wiring.
- T014 (`EmptyCollectionContent`) and T018 (`CollectionDetailViewModelTest`) can run in parallel.

---

## Implementation Strategy

### MVP First (User Stories 1 & 2)

1. Complete Phase 1 & Phase 2 (Foundational DB queries).
2. Complete Phase 3 (Header & View Model navigation).
3. Complete Phase 4 (2-column Neobrutalist bookmark card grid).
4. **VALIDATE MVP**: Open collection view, verify 2-column grid and quick-add pre-selection.

### Incremental Delivery

1. MVP (Phases 1-4): Collection header + 2-column link card grid.
2. Increment 2 (Phase 5): Empty collection state.
3. Increment 3 (Phase 6): Nested subcollection navigation.
4. Polish (Phase 7): ViewModel tests & verification.
