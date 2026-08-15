# Tasks: Bookmark Details View

**Input**: Design documents from `specs/006-bookmark-details/`

**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Unit tests for `BookmarkDetailViewModel` and `BookmarkRepository` operations.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3, US4, US5)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Package structure and navigation routes setup for the bookmark details feature.

- [x] T001 Create bookmark details UI package structure at `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/` and `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/components/`
- [x] T002 [P] Define `BOOKMARK_DETAIL` route and helper `bookmarkDetail(bookmarkId: String)` in `android/app/src/main/java/com/madruga665/bookmarks/ui/navigation/NavRoutes.kt`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Database schema updates, DAO queries, repository methods, and metadata extraction.

**⚠️ CRITICAL**: Must be completed before UI user story implementation.

- [x] T003 Extend `BookmarkEntity` with `description: String?`, `notes: String?`, `tags: String = ""`, and `updated_at: Long` in `android/app/src/main/java/com/madruga665/bookmarks/data/local/Entities.kt`
- [x] T004 [P] Update `LinkMetadata` and `LinkMetadataExtractor` in `android/app/src/main/java/com/madruga665/bookmarks/data/remote/LinkMetadataExtractor.kt` to extract description from OpenGraph and HTML meta tags
- [x] T005 Update `AppDatabase` version to 4 and add `BookmarkDao` queries (`getBookmarkById`, `updateBookmark`, `deleteBookmarkById`) in `android/app/src/main/java/com/madruga665/bookmarks/data/local/AppDatabase.kt` (depends on T003)
- [x] T006 Extend `BookmarkRepository` in `android/app/src/main/java/com/madruga665/bookmarks/data/repository/BookmarkRepository.kt` with methods `getBookmarkById`, `updateTitle`, `updateNotes`, `addTag`, `removeTag`, `togglePin`, `moveToCollection`, `deleteBookmark`, and `refreshMetadata` (depends on T003, T004, T005)

**Checkpoint**: Foundation data layer ready - user story implementation can now begin.

---

## Phase 3: User Story 1 - Full-Screen Bookmark Details Navigation & Header Actions (Priority: P1) 🎯 MVP

**Goal**: Full-screen `BookmarkDetailScreen` with top bar showing platform/collection badge, Refresh, Share, Move, and Delete action buttons, with back navigation.

**Independent Test**: Tap a bookmark in a collection; verify navigation to `BookmarkDetailScreen` displaying the top bar and platform badge, and tapping Share launches the Android share sheet.

### Implementation for User Story 1

- [x] T007 [P] [US1] Create UI state data class in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/BookmarkDetailUiState.kt`
- [x] T008 [US1] Create `BookmarkDetailViewModel` in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/BookmarkDetailViewModel.kt` loading bookmark and collection details by ID (depends on T006, T007)
- [x] T009 [P] [US1] Create `BookmarkDetailTopBar` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/components/BookmarkDetailTopBar.kt` with platform badge and 4 Neobrutalist action buttons
- [x] T010 [US1] Create `BookmarkDetailScreen` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/BookmarkDetailScreen.kt` integrating top bar and back navigation (depends on T008, T009)
- [x] T011 [US1] Register `BOOKMARK_DETAIL` route in `android/app/src/main/java/com/madruga665/bookmarks/ui/navigation/NavGraph.kt` and update bookmark click in `CollectionDetailScreen.kt` to navigate to `NavRoutes.bookmarkDetail(bookmark.id)` (depends on T002, T010)

**Checkpoint**: User Story 1 complete and testable independently.

---

## Phase 4: User Story 2 - Hero Image Preview with Pin/Unpin Toggle (Priority: P1) 🎯 MVP

**Goal**: Render the Hero preview thumbnail with an overlay Pin/Unpin button, and display pinned bookmarks in a dedicated "PINNED" section on the collection screen.

**Independent Test**: In `BookmarkDetailScreen`, tap the Pin button on the hero image; return to the collection view and verify the bookmark appears in the "PINNED" section.

### Implementation for User Story 2

