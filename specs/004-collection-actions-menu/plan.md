# Implementation Plan: Collection Long-Press Actions Menu

**Branch**: `004-collection-actions-menu` | **Date**: 2026-08-12 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/004-collection-actions-menu/spec.md`

## Summary

Implement long-press gesture (>500ms touch-and-hold) on collection cards in the Android app to launch a floating Neobrutalist context actions menu overlay containing three fully functional action triggers: **Edit** (pencil), **Share** (share icon), and **Delete** (trash bin), matching `Screenshot_20260811_183702_Tuckii.jpg`.

## Technical Context

**Language/Version**: Kotlin 1.9+ / Android SDK 34  
**Primary Dependencies**: Jetpack Compose, Material3, Room Database, Hilt, androidx.compose.foundation  
**Storage**: Room Database (`CollectionDao`, `BookmarkDao`, Local SQLite)  
**Testing**: JUnit 4, Compose UI Test Framework  
**Target Platform**: Android 8.0+ (API 26+)  
**Project Type**: Mobile App (Android)  
**Performance Goals**: Menu trigger & backdrop overlay rendering <100ms; database edit/delete reflection <150ms  
**Constraints**: Neobrutalism Design System (2.5dp black borders, offset shadows, high contrast colors), Constitution Principles II, III, V  
**Scale/Scope**: 1 collection card component update (`NeobrutalistFolderCard`), 1 overlay component (`CollectionActionsOverlay`), 2 dialog components (`EditCollectionDialog`, `DeleteCollectionDialog`), 1 viewmodel state expansion (`HomeViewModel`)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. API-First & Cross-Platform Sync**: All collection edits and deletions persist locally to Room database first and emit Flow updates.
- [x] **II. OS Share Target Integration**: Tapping Share triggers Android native OS `Intent.ACTION_SEND` share sheet with collection name and link.
- [x] **III. Flexible Folder Organization**: Edit modal allows instant renaming and visual customizing. Delete confirmation safeguards bookmarks by default (moving to Unorganized state).
- [x] **IV. Dedicated Search & Instant Discovery**: Collection name edits instantly update search index and list views.
- [x] **V. Cross-Platform UI Consistency & Offline Resiliency**: Overlay floating action buttons adhere strictly to Neobrutalist design tokens (2.5dp borders, 4dp offset shadow) and function completely offline.

## Project Structure

### Documentation (this feature)

```text
specs/004-collection-actions-menu/
├── spec.md              # Feature specification
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
│   ├── ui-actions-contract.md
│   └── collection-repository-contract.md
└── checklists/
    └── requirements.md
```

### Source Code (repository root)

```text
android/app/src/main/java/com/madruga665/bookmarks/
├── data/
│   ├── local/
│   │   ├── CollectionDao.kt
│   │   ├── CollectionEntity.kt
│   │   └── BookmarkDao.kt
│   └── repository/
│       └── BookmarkRepository.kt
├── ui/
│   ├── components/
│   │   ├── NeobrutalistFolderCard.kt         # Updated with combinedClickable for onLongClick
│   │   ├── CollectionActionsOverlay.kt       # New floating actions overlay matching screenshot
│   │   ├── EditCollectionDialog.kt           # New dialog for editing collection name, color, icon
│   │   └── DeleteCollectionDialog.kt         # New confirmation dialog for collection deletion
│   └── home/
│       ├── HomeScreen.kt                     # Renders overlay and modals based on state
│       ├── HomeScreenUiState.kt              # Extended with active menu, edit, and delete states
│       └── HomeViewModel.kt                  # Handles updateCollection, deleteCollection, share Intent
```

**Structure Decision**: Android Jetpack Compose feature integration in `android/app/src/main/java/com/madruga665/bookmarks/`.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | Fully compliant with Constitution and architectural constraints. |
