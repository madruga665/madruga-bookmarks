---
description: "Task list for Add Bookmark Bottom Sheet & Collection Selector Modal feature implementation"
---

# Tasks: Add Bookmark Bottom Sheet & Collection Selector Modal

**Input**: Design documents from `specs/002-save-bookmark-modal/` (`plan.md`, `spec.md`, `data-model.md`, `contracts/`, `research.md`, `quickstart.md`)

**Prerequisites**: plan.md (required), spec.md (required for user stories), data-model.md, contracts/, research.md, quickstart.md

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (`[US1]`, `[US2]`, `[US3]`, `[US4]`)
- Explicit file paths included in all descriptions.

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Database schema updates for `is_pinned` column and model extensions

- [x] T001 Update Room Database schema for `is_pinned` column in `android/app/src/main/java/com/madruga665/bookmarks/data/local/Entities.kt`
- [x] T002 [P] Update `AppDatabase` version and fallback migration strategy in `android/app/src/main/java/com/madruga665/bookmarks/data/local/AppDatabase.kt`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Repository updates and UI state model definition required by all user stories

- [x] T003 Update `BookmarkRepository` to support `isPinned` parameter in `android/app/src/main/java/com/madruga665/bookmarks/data/repository/BookmarkRepository.kt`
- [x] T004 [P] Add `insertCollection` method in `CollectionRepository` in `android/app/src/main/java/com/madruga665/bookmarks/data/repository/CollectionRepository.kt`
- [x] T005 [P] Create `SaveBookmarkModalUiState` model in `android/app/src/main/java/com/madruga665/bookmarks/ui/savemodal/SaveBookmarkModalUiState.kt`

**Checkpoint**: Core foundation ready - user story implementation can now begin.

---

## Phase 3: User Story 1 - Save Bookmark Bottom Sheet Modal (Priority: P1) 🎯 MVP

**Goal**: Render the Neobrutalist bottom sheet container displaying the drag handle, title "Save to Bookmarks", target URL, and default "Unsorted" selection.

**Independent Test**: Trigger quick save or share; verify bottom sheet modal slides up with drag handle, URL, and default save button.

### Implementation & Tests for User Story 1

- [x] T006 [P] [US1] Create unit tests for `SaveBookmarkViewModel` initial modal state in `android/app/src/test/java/com/madruga665/bookmarks/ui/savemodal/SaveBookmarkViewModelTest.kt`
- [x] T007 [P] [US1] Implement `SaveBookmarkViewModel` managing bottom sheet visibility and state in `android/app/src/main/java/com/madruga665/bookmarks/ui/savemodal/SaveBookmarkViewModel.kt`
- [x] T008 [US1] Implement base `SaveBookmarkBottomSheet` composable container in `android/app/src/main/java/com/madruga665/bookmarks/ui/savemodal/SaveBookmarkBottomSheet.kt`
- [x] T009 [US1] Connect quick save bar submit to open `SaveBookmarkBottomSheet` in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/HomeScreen.kt`
- [x] T010 [US1] Add Compose UI preview and layout tests for `SaveBookmarkBottomSheet` in `android/app/src/androidTest/java/com/madruga665/bookmarks/ui/savemodal/SaveBookmarkBottomSheetTest.kt`

**Checkpoint**: User Story 1 functional and independently testable.

---

## Phase 4: User Story 2 - Select Destination Collection Folder (Priority: P1) 🎯 MVP

**Goal**: Render selectable collection list inside modal with yellow highlight + checkmark, updating primary action button text `Save to "[Collection]"`.

**Independent Test**: Tap folder card in modal; verify yellow container highlight and main save button label updates.

### Implementation & Tests for User Story 2

- [x] T011 [P] [US2] Implement `NeobrutalistSelectableFolderCard` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/components/NeobrutalistSelectableFolderCard.kt`
- [x] T012 [US2] Integrate folder list selection into `SaveBookmarkBottomSheet` in `android/app/src/main/java/com/madruga665/bookmarks/ui/savemodal/SaveBookmarkBottomSheet.kt`
- [x] T013 [US2] Add folder selection logic and dynamic save button text in `SaveBookmarkViewModel` in `android/app/src/main/java/com/madruga665/bookmarks/ui/savemodal/SaveBookmarkViewModel.kt`
- [x] T014 [US2] Add Compose UI test for folder selection and button label change in `android/app/src/androidTest/java/com/madruga665/bookmarks/ui/savemodal/FolderSelectionTest.kt`

