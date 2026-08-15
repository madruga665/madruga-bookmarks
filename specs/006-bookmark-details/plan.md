# Implementation Plan: Bookmark Details View

**Branch**: `006-bookmark-details` | **Date**: 2026-08-15 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/006-bookmark-details/spec.md`

## Summary

Implement the dedicated full-screen Bookmark Details view (`BookmarkDetailScreen`) that displays complete link information, actions, and metadata when a bookmark is clicked in a collection or search view. The feature includes:
1. Top bar with platform/collection badge and 4 Neobrutalist action buttons (Refresh metadata, Share, Move collection, and Delete).
2. Hero thumbnail preview with an overlay Pin/Unpin button (pinned items are prominently grouped in a "PINNED" section on the collection screen).
3. Inline title editing with "Salvar" / "Cancelar" controls.
4. Expandable description preview ("Show more" / "Show less").
5. High-contrast yellow Neobrutalist URL card launching the external browser.
6. Interactive TAGS management (add via compact dialog, remove via 'X' chip button).
7. Personal NOTES editing with "Salvar" / "Cancelar" controls.
8. Collection move via bottom sheet and deletion via Neobrutalist confirmation dialog.

## Technical Context

**Language/Version**: Kotlin 2.2.10, JVM Target 17

**Primary Dependencies**: Android Jetpack Compose, Material 3, Compose Navigation, Hilt, Room Database, Coil Compose, JSoup, Kotlin Coroutines & Flow

**Storage**: Local Room SQLite Database (`AppDatabase`, `BookmarkDao`, `CollectionDao`) with schema migration to version 4 for `description`, `notes`, `tags`, and `updated_at`.

**Testing**: JUnit4, MockK, Kotlinx Coroutines Test, Compose UI Test

**Target Platform**: Android (API 26+)

**Project Type**: Mobile Application (Android)

**Performance Goals**:
- Navigate to `BookmarkDetailScreen` in <150ms.
- Render details and hero preview in <200ms from local DB cache.
- Launch browser via URL card in <100ms.

**Constraints**: Neobrutalism UI style (2.5dp black borders, 4dp shadow offsets, bold typography), offline resiliency (local edits persist immediately with `sync_status = "PENDING_SYNC"`).

**Scale/Scope**: 1 new screen (`BookmarkDetailScreen`), 1 ViewModel (`BookmarkDetailViewModel`), UI state holder (`BookmarkDetailUiState`), Room schema updates, `LinkMetadataExtractor` updates, `CollectionDetailScreen` pinned section layout update, and `NavGraph` route integration.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Principle I: API-First & Cross-Platform Sync**: PASS. Bookmark schema changes (notes, tags, description, updated_at) follow unified sync model with `PENDING_SYNC` status.
- **Principle II: Frictionless Capture**: PASS. Unblocks quick-saved links by enriching them post-capture with notes, tags, and custom title.
- **Principle III: Flexible Folder Organization**: PASS. Supports moving bookmarks across collections at any time via the top bar move action.
- **Principle IV: Dedicated Search & Instant Discovery**: PASS. Enables clicking search results to inspect details and tags.
- **Principle V: Cross-Platform UI Consistency & Offline Resiliency**: PASS. Consistent Neobrutalism design tokens; all changes persist immediately to local Room database.

All constitution check gates PASS with 0 violations.

## Project Structure

### Documentation (this feature)

```text
specs/006-bookmark-details/
├── plan.md              # This file (/speckit-plan output)
├── research.md          # Technical research & decisions
├── data-model.md        # Entities, schema, and state transitions
├── quickstart.md        # Verification and manual test guide
├── contracts/           # UI state & navigation contract
│   └── bookmark_details_contract.json
└── checklists/
    └── requirements.md  # Spec quality checklist
```

### Source Code (repository root)

```text
android/app/src/main/java/com/madruga665/bookmarks/
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt               # Updated schema version 4 & BookmarkDao queries
│   │   └── Entities.kt                  # Extended BookmarkEntity with description, notes, tags, updated_at
│   ├── remote/
│   │   └── LinkMetadataExtractor.kt     # Extended with description extraction
│   └── repository/
│       └── BookmarkRepository.kt        # Extended with update, pin, tags, notes, and move methods
└── ui/
    ├── bookmark/
    │   ├── BookmarkDetailScreen.kt      # Main full-screen Composable with all sections
    │   ├── BookmarkDetailUiState.kt     # Immutable UI State holder
    │   ├── BookmarkDetailViewModel.kt   # ViewModel managing bookmark detail state & actions
    │   └── components/
    │       ├── BookmarkDetailTopBar.kt  # Badge + 4 Neobrutalist action buttons
    │       ├── BookmarkHeroSection.kt   # Preview image with Pin/Unpin overlay button
    │       ├── BookmarkTitleSection.kt  # Display / Inline editable title with Save/Cancel
    │       ├── BookmarkDescriptionSection.kt # Collapsible description
    │       ├── BookmarkUrlCard.kt       # Yellow Neobrutalist external link button
    │       ├── BookmarkTagsSection.kt   # Tag chips with remove + Add dialog
    │       └── BookmarkNotesSection.kt  # Multi-line notes with Save/Cancel
    ├── collection/
    │   ├── CollectionDetailScreen.kt    # Updated to display "PINNED" section above "ALL LINKS"
    │   └── CollectionDetailViewModel.kt # Updated bookmark click navigation
    └── navigation/
        ├── NavGraph.kt                  # Registered BOOKMARK_DETAIL route
        └── NavRoutes.kt                 # Added BOOKMARK_DETAIL route constants & helpers
```

**Structure Decision**: Standard Android Clean Architecture / MVVM structure matching existing app modules.

## Complexity Tracking

> **No Constitution violations. Complexity tracking table empty.**
