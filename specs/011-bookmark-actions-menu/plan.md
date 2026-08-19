# Implementation Plan: Bookmark Long-Press Actions Menu

**Branch**: `011-bookmark-actions-menu` | **Date**: 2026-08-18 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/011-bookmark-actions-menu/spec.md`

## Summary

Implement long-press gesture (~350ms touch-and-hold) on bookmark cards across the Android app (`CollectionDetailScreen`, `SearchScreen`, and all bookmark list surfaces) to launch a floating Neobrutalist radial/arc contextual actions menu overlay containing four fully functional action triggers: **Abrir** (Open in browser), **Pinnar / Desafixar** (Toggle Pin), **Compartilhar** (Share intent), and **Excluir** (Delete with confirmation dialog).

## Technical Context

**Language/Version**: Kotlin 2.2.10 / Android SDK 35 (JVM Target 17)  
**Primary Dependencies**: Jetpack Compose, Material3, Room Database, Hilt, androidx.compose.foundation  
**Storage**: Room Database (`BookmarkDao`, `BookmarkEntity`, Local SQLite)  
**Testing**: JUnit 4, MockK, kotlinx-coroutines-test, Compose UI Test  
**Target Platform**: Android 8.0+ (API 26+)  
**Project Type**: Mobile App (Android)  
**Performance Goals**: Radial menu trigger & backdrop overlay rendering <100ms; database pin/delete reflection <150ms  
**Constraints**: Neobrutalism Design System (2.5dp black borders, offset shadows, high-contrast palette), Constitution Principles I, II, III, IV, V  
**Scale/Scope**: 1 bookmark card component update (`NeobrutalistBookmarkCard`), 1 new overlay component (`BookmarkActionsOverlay`), 1 shared delete dialog (`DeleteConfirmationDialog`), 2 screen/viewmodel integrations (`CollectionDetailScreen`/`ViewModel`, `SearchScreen`/`ViewModel`)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. API-First & Cross-Platform Sync**: All bookmark pin toggles and deletions persist locally to Room database first and emit Flow updates.
- [x] **II. Frictionless Capture & OS Share Target Integration**: Selecting Share triggers Android native OS `Intent.ACTION_SEND` share sheet with bookmark title and URL.
- [x] **III. Flexible Folder Organization**: Pinning instantly updates the collection layout; deleting removes the bookmark cleanly.
- [x] **IV. Dedicated Search & Instant Discovery**: Bookmark actions work seamlessly on `SearchScreen` and `CollectionDetailScreen`.
- [x] **V. Cross-Platform UI Consistency & Offline Resiliency**: Overlay floating action buttons adhere strictly to Neobrutalist design tokens (2.5dp borders, 3dp offset shadow) and function completely offline.

## Project Structure

### Documentation (this feature)

```text
specs/011-bookmark-actions-menu/
├── spec.md              # Feature specification
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
│   ├── ui-actions-contract.md
│   └── bookmark-repository-contract.md
└── checklists/
    └── requirements.md
```

### Source Code (repository root)

```text
app/src/main/java/com/madruga665/bookmarks/
├── data/
│   ├── local/
│   │   ├── BookmarkDao.kt
│   │   └── Entities.kt (BookmarkEntity)
│   └── repository/
│       └── BookmarkRepository.kt
├── ui/
│   ├── components/
│   │   ├── NeobrutalistBookmarkCard.kt       # Updated with gesture recognizer (long-press, drag, release)
│   │   ├── BookmarkActionsOverlay.kt         # New radial floating actions overlay with 4 options
│   │   └── DeleteConfirmationDialog.kt       # Shared confirmation dialog for deletion
│   ├── collection/
│   │   ├── CollectionDetailScreen.kt         # Integrated with BookmarkActionsOverlay and action handlers
│   │   ├── CollectionDetailUiState.kt        # Extended with active bookmark menu state
│   │   └── CollectionDetailViewModel.kt      # Handles togglePin and deleteBookmark
│   └── search/
│       ├── SearchScreen.kt                   # Integrated with BookmarkActionsOverlay in search results
│       ├── SearchUiState.kt                  # Extended with active bookmark menu state
│       └── SearchViewModel.kt                # Handles togglePin and deleteBookmark
```

**Structure Decision**: Android Jetpack Compose Clean Architecture integration in `app/src/main/java/com/madruga665/bookmarks/`.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|---|---|---|
| None | N/A | Fully compliant with Constitution and architectural constraints. |
