# Implementation Plan: Add Bookmark Bottom Sheet & Collection Selector Modal

**Branch**: `002-save-bookmark-modal` | **Date**: 2026-08-11 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/002-save-bookmark-modal/spec.md`

## Summary

Implement a Neobrutalist Add Bookmark Bottom Sheet Modal in Kotlin and Jetpack Compose. The modal presents the target URL, a "Pin this link" pushpin toggle, a scrollable list of available folder collections with a default selected "Unsorted" option, a top-right action button to create new folders inline, and a dynamic bottom save button `Save to "[Collection Name]"`.

## Technical Context

**Language/Version**: Kotlin 1.9+ / Java 17 (Android SDK 26+, Target SDK 34/35)

**Primary Dependencies**: Jetpack Compose (Material3 `ModalBottomSheet`), Navigation Compose, Room Database, StateFlow, Hilt DI

**Storage**: Room Database (`bookmarks_table`, `collections_table`)

**Testing**: JUnit 5 / JUnit 4, Compose UI Test Framework, MockK

**Target Platform**: Native Android Mobile (API Level 26+)

**Project Type**: Mobile App (Android Native)

**Performance Goals**: Smooth 60/120 fps bottom sheet animation, instant folder selection state updates (<50ms), inline folder creation <200ms

**Constraints**: Neobrutalism UI style (thick 2.5dp borders, zero-blur hard offset shadows, yellow fill for selected folder, checkmark icon), Light Theme matching reference screenshot, Catppuccin Mocha Dark Theme

**Scale/Scope**: `SaveBookmarkBottomSheet` composable, `SaveBookmarkViewModel`, `NeobrutalistSelectableFolderCard`, `InlineCreateFolderForm`, Room schema migration/update for `isPinned`

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Principle I: API-First & Cross-Platform Sync**: PASSED. REST schema contract defined in `contracts/bookmark-save-api-contract.json` aligns backend persistence.
- **Principle II: Frictionless Capture & OS Share Target**: PASSED. Opening modal populates target URL instantly with sensible default folder choice.
- **Principle III: Flexible Folder Organization**: PASSED. Core user scenario for picking folder destination during bookmark save or creating a folder inline.
- **Principle IV: Dedicated Search & Instant Discovery**: PASSED. Pinned links and collection tagging enhance searchability.
- **Principle V: Cross-Platform UI Consistency & Offline Resiliency**: PASSED. Neobrutalist bottom sheet adheres to dual theme tokens; saved items queue for offline persistence.

## Project Structure

### Documentation (this feature)

```text
specs/002-save-bookmark-modal/
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output (/speckit-plan command)
├── data-model.md        # Phase 1 output (/speckit-plan command)
├── quickstart.md        # Phase 1 output (/speckit-plan command)
├── contracts/           # Phase 1 output (/speckit-plan command)
│   ├── save-bookmark-modal-ui-contract.md
│   └── bookmark-save-api-contract.json
└── tasks.md             # Phase 2 output (/speckit-tasks command - NOT created by /speckit-plan)
```

### Source Code Layout (Android Native)

```text
android/
└── app/
    ├── src/
    │   ├── main/
    │   │   ├── java/com/madruga665/bookmarks/
    │   │   │   ├── ui/
    │   │   │   │   ├── components/
    │   │   │   │   │   └── NeobrutalistSelectableFolderCard.kt # Selectable folder card item
    │   │   │   │   └── savemodal/
    │   │   │   │       ├── SaveBookmarkBottomSheet.kt         # Bottom sheet composable
    │   │   │   │       ├── SaveBookmarkViewModel.kt           # ViewModel & StateFlow
    │   │   │   │       ├── SaveBookmarkModalUiState.kt        # UI state model
    │   │   │   │       └── InlineCreateFolderForm.kt          # Inline folder creation component
    │   │   │   ├── data/
    │   │   │   │   ├── local/                                 # Updated Bookmark & Collection entities/DAOs
    │   │   │   │   └── repository/                            # BookmarkRepository & CollectionRepository
    │   └── test/                                              # ViewModel & Repository Unit Tests
    └── androidTest/                                           # Compose UI Tests
```

**Structure Decision**: Mobile App structure using standard Android Jetpack architecture (UI composables, Theme design system, ViewModel, Data repositories, Room local cache).

## Complexity Tracking

*No violations detected. All design choices comply with project Constitution.*
