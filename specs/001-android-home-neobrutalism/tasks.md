---
description: "Task list for Native Android Neobrutalism Home Screen feature implementation"
---

# Tasks: Native Android Neobrutalism Home Screen

**Input**: Design documents from `specs/001-android-home-neobrutalism/` (`plan.md`, `spec.md`, `data-model.md`, `contracts/`, `research.md`, `quickstart.md`)

**Prerequisites**: plan.md (required), spec.md (required for user stories), data-model.md, contracts/, research.md, quickstart.md

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (`[US1]`, `[US2]`, `[US3]`, `[US4]`)
- Explicit file paths included in all descriptions.

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure setup for Android Jetpack Compose application

- [x] T001 Create Android Jetpack Compose project directory structure per plan in `android/app/src/main/java/com/madruga665/bookmarks/`
- [x] T002 Configure Gradle dependencies for Jetpack Compose, Material3, Navigation Compose, Room, DataStore, and Hilt in `android/app/build.gradle.kts`
- [x] T003 [P] Configure Ktlint and Compose linting tools in `android/build.gradle.kts`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core design system, theme tokens, database entities, and repositories required by all user stories

- [x] T004 Implement Color palette tokens for Light (Design Print) and Dark (Catppuccin Mocha) in `android/app/src/main/java/com/madruga665/bookmarks/ui/theme/Color.kt`
- [x] T005 [P] Implement Neobrutalism Typography in `android/app/src/main/java/com/madruga665/bookmarks/ui/theme/Type.kt`
- [x] T006 [P] Implement custom `neobrutalistShadow` and solid border Modifiers in `android/app/src/main/java/com/madruga665/bookmarks/ui/theme/Modifiers.kt`
- [x] T007 Implement `NeobrutalismTheme` composition local provider in `android/app/src/main/java/com/madruga665/bookmarks/ui/theme/Theme.kt`
- [x] T008 Implement Room Database entity models (`CollectionEntity`, `BookmarkEntity`) in `android/app/src/main/java/com/madruga665/bookmarks/data/local/Entities.kt`
- [x] T009 Setup Room DAOs (`CollectionDao`, `BookmarkDao`) and Database in `android/app/src/main/java/com/madruga665/bookmarks/data/local/AppDatabase.kt`
- [x] T010 Setup Theme DataStore preference repository in `android/app/src/main/java/com/madruga665/bookmarks/data/repository/ThemeRepository.kt`

**Checkpoint**: Core foundation ready - user story implementation can now begin.

---

## Phase 3: User Story 1 - Neobrutalism Home Screen & Dual-Theme Experience (Priority: P1) 🎯 MVP

**Goal**: Render the base Neobrutalism Home Screen shell with top bar, title headline, and dynamic Light / Catppuccin Mocha theme switching.

**Independent Test**: Launch app in Light and Dark device modes; verify off-white/Mocha backgrounds, solid black/Crust outlines, zero-blur offset shadows, and theme responsiveness.

### Implementation & Tests for User Story 1

- [x] T011 [P] [US1] Create unit tests for `ThemeRepository` and theme mode switching in `android/app/src/test/java/com/madruga665/bookmarks/data/repository/ThemeRepositoryTest.kt`
- [x] T012 [P] [US1] Implement `NeobrutalistButton` composable with press elevation animation in `android/app/src/main/java/com/madruga665/bookmarks/ui/components/NeobrutalistButton.kt`
- [x] T013 [P] [US1] Implement `HomeScreenTopBar` composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/components/HomeScreenTopBar.kt`
- [x] T014 [US1] Implement `HomeHeroHeadline` composable ("Save now. Find anytime.") in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/components/HomeHeroHeadline.kt`
- [x] T015 [US1] Assemble `HomeScreen` shell composable supporting Light and Catppuccin Mocha Dark themes in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/HomeScreen.kt`
- [x] T016 [US1] Create Compose UI preview and layout tests for `HomeScreen` shell in `android/app/src/androidTest/java/com/madruga665/bookmarks/ui/home/HomeScreenThemeTest.kt`

**Checkpoint**: User Story 1 functional and independently testable.

---

## Phase 4: User Story 2 - Quick Link Capture from Home Screen (Priority: P1) 🎯 MVP

**Goal**: Provide URL text input bar with clipboard auto-paste button and Add (`+`) action button for frictionless link saving.

**Independent Test**: Copy URL to clipboard, tap paste button to populate input, tap Add button to save bookmark.

### Implementation & Tests for User Story 2

- [x] T017 [P] [US2] Create unit tests for Quick Save URL validation and bookmark creation in `android/app/src/test/java/com/madruga665/bookmarks/ui/home/HomeViewModelQuickSaveTest.kt`
- [x] T018 [P] [US2] Implement `NeobrutalistTextField` composable with `#` icon prefix and paste action in `android/app/src/main/java/com/madruga665/bookmarks/ui/components/NeobrutalistTextField.kt`
- [x] T019 [US2] Implement `QuickSaveBar` composable combining input field, paste button, and yellow Add (`+`) button in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/components/QuickSaveBar.kt`
- [x] T020 [US2] Implement `BookmarkRepository` to handle local Room persistence and API quick save sync in `android/app/src/main/java/com/madruga665/bookmarks/data/repository/BookmarkRepository.kt`
- [x] T021 [US2] Integrate `QuickSaveBar` into `HomeViewModel` and `HomeScreen` in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/HomeViewModel.kt`
- [x] T022 [US2] Add Android UI test for clipboard paste and Add button interaction in `android/app/src/androidTest/java/com/madruga665/bookmarks/ui/home/QuickSaveInteractionTest.kt`

