# Implementation Plan: Collection Bookmarks List View

**Branch**: `003-collection-bookmarks-list` | **Date**: 2026-08-12 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/003-collection-bookmarks-list/spec.md`

## Summary

Implement the collection detail view screen (`CollectionDetailScreen`) that lists bookmarks within a specific collection folder. The screen includes a Neobrutalist top bar with navigation back, collection title, link/subcollection counters, pre-selected quick-add link button, and collection options menu. Bookmarks are displayed under "ALL LINKS (N)" in a 2-column Neobrutalist grid of cards featuring thumbnails, titles, and platform source badges.

## Technical Context

**Language/Version**: Kotlin 1.9+, Java 17

**Primary Dependencies**: Android Jetpack Compose, Compose Navigation, Room Database, Kotlin Coroutines & Flow, Material 3

**Storage**: Local Room SQLite Database (`AppDatabase`, `BookmarkDao`, `CollectionDao`)

**Testing**: JUnit4, Kotlinx Coroutines Test, Compose UI Test

**Target Platform**: Android (API 26+)

**Project Type**: Mobile Application (Android)

**Performance Goals**: Render collection screen with up to 50 items in <200ms from local DB cache.

**Constraints**: Neobrutalism UI style (2.5dp black strokes, rounded corners, offset shadows), Catppuccin Mocha Dark and Light theme support, offline resiliency.

**Scale/Scope**: 1 screen (`CollectionDetailScreen`), 1 ViewModel (`CollectionDetailViewModel`), DAO/Repository queries for filtering bookmarks by `collectionId`.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Principle I: API-First & Cross-Platform Sync**: PASS. Single source of truth in local DB flow mapped to backend sync contract.
- **Principle II: Frictionless Capture**: PASS. Quick add button in collection header pre-selects current collection ID.
- **Principle III: Flexible Folder Organization**: PASS. View displays folder hierarchy and allows adding directly to open collection.
- **Principle IV: Dedicated Search & Instant Discovery**: PASS. Consistent search routes and clean navigation stack maintained.
- **Principle V: Cross-Platform UI Consistency & Offline Resiliency**: PASS. Neobrutalism visual language implemented; cached local Room Flow renders offline seamlessly.

All constitution check gates PASS with 0 violations.

## Project Structure

### Documentation (this feature)

```text
specs/003-collection-bookmarks-list/
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
│   └── collection_details_contract.json
└── tasks.md             # Phase 2 output (/speckit-tasks command)
```

### Source Code (repository root)

```text
android/app/src/main/java/com/madruga665/bookmarks/
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt          # Extended with collection filter queries
│   │   └── Entities.kt             # Bookmark & Collection entities
│   └── repository/
│       ├── BookmarkRepository.kt   # Extended to query by collection ID
│       └── CollectionRepository.kt # Extended to query collection by ID
└── ui/
    ├── components/
    │   └── NeobrutalistBookmarkCard.kt # Neobrutalist 2-column bookmark card component
    ├── collection/
    │   ├── CollectionDetailScreen.kt    # Main screen implementation
    │   ├── CollectionDetailUiState.kt  # UI State holder
    │   └── CollectionDetailViewModel.kt# ViewModel managing collection detail flow
    └── navigation/
        └── NavGraph.kt             # Replaces placeholder for FOLDER_DETAIL route
```

**Structure Decision**: Single Android application structure (`android/app/src/main/java/com/madruga665/bookmarks/`).

## Complexity Tracking

> **No Constitution violations. Complexity tracking table empty.**
