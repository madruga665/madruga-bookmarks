# Implementation Plan: Touch-Anchored Arc Actions Menu

**Branch**: `013-arc-action-menu` | **Date**: 2026-08-22 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/013-arc-action-menu/spec.md`

## Summary

Refactor the Collection and Bookmark actions menu to behave as a true dynamic Satellite / Arc Menu anchored directly to the user's touch coordinate. Satellite action buttons radiate outward along an adaptive radial arc calculated based on screen boundary proximity. The menu supports continuous drag-to-select with haptic feedback as well as discrete tap-to-select, while preserving the Neobrutalist design tokens across all views.

## Technical Context

**Language/Version**: Kotlin 2.2.10 (JVM Target 17)
**Primary Dependencies**: Jetpack Compose (Material 3, Foundation, Animation), Hilt 2.60.1
**Storage**: Room Database 2.8.4 & DataStore Preferences 1.2.1
**Testing**: JUnit 4, MockK 1.14.11, kotlinx-coroutines-test 1.11.0
**Target Platform**: Android (API 26+)
**Project Type**: Android Native Mobile Application
**Performance Goals**: 60+ fps animations, <50ms spring blossom response, zero UI thread blocking
**Constraints**: Pure Jetpack Compose implementation, offline resilience, Neobrutalism design system tokens
**Scale/Scope**: Reusable across `HomeScreen`, `CollectionDetailScreen`, and `SearchScreen`

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **Principle I (API-First & Cross-Platform Sync)**: Contextual actions trigger existing repository/API synchronization operations.
- [x] **Principle II (Frictionless Capture)**: Quick-actions support instantaneous bookmark opening, pinning, and sharing.
- [x] **Principle III (Flexible Folder Organization)**: Collection editing and deletion operate seamlessly through the new arc menu.
- [x] **Principle IV (Dedicated Search & Discovery)**: Search screen bookmark results fully support the new arc menu.
- [x] **Principle V (UI Consistency & Offline Resiliency)**: Consistent Neobrutalism tokens (bold borders, drop shadows, spring animations) and full offline operation.

## Project Structure

### Documentation (this feature)

```text
specs/013-arc-action-menu/
├── spec.md              # Feature specification
├── plan.md              # Implementation plan
├── research.md          # Technical decisions & mathematical model
├── data-model.md        # Entities, geometric models & state integration
├── quickstart.md        # Validation & verification guide
├── contracts/           # UI contracts
│   └── arc-menu-contract.md
└── tasks.md             # Actionable task breakdown (Phase 2)
```

### Source Code

```text
app/src/main/java/com/madruga665/bookmarks/
├── ui/
│   ├── components/
│   │   ├── NeobrutalistArcActionsMenu.kt   # Reusable generic Satellite/Arc Menu composable
│   │   ├── ArcGeometryCalculator.kt        # Pure Kotlin geometry & hit-testing engine
│   │   ├── ArcActionItem.kt                # Data models for arc menu items
│   │   ├── CollectionActionsOverlay.kt     # Refactored Collection overlay using Arc Menu
│   │   ├── BookmarkActionsOverlay.kt       # Refactored Bookmark overlay using Arc Menu
│   │   ├── NeobrutalistFolderCard.kt       # Streamlined folder card (removed legacy inline buttons)
│   │   └── NeobrutalistBookmarkCard.kt     # Streamlined bookmark card (removed legacy inline buttons)
│   ├── home/
│   │   └── HomeScreen.kt                   # Integration with Collection arc actions
│   ├── collection/
│   │   └── CollectionDetailScreen.kt       # Integration with Bookmark arc actions
│   └── search/
│       └── SearchScreen.kt                 # Integration with Search bookmark arc actions
app/src/test/java/com/madruga665/bookmarks/
└── ui/
    └── components/
        └── ArcGeometryCalculatorTest.kt    # Unit tests for arc trigonometry & boundary checks
```

## Phase 0: Outline & Research

Completed. Key findings consolidated in [research.md](./research.md):
- Boundary-aware adaptive sector calculation algorithm for screen edges and corners.
- Mathematical positioning formulas for $N$ satellite items.
- Dual-mode gesture handling (fluid drag-release and discrete tap).
- Clean architectural separation of card content from overlay menu layer.

## Phase 1: Design & Contracts

Completed. Artifacts generated:
- [data-model.md](./data-model.md): Data structures (`ArcActionItem`, `ArcItemPosition`, `ArcGeometryConfig`) and overlay state flow.
- [contracts/arc-menu-contract.md](./contracts/arc-menu-contract.md): Composable API contract for `NeobrutalistArcActionsMenu`.
- [quickstart.md](./quickstart.md): Step-by-step test execution and manual verification guide.
