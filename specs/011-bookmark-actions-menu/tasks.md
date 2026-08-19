# Tasks: Bookmark Long-Press Actions Menu

**Input**: Design documents from `/specs/011-bookmark-actions-menu/`

**Prerequisites**: [plan.md](./plan.md) (required), [spec.md](./spec.md) (required for user stories), [research.md](./research.md), [data-model.md](./data-model.md), [contracts/](./contracts/)

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3, US4, US5)
- Exact file paths are specified in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Define core enums, transient state data models, and localized string resources

- [x] T001 [P] Create `BookmarkOption` enum (`OPEN`, `PIN`, `SHARE`, `DELETE`) and `BookmarkActionsOverlayState` data class in `app/src/main/java/com/madruga665/bookmarks/ui/components/BookmarkOption.kt`.
- [x] T002 [P] Add localized string resources for bookmark actions (Open, Pin, Unpin, Share, Delete, toasts) in `app/src/main/res/values/strings.xml` and `app/src/main/res/values-en/strings.xml`.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Shared UI dialogs and ViewModel state holders for bookmark actions

- [x] T003 [P] Ensure shared `DeleteConfirmationDialog` is accessible from `app/src/main/java/com/madruga665/bookmarks/ui/components/DeleteConfirmationDialog.kt` with customizable title and message parameters.
- [x] T004 [P] Extend `CollectionDetailUiState` and `CollectionDetailViewModel` in `app/src/main/java/com/madruga665/bookmarks/ui/collection/CollectionDetailUiState.kt` and `app/src/main/java/com/madruga665/bookmarks/ui/collection/CollectionDetailViewModel.kt` to support active overlay state, `togglePin(bookmarkId)`, and `deleteBookmark(bookmarkId)`.
- [x] T005 [P] Extend `SearchUiState` and `SearchViewModel` in `app/src/main/java/com/madruga665/bookmarks/ui/search/SearchUiState.kt` and `app/src/main/java/com/madruga665/bookmarks/ui/search/SearchViewModel.kt` to support active overlay state, `togglePin(bookmarkId)`, and `deleteBookmark(bookmarkId)`.

**Checkpoint**: Foundation ready - UI gesture overlay and story actions can now be implemented.

---

## Phase 3: User Story 1 - Long-Press Gesture & Radial Floating Actions Menu Overlay (Priority: P1) 🎯 MVP

**Goal**: Pressing and holding any bookmark card for ~350ms activates a dimmed backdrop and presents 4 circular Neobrutalist buttons arranged in a radial arc with hover drag tracking and haptic feedback.

**Independent Test**: Long-press a bookmark card in `CollectionDetailScreen` or `SearchScreen`; verify overlay appears with elevated card and 4 radial action buttons. Dragging finger highlights hovered buttons with haptics. Releasing outside dismisses cleanly.

### Implementation for User Story 1

- [x] T006 [P] [US1] Update `NeobrutalistBookmarkCard` in `app/src/main/java/com/madruga665/bookmarks/ui/components/NeobrutalistBookmarkCard.kt` to support gesture detection via `pointerInput` (`awaitEachGesture`, ~350ms threshold, position reporting, drag tracking, release, and single-click fallback).
- [x] T007 [P] [US1] Create `BookmarkActionsOverlay` in `app/src/main/java/com/madruga665/bookmarks/ui/components/BookmarkActionsOverlay.kt` rendering dimmed backdrop, tilted highlighted bookmark card, and 4 radial arc floating action buttons (Abrir, Pinnar/Desafixar, Compartilhar, Excluir) with hover scaling, accent colors, and hit-detection.
- [x] T008 [US1] Integrate `BookmarkActionsOverlay` and gesture handlers in `CollectionDetailScreen` in `app/src/main/java/com/madruga665/bookmarks/ui/collection/CollectionDetailScreen.kt`.
- [x] T009 [US1] Integrate `BookmarkActionsOverlay` and gesture handlers in `SearchScreen`, `SearchResultsList`, and `RecentlySavedSection` in `app/src/main/java/com/madruga665/bookmarks/ui/search/SearchScreen.kt`.

**Checkpoint**: At this point, User Story 1 (radial overlay gesture trigger) is fully functional and testable independently.

---