**Checkpoint**: User Stories 1 & 2 functional - complete MVP link capture flow ready.

---

## Phase 5: User Story 3 - Collection Browsing & Search Navigation (Priority: P2)

**Goal**: Render "My Collections" section with tabbed folder cards (icon square, subtext, title, tab color) and handle Search and Folder click navigation.

**Independent Test**: Verify folder cards display correct link counts and colors; tap folder card or search icon to trigger navigation handlers.

### Implementation & Tests for User Story 3

- [x] T023 [P] [US3] Create unit tests for `CollectionRepository` link counts and list queries in `android/app/src/test/java/com/madruga665/bookmarks/data/repository/CollectionRepositoryTest.kt`
- [x] T024 [P] [US3] Implement `NeobrutalistFolderCard` tabbed folder composable in `android/app/src/main/java/com/madruga665/bookmarks/ui/components/NeobrutalistFolderCard.kt`
- [x] T025 [US3] Implement `MyCollectionsGrid` composable rendering folder cards ("IA", "Vagas", "Programação") in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/components/MyCollectionsGrid.kt`
- [x] T026 [US3] Connect collection state and click handlers in `HomeViewModel` in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/HomeViewModel.kt`
- [x] T027 [US3] Wire navigation callbacks (`onNavigateToSearch`, `onCollectionClick`) into main Navigation graph in `android/app/src/main/java/com/madruga665/bookmarks/ui/navigation/NavGraph.kt`
- [x] T028 [US3] Add Compose UI test verifying folder card rendering and search click trigger in `android/app/src/androidTest/java/com/madruga665/bookmarks/ui/home/CollectionsAndSearchNavigationTest.kt`

**Checkpoint**: User Stories 1, 2, and 3 functional independently.

---

## Phase 6: User Story 4 - App Settings & Collection Management Triggers (Priority: P3)

**Goal**: Connect top-left Settings gear icon and top-right Manage Collections folder icon to respective navigation destinations.

**Independent Test**: Tap top-left Settings button and top-right Manage Collections button; verify callbacks fire.

### Implementation & Tests for User Story 4

- [x] T029 [P] [US4] Connect Settings and Manage Collections action handlers in `HomeScreenTopBar` in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/components/HomeScreenTopBar.kt`
- [x] T030 [US4] Wire Settings (`onNavigateToSettings`) and Manage Collections (`onNavigateToManageCollections`) navigation routes in `android/app/src/main/java/com/madruga665/bookmarks/ui/navigation/NavGraph.kt`
- [x] T031 [US4] Add Compose UI test verifying top-bar action button clicks in `android/app/src/androidTest/java/com/madruga665/bookmarks/ui/home/TopBarActionsTest.kt`

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Dependency injection wiring, end-to-end quickstart validation, and final code polish

- [x] T032 [P] Implement Hilt Dependency Injection modules in `android/app/src/main/java/com/madruga665/bookmarks/di/AppModule.kt`
- [x] T033 Validate quickstart test suite scenario execution per `specs/001-android-home-neobrutalism/quickstart.md`
- [x] T034 Code cleanup, Compose preview documentation, and accessibility semantics validation

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately.
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories.
- **User Stories (Phase 3+)**: All depend on Foundational phase completion.
  - User Stories can proceed sequentially by priority (US1 → US2 → US3 → US4) or in parallel.
- **Polish (Phase 7)**: Depends on completion of desired user stories.

### Parallel Opportunities Per User Story

- **User Story 1**: T011 (ThemeRepositoryTest), T012 (NeobrutalistButton), T013 (HomeScreenTopBar) can run in parallel.
- **User Story 2**: T017 (HomeViewModelQuickSaveTest) and T018 (NeobrutalistTextField) can run in parallel.
- **User Story 3**: T023 (CollectionRepositoryTest) and T024 (NeobrutalistFolderCard) can run in parallel.
- **User Story 4**: T029 (HomeScreenTopBar handlers) can run in parallel.

---

## Implementation Strategy

### MVP First (User Stories 1 & 2)

1. Complete Phase 1 (Setup) and Phase 2 (Foundational).
2. Complete Phase 3 (US1 - Neobrutalism Home Screen & Themes) and Phase 4 (US2 - Quick Link Capture).
3. **STOP and VALIDATE**: Verify home screen rendering and link paste/saving end-to-end.

### Full Feature Incremental Delivery

1. Foundation ready → Add US1 (Visual Home Shell + Dual Theme).
2. Add US2 → Quick Save Bar + Clipboard Paste (MVP Ready!).
3. Add US3 → Collection Cards Grid + Search Navigation.
4. Add US4 → Settings & Collection Management Triggers.