**Checkpoint**: User Stories 1 & 2 functional - complete MVP link capture & folder picker ready.

---

## Phase 5: User Story 3 - Create New Collection from Modal (Priority: P2)

**Goal**: Provide top-right "New Folder" icon button and inline creation form (`InlineCreateFolderForm`) that creates and auto-selects new folder.

**Independent Test**: Tap top-right button, type folder name, click Create Folder; verify new folder is created and auto-selected.

### Implementation & Tests for User Story 3

- [x] T015 [P] [US3] Create unit tests for inline folder creation in `android/app/src/test/java/com/madruga665/bookmarks/ui/savemodal/InlineFolderCreationTest.kt`
- [x] T016 [P] [US3] Implement `InlineCreateFolderForm` composable with folder name field and accent color picker in `android/app/src/main/java/com/madruga665/bookmarks/ui/savemodal/components/InlineCreateFolderForm.kt`
- [x] T017 [US3] Integrate top-right "New Folder" action button and `InlineCreateFolderForm` into `SaveBookmarkBottomSheet` in `android/app/src/main/java/com/madruga665/bookmarks/ui/savemodal/SaveBookmarkBottomSheet.kt`
- [x] T018 [US3] Add folder creation and auto-selection handling in `SaveBookmarkViewModel` in `android/app/src/main/java/com/madruga665/bookmarks/ui/savemodal/SaveBookmarkViewModel.kt`
- [x] T019 [US3] Add Compose UI test verifying inline folder creation in `android/app/src/androidTest/java/com/madruga665/bookmarks/ui/savemodal/CreateFolderInteractionTest.kt`

**Checkpoint**: User Stories 1, 2, and 3 functional independently.

---

## Phase 6: User Story 4 - Pin Bookmark Toggle (Priority: P3)

**Goal**: Render "Pin this link" pushpin toggle row inside modal and persist `isPinned` state when bookmark is saved.

**Independent Test**: Toggle "Pin this link" pushpin row in modal; verify `isPinned` boolean is saved with bookmark.

### Implementation & Tests for User Story 4

- [x] T020 [P] [US4] Implement `PinLinkToggleRow` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/savemodal/components/PinLinkToggleRow.kt`
- [x] T021 [US4] Integrate `PinLinkToggleRow` into `SaveBookmarkBottomSheet` in `android/app/src/main/java/com/madruga665/bookmarks/ui/savemodal/SaveBookmarkBottomSheet.kt`
- [x] T022 [US4] Connect pin toggle state to `SaveBookmarkViewModel` in `android/app/src/main/java/com/madruga665/bookmarks/ui/savemodal/SaveBookmarkViewModel.kt`

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Dependency injection wiring, end-to-end quickstart validation, and final code polish

- [x] T023 [P] Update Hilt Dependency Injection bindings for `SaveBookmarkViewModel` in `android/app/src/main/java/com/madruga665/bookmarks/di/AppModule.kt`
- [x] T024 Validate quickstart test suite scenario execution per `specs/002-save-bookmark-modal/quickstart.md`
- [x] T025 Code cleanup, Compose preview documentation, and accessibility semantics validation

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately.
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories.
- **User Stories (Phase 3+)**: All depend on Foundational phase completion.
- **Polish (Phase 7)**: Depends on completion of desired user stories.

### Parallel Opportunities Per User Story

- **User Story 1**: T006 (SaveBookmarkViewModelTest), T007 (SaveBookmarkViewModel) can run in parallel.
- **User Story 2**: T011 (NeobrutalistSelectableFolderCard) can run in parallel.
- **User Story 3**: T015 (InlineFolderCreationTest) and T016 (InlineCreateFolderForm) can run in parallel.
- **User Story 4**: T020 (PinLinkToggleRow) can run in parallel.

---

## Implementation Strategy

### MVP First (User Stories 1 & 2)

1. Complete Phase 1 (Setup) and Phase 2 (Foundational).
2. Complete Phase 3 (US1 - Save Modal Shell) and Phase 4 (US2 - Select Folder Destination).
3. **STOP and VALIDATE**: Verify opening modal, picking folder, and saving link.

### Full Feature Incremental Delivery

1. Foundation ready → Add US1 (Modal Shell).
2. Add US2 → Folder Selection + Dynamic Button Label (MVP Ready!).
3. Add US3 → Inline Folder Creation.
4. Add US4 → Pin Link Toggle.