## Phase 4: User Story 2 - Open Bookmark Action (Priority: P1) 🎯 MVP

**Goal**: Selecting "Abrir" from the radial menu immediately launches the bookmark URL in the default web browser or Custom Tab.

**Independent Test**: Long-press a bookmark card, release on "Abrir", and verify browser opens the bookmark URL.

### Implementation for User Story 2

- [x] T010 [P] [US2] Create URL opener utility `LinkOpener.kt` in `app/src/main/java/com/madruga665/bookmarks/ui/utils/LinkOpener.kt` to validate and launch browser `Intent.ACTION_VIEW` with fallback error toast.
- [x] T011 [US2] Connect `OPEN` action in `CollectionDetailScreen` and `SearchScreen` to invoke `LinkOpener.openUrl(context, bookmark.url)`.

**Checkpoint**: At this point, User Story 2 (Open) is fully functional.

---

## Phase 5: User Story 3 - Toggle Pin / Unpin Bookmark Action (Priority: P1) 🎯 MVP

**Goal**: Selecting "Pinnar" / "Desafixar" toggles the bookmark `isPinned` state in Room DB, immediately reordering the collection grid.

**Independent Test**: Long-press an unpinned card, select "Pinnar", and verify it moves to the Pinned header; long-press pinned card, select "Desafixar", and verify it moves back.

### Implementation for User Story 3

- [x] T012 [P] [US3] Ensure `BookmarkActionsOverlay` in `app/src/main/java/com/madruga665/bookmarks/ui/components/BookmarkActionsOverlay.kt` dynamically shows pushpin icon and "Fixar" vs "Desafixar" label based on `bookmark.isPinned`.
- [x] T013 [US3] Connect `PIN` action in `CollectionDetailScreen` and `SearchScreen` to call ViewModel `togglePin(bookmark.id)`.

**Checkpoint**: At this point, User Story 3 (Pin/Unpin) is fully functional.

---

## Phase 6: User Story 4 - Share Bookmark Action (Priority: P1) 🎯 MVP

**Goal**: Selecting "Compartilhar" opens Android OS native share sheet containing bookmark title and URL.

**Independent Test**: Long-press a bookmark card, select "Compartilhar", and verify Android OS share sheet opens with `"<Title> - <URL>"`.

### Implementation for User Story 4

- [x] T014 [P] [US4] Add `shareBookmark(context, bookmark)` helper in `app/src/main/java/com/madruga665/bookmarks/ui/utils/ShareUtils.kt` to create and launch `Intent.ACTION_SEND` chooser with formatted bookmark payload.
- [x] T015 [US4] Connect `SHARE` action in `CollectionDetailScreen` and `SearchScreen` to invoke `ShareUtils.shareBookmark(context, bookmark)`.

**Checkpoint**: At this point, User Story 4 (Share) is fully functional.

---

## Phase 7: User Story 5 - Delete Bookmark with Confirmation Dialog (Priority: P1) 🎯 MVP

**Goal**: Selecting "Excluir" displays confirmation dialog. Confirming permanently deletes the bookmark from Room DB and UI in real-time.

**Independent Test**: Long-press a bookmark card, select "Excluir", confirm in dialog, and verify card disappears immediately.

### Implementation for User Story 5

- [x] T016 [US5] Wire `DELETE` action in `CollectionDetailScreen` and `SearchScreen` to activate `bookmarkToDelete` state and render `DeleteConfirmationDialog`.
- [x] T017 [US5] Connect deletion confirmation in `CollectionDetailScreen` and `SearchScreen` to execute ViewModel `deleteBookmark(bookmark.id)`.

**Checkpoint**: All user stories (US1–US5) are fully functional.

---

## Phase 8: Polish & Verification

**Purpose**: Unit testing, regression validation, and edge case hardening

- [x] T018 [P] Write unit tests for `CollectionDetailViewModel` and `SearchViewModel` pin toggle and delete actions in `app/src/test/java/com/madruga665/bookmarks/ui/collection/CollectionDetailViewModelTest.kt` and `app/src/test/java/com/madruga665/bookmarks/ui/search/SearchViewModelTest.kt`.
- [x] T019 Execute `./gradlew testDebugUnitTest` to verify all unit tests pass with zero regressions.
