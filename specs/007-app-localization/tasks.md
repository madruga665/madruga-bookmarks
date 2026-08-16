# Tasks: Full App Localization & Internationalization (i18n)

**Input**: Design documents from `specs/007-app-localization/`

**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Unit tests for language switching, settings repository persistence, and ViewModel event handling.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3, US4)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Resource directory structure setup.

- [x] T001 Ensure `res/values/` and `res/values-pt-rBR/` directory structures exist in `android/app/src/main/res/`

---

## Phase 2: Foundational (String Resources & Dynamic Locale Engine)

**Purpose**: Complete dictionary definitions and reactive Compose locale wrapper.

**⚠️ CRITICAL**: Must be completed before UI components are migrated to string resources.

- [x] T002 Define full English string resource dictionary in `android/app/src/main/res/values/strings.xml` covering all screens, actions, placeholders, dialogs, and errors
- [x] T003 [P] Define full Brazilian Portuguese string resource dictionary in `android/app/src/main/res/values-pt-rBR/strings.xml` matching 100% of keys in `values/strings.xml`
- [x] T004 Implement reactive dynamic locale provider in `android/app/src/main/java/com/madruga665/bookmarks/MainActivity.kt` using `CompositionLocalProvider` (updating `LocalConfiguration` and `LocalContext`) and `AppCompatDelegate.setApplicationLocales` (depends on T002, T003)

**Checkpoint**: Foundational string dictionaries and dynamic locale engine ready.

---

## Phase 3: User Story 1 & 2 - Screen-by-Screen UI Localization (Priority: P1) 🎯 MVP

**Goal**: Replace all hardcoded strings with `stringResource(...)` across all 6 core feature packages and enable instant runtime translation.

**Independent Test**: Switch language between English and Portuguese in Settings; verify every screen immediately displays corresponding translations.

### Implementation for User Story 1 & 2

- [x] T005 [P] [US1] Localize Home screen components in `android/app/src/main/java/com/madruga665/bookmarks/ui/home/HomeScreen.kt`, `CollectionsHeader.kt`, `QuickAddSection.kt`, and `HomeHeroCard.kt` using `stringResource(...)`
- [x] T006 [P] [US1] Localize Save Bookmark Modal components in `android/app/src/main/java/com/madruga665/bookmarks/ui/savemodal/SaveBookmarkBottomSheet.kt` and `SaveBookmarkViewModel.kt` (toast messages, folder hints)
- [x] T007 [P] [US1] Localize Collection Details components in `android/app/src/main/java/com/madruga665/bookmarks/ui/collection/CollectionDetailScreen.kt`, `CollectionHeader.kt`, and `EmptyCollectionContent.kt`
- [x] T008 [P] [US1] Localize Collection Actions Menu components in `android/app/src/main/java/com/madruga665/bookmarks/ui/collection/components/CollectionActionsBottomSheet.kt`, `EditCollectionModal.kt`, and `DeleteConfirmDialog.kt`
- [x] T009 [P] [US1] Localize Bookmark Details Screen components in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/BookmarkDetailScreen.kt`, `BookmarkDetailTopBar.kt`, `BookmarkTitleSection.kt`, `BookmarkDescriptionSection.kt`, `BookmarkTagsSection.kt`, `BookmarkNotesSection.kt`, `DeleteConfirmationDialog.kt`, and `MoveCollectionBottomSheet.kt`
- [x] T010 [P] [US1] Localize Settings Screen components in `android/app/src/main/java/com/madruga665/bookmarks/ui/settings/SettingsScreen.kt`, `LanguageSelectionDialog.kt`, and `ThemeSelectionDialog.kt`
- [x] T011 [US2] Verify dynamic instant language switching between Settings and all navigation destinations in `android/app/src/main/java/com/madruga665/bookmarks/ui/navigation/NavGraph.kt` and `MainActivity.kt` (depends on T004, T005, T006, T007, T008, T009, T010)

**Checkpoint**: User Stories 1 and 2 complete (Full App Localization MVP functional).

---

## Phase 4: User Story 3 & 4 - Plurals, Dates & Formatted Counters (Priority: P2)

**Goal**: Ensure formatted plural counters and date formatting respect the active app locale.

**Independent Test**: Inspect dates and link/subcollection counters in both languages to confirm proper singular/plural grammar and date formats.

### Implementation for User Story 3 & 4

- [x] T012 [P] [US4] Implement localized date formatting helper respecting active locale in `android/app/src/main/java/com/madruga665/bookmarks/ui/bookmark/components/BookmarkTitleSection.kt`
- [x] T013 [P] [US4] Implement localized link and subcollection count formatting strings in `android/app/src/main/java/com/madruga665/bookmarks/ui/collection/CollectionHeader.kt` and `CollectionDetailScreen.kt`
- [x] T014 [US3] Verify "System Default" option in `SettingsViewModel.kt` correctly maps to system configuration locale fallback

**Checkpoint**: User Stories 3 and 4 complete.

---

## Phase 5: Polish & Test Verification

**Purpose**: Unit test validation and zero-regression check.

- [x] T015 [P] Update unit tests in `android/app/src/test/java/com/madruga665/bookmarks/ui/settings/SettingsViewModelTest.kt` and `android/app/src/test/java/com/madruga665/bookmarks/data/repository/SettingsRepositoryTest.kt` for language switching
- [x] T016 Run `./gradlew testDebugUnitTest` and verify all tests pass with 0 errors

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: Can start immediately.
- **Foundational (Phase 2)**: Depends on Phase 1 - BLOCKS all UI localization tasks.
- **User Story 1 & 2 (Phase 3)**: Depends on Phase 2. All component localization tasks (T005-T010) can run in parallel.
- **User Story 3 & 4 (Phase 4)**: Depends on Phase 3.
- **Polish (Phase 5)**: Depends on Phase 3 and Phase 4 completion.

### Parallel Opportunities

- T002 and T003 (String resources) can be developed in parallel.
- T005, T006, T007, T008, T009, T010 (UI Screen localization) can all run in parallel across separate files.
- T012 and T013 can run in parallel.
- T015 (Unit tests) can run in parallel with polish tasks.

---

## Implementation Strategy

### MVP First (Phases 1 to 3)

1. Define comprehensive `strings.xml` and `values-pt-rBR/strings.xml`.
2. Implement dynamic reactive locale wrapper in `MainActivity.kt`.
3. Replace hardcoded strings across all 6 UI packages.
4. **VALIDATE MVP**: Switch between English and Portuguese in Settings; confirm all screens immediately reflect the selected language.

### Incremental Delivery

1. MVP (Phases 1-3): 100% string localization and instant switching.
2. Increment 2 (Phase 4): Date formatting and plural counters.
3. Polish (Phase 5): Full unit test suite verification.
