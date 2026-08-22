# Tasks: Subtle Neobrutalist Grid Background Pattern

**Input**: Design documents from `/specs/012-grid-background/`  
**Prerequisites**: plan.md, spec.md, data-model.md, contracts/ui-grid-background-contract.md, research.md, quickstart.md

## Format: `[ID] [P?] [Story] Description with file path`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to ([US1], [US2], [US3])
- Exact file paths included for all tasks

---

## Phase 1: Setup & Design Tokens

**Purpose**: Theme color tokens and defaults for the Neobrutalist grid pattern

- [x] T001 [P] Extend `NeobrutalismColors` data class with `gridLine: Color` token and define Light (`#E2E2E2`) and Catppuccin Mocha (`#28283D`) palettes in `app/src/main/java/com/madruga665/bookmarks/ui/theme/Color.kt`

---

## Phase 2: Foundational Modifier & Core Tests

**Purpose**: Core `Modifier.neobrutalistGridBackground` and unit tests

- [x] T002 Implement `Modifier.neobrutalistGridBackground(gridColor, gridSize, strokeWidth)` using `drawBehind` in `app/src/main/java/com/madruga665/bookmarks/ui/theme/Modifiers.kt`
- [x] T003 [P] Create theme and modifier unit tests in `app/src/test/java/com/madruga665/bookmarks/ui/theme/GridBackgroundTest.kt`

**Checkpoint**: Foundation ready — grid modifier and theme tokens are available and tested.

---

## Phase 3: User Story 1 - Discreet Grid Pattern on App Backgrounds (Priority: P1) 🎯 MVP

**Goal**: Apply fixed 24dp x 24dp subtle light gray grid canvas background to all primary app screens.

**Independent Test**: Launch the app in Light Mode, navigate to Home, Search, Collection Detail, Bookmark Detail, and Settings screens, and verify that the subtle 24dp x 24dp light gray grid pattern is visible on background canvas surfaces while content scrolls smoothly over it.

### Implementation for User Story 1

- [x] T004 [P] [US1] Apply `neobrutalistGridBackground` to the root `Box` in `app/src/main/java/com/madruga665/bookmarks/ui/home/HomeScreen.kt`
- [x] T005 [P] [US1] Apply `neobrutalistGridBackground` to the root `Box` in `app/src/main/java/com/madruga665/bookmarks/ui/search/SearchScreen.kt`
- [x] T006 [P] [US1] Apply `neobrutalistGridBackground` to the root `Box` in `app/src/main/java/com/madruga665/bookmarks/ui/collection/CollectionDetailScreen.kt`
- [x] T007 [P] [US1] Apply `neobrutalistGridBackground` to the root `Box` / container in `app/src/main/java/com/madruga665/bookmarks/ui/bookmark/BookmarkDetailScreen.kt`
- [x] T008 [P] [US1] Apply `neobrutalistGridBackground` to the root `Box` in `app/src/main/java/com/madruga665/bookmarks/ui/settings/SettingsScreen.kt`

**Checkpoint**: User Story 1 complete — all main screens render the fixed grid background.

---

## Phase 4: User Story 2 - Dark Mode (Catppuccin Mocha) Adaptive Grid (Priority: P2)

**Goal**: Ensure grid background dynamically adapts with subtle dark-slate contrast when toggling to Catppuccin Mocha Dark Mode.

**Independent Test**: Toggle to Dark Mode in Settings, verify grid lines render with subtle `#28283D` dark-slate contrast against `#1E1E2E` base across all screens.

### Implementation for User Story 2

- [x] T009 [US2] Verify dynamic dark mode theme integration and add test cases for `MochaDarkNeobrutalismColors.gridLine` in `app/src/test/java/com/madruga665/bookmarks/ui/theme/GridBackgroundTest.kt`

**Checkpoint**: User Stories 1 and 2 complete — grid works seamlessly across both light and dark themes.

---

## Phase 5: User Story 3 - Visual Hierarchy and Container Opacity (Priority: P3)

**Goal**: Verify that all cards, bottom sheets, dialogs, and text fields have solid surfaces occluding the grid lines cleanly.

**Independent Test**: Open modals, bottom sheets, and view cards on Home and Search to verify zero grid line bleed-through on solid components.

### Implementation for User Story 3

- [x] T010 [P] [US3] Verify and ensure solid surface backgrounds for cards, bottom sheets, and dialog overlays in `app/src/main/java/com/madruga665/bookmarks/ui/components/` and `app/src/main/java/com/madruga665/bookmarks/ui/savemodal/`

**Checkpoint**: All user stories complete and visual hierarchy preserved.

---

## Phase 6: Polish & Verification

**Purpose**: Automated test suite execution, static analysis, and final validation

- [x] T011 Run full unit test suite and static checks with `JAVA_HOME=/opt/android-studio/jbr ./gradlew test check`
- [x] T012 Validate end-to-end verification scenarios described in `specs/012-grid-background/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — start immediately (T001)
- **Foundational (Phase 2)**: Depends on T001 (T002, T003)
- **User Story 1 (Phase 3)**: Depends on Foundational Phase (T004-T008 can run in parallel)
- **User Story 2 (Phase 4)**: Depends on Foundational Phase and US1 (T009)
- **User Story 3 (Phase 5)**: Depends on US1 (T010)
- **Polish (Phase 6)**: Depends on all user stories being complete (T011, T012)

### Parallel Opportunities

- T004, T005, T006, T007, T008 in Phase 3 can run in parallel across separate screen files.
- T010 in Phase 5 can run in parallel with US2 verification.

---

## Implementation Strategy

### MVP First (User Story 1)
1. Complete Setup (T001) + Foundational (T002, T003).
2. Complete Screen Integrations (T004 - T008).
3. Validate MVP on Home & Search screens.

### Incremental Delivery
1. Foundation & Color tokens ready.
2. US1: Light Mode grid canvas applied to all screens.
3. US2: Dark Mode Catppuccin Mocha adaptation tested.
4. US3: Container opacity & visual hierarchy verified.
5. Full regression check (`./gradlew test check`).