- [x] T012 [P] [US2] Create `BookmarkHeroSection` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/components/BookmarkHeroSection.kt` with full-width thumbnail, geometric fallback placeholder, and top-right overlay Pin button
- [x] T013 [US2] Connect `BookmarkHeroSection` and pin toggle action in `BookmarkDetailScreen.kt` and `BookmarkDetailViewModel.kt` (depends on T008, T012)
- [x] T014 [US2] Update `CollectionDetailScreen.kt` to partition links into "PINNED" and "ALL LINKS" sections, rendering the "PINNED" section header and grid when pinned links exist

**Checkpoint**: User Stories 1 AND 2 complete (Hero & Pinning functional).

---

## Phase 5: User Story 3 - Inline Title Editing & URL Action Card (Priority: P1) 🎯 MVP

**Goal**: Allow inline editing of the title with "Salvar" and "Cancelar" buttons, and provide a prominent yellow Neobrutalist URL card that launches the link in the browser.

**Independent Test**: Tap the pencil icon next to the title, edit and save the title; tap the yellow URL button to verify it launches the browser with the bookmark URL.

### Implementation for User Story 3

- [x] T015 [P] [US3] Create `BookmarkTitleSection` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/components/BookmarkTitleSection.kt` supporting display mode with edit button and inline edit mode with "Salvar" and "Cancelar" buttons
- [x] T016 [P] [US3] Create `BookmarkUrlCard` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/components/BookmarkUrlCard.kt` with yellow background, thick black borders, external link icon, and tap handler
- [x] T017 [US3] Integrate `BookmarkTitleSection` and `BookmarkUrlCard` into `BookmarkDetailScreen.kt` and wire title saving in `BookmarkDetailViewModel.kt` (depends on T010, T015, T016)

**Checkpoint**: User Stories 1, 2, and 3 complete (MVP fully functional).

---

## Phase 6: User Story 4 - Expandable Description, Personal Notes & Tag Management (Priority: P2)

**Goal**: Display expandable description text ("Show more" / "Show less"), manage tag chips with '+ Add' dialog and 'X' removal, and provide editable NOTES area with "Salvar" / "Cancelar".

**Independent Test**: Expand/collapse description, add a tag via '+ Add' dialog, delete a tag via 'X', and save notes in the NOTES area.

### Implementation for User Story 4

- [x] T018 [P] [US4] Create `BookmarkDescriptionSection` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/components/BookmarkDescriptionSection.kt` with "Show more" / "Show less" toggle for long descriptions
- [x] T019 [P] [US4] Create `BookmarkTagsSection` composable and `AddTagDialog` in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/components/BookmarkTagsSection.kt` with Neobrutalist tag chips, remove icon, and add dialog
- [x] T020 [P] [US4] Create `BookmarkNotesSection` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/components/BookmarkNotesSection.kt` with multi-line text input and "Salvar" / "Cancelar" controls
- [x] T021 [US4] Integrate description, tags, and notes sections into `BookmarkDetailScreen.kt` and wire actions in `BookmarkDetailViewModel.kt` (depends on T010, T018, T019, T020)

**Checkpoint**: User Story 4 complete.

---

## Phase 7: User Story 5 - Move Collection & Delete Bookmark (Priority: P2)

**Goal**: Move bookmark to a different collection via bottom sheet, and delete bookmark with Neobrutalist confirmation dialog.

**Independent Test**: Move a bookmark to another folder; tap delete, confirm in dialog, and verify bookmark is removed and screen pops back.

### Implementation for User Story 5

- [x] T022 [P] [US5] Create `MoveCollectionBottomSheet` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/components/MoveCollectionBottomSheet.kt` rendering all user collections with select action
- [x] T023 [P] [US5] Create `DeleteConfirmationDialog` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/components/DeleteConfirmationDialog.kt` with Neobrutalist "Excluir" and "Cancelar" buttons
- [x] T024 [US5] Wire move collection, delete confirmation, metadata refresh, and back-navigation on deletion in `BookmarkDetailScreen.kt` and `BookmarkDetailViewModel.kt` (depends on T010, T022, T023)

**Checkpoint**: User Story 5 complete.

---

## Phase 8: Polish & Cross-Cutting Concerns

**Purpose**: Unit testing, Neobrutalism UI polish, and end-to-end verification.

- [x] T025 [P] Create unit test suite in `android/app/src/test/java/com/madruga665/bookmarks/ui/bookmark/BookmarkDetailViewModelTest.kt` verifying state loading, title update, notes update, tag add/remove, pin toggle, move collection, and delete
- [x] T026 Execute `./gradlew test` in `android/` directory and verify all tests pass with 0 errors

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - starts immediately.
- **Foundational (Phase 2)**: Depends on Phase 1 - BLOCKS all UI user stories.
- **User Story 1 (Phase 3)**: Depends on Phase 2 (Foundational data layer).
- **User Story 2 (Phase 4)**: Depends on Phase 3 (Screen & ViewModel base).
- **User Story 3 (Phase 5)**: Depends on Phase 3 (Screen & ViewModel base).
- **User Story 4 (Phase 6)**: Depends on Phase 3 (Screen & ViewModel base).
- **User Story 5 (Phase 7)**: Depends on Phase 3 (Screen & ViewModel base).
- **Polish (Phase 8)**: Depends on User Stories 1-5 completion.

### Parallel Opportunities

- T002 (NavRoutes) can run in parallel with T001.
- T003 (Entities) and T004 (LinkMetadataExtractor) can run in parallel.
- T007 (UiState) and T009 (TopBar) can run in parallel.
- T015 (TitleSection) and T016 (UrlCard) can run in parallel.
- T018 (DescriptionSection), T019 (TagsSection), and T020 (NotesSection) can run in parallel.
- T022 (MoveSheet) and T023 (DeleteDialog) can run in parallel.
- T025 (ViewModelTest) can run in parallel once ViewModel implementation is complete.

---

## Implementation Strategy

### MVP First (User Stories 1, 2, and 3)

1. Complete Phase 1 (Setup) & Phase 2 (Foundational Room/Repository updates).
2. Complete Phase 3 (User Story 1: Top Bar & Screen Navigation).
3. Complete Phase 4 (User Story 2: Hero Preview & Pinning).
4. Complete Phase 5 (User Story 3: Inline Title Editing & URL Action Card).
5. **VALIDATE MVP**: Open bookmark from collection, verify top bar, hero thumbnail, pin toggle, inline title edit, and URL launching.

### Incremental Delivery

1. **MVP (Phases 1-5)**: Complete details view navigation, hero image with pin, title edit, and URL card.
2. **Increment 2 (Phase 6)**: Description expansion, tags management with '+ Add' dialog, and personal notes.
3. **Increment 3 (Phase 7)**: Move collection bottom sheet, delete confirmation dialog, and metadata refresh.
4. **Polish (Phase 8)**: Unit tests and gradle verification.
