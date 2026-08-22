# Implementation Plan: Subtle Neobrutalist Grid Background Pattern

**Branch**: `012-grid-background` | **Date**: 2026-08-22 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/012-grid-background/spec.md`

## Summary

Implement a subtle, elegant Neobrutalist grid pattern (`Modifier.neobrutalistGridBackground`) applied across all primary application screen backgrounds (`HomeScreen`, `SearchScreen`, `CollectionDetailScreen`, `BookmarkDetailScreen`, `SettingsScreen`). The grid uses 24dp x 24dp square cells with a 1dp stroke width, rendered via GPU-accelerated `DrawScope.drawBehind` on fixed root containers. It dynamically adapts to theme changes with a discreet light-gray tone (`#E2E2E2`) in Light Mode and a low-glare dark-slate tone (`#28283D`) in Catppuccin Mocha Dark Mode.

## Technical Context

**Language/Version**: Kotlin 2.2.10 / Android SDK 35 (JVM Target 17)  
**Primary Dependencies**: Jetpack Compose (Material3, Foundation, UI), Hilt, Coroutines  
**Storage**: N/A (UI Theme and Canvas Rendering)  
**Testing**: JUnit 4, Compose UI Test, Robolectric / Unit Tests  
**Target Platform**: Android 8.0+ (API 26+)  
**Project Type**: Mobile App (Android)  
**Performance Goals**: 60/120 FPS fluid rendering during scrolling; 0ms allocation overhead during draw pass  
**Constraints**: Neobrutalism Design System, WCAG contrast compliance for foreground text/cards, Constitution Principles I, II, III, IV, V  
**Scale/Scope**: 1 theme color token update (`Color.kt`), 1 modifier extension (`Modifiers.kt`), 5 screen composables (`HomeScreen`, `SearchScreen`, `CollectionDetailScreen`, `BookmarkDetailScreen`, `SettingsScreen`), unit/theme tests (`ThemeTest.kt`)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. API-First & Cross-Platform Sync**: Pure visual UI layer styling; does not affect data sync or API contracts.
- [x] **II. Frictionless Capture & OS Share Target Integration**: Background styling does not block or introduce latency to quick save or share operations.
- [x] **III. Flexible Folder Organization**: Grid background renders seamlessly behind folder lists and management overlays.
- [x] **IV. Dedicated Search & Instant Discovery**: Search screen discovery cards and live results list render with solid surfaces over the fixed grid pattern.
- [x] **V. Cross-Platform UI Consistency & Offline Resiliency**: Reinforces the Neobrutalist visual identity across both Light and Dark (Catppuccin Mocha) themes, functioning 100% offline.

## Project Structure

### Documentation (this feature)

```text
specs/012-grid-background/
├── spec.md              # Feature specification
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
│   └── ui-grid-background-contract.md
└── checklists/
    └── requirements.md
```

### Source Code (repository root)

```text
app/src/main/java/com/madruga665/bookmarks/
├── ui/
│   ├── theme/
│   │   ├── Color.kt                          # Added gridLine token to NeobrutalismColors (Light and Dark)
│   │   └── Modifiers.kt                      # Added Modifier.neobrutalistGridBackground(...)
│   ├── home/
│   │   └── HomeScreen.kt                     # Integrated fixed grid background on root Box
│   ├── search/
│   │   └── SearchScreen.kt                   # Integrated fixed grid background on root Box
│   ├── collection/
│   │   └── CollectionDetailScreen.kt         # Integrated fixed grid background on root Box
│   ├── bookmark/
│   │   └── BookmarkDetailScreen.kt           # Integrated fixed grid background on root Box
│   └── settings/
│       └── SettingsScreen.kt                 # Integrated fixed grid background on root Box
app/src/test/java/com/madruga665/bookmarks/
└── ui/
    └── theme/
        └── GridBackgroundTest.kt             # Unit test for theme tokens and modifier properties
```

**Structure Decision**: Android Jetpack Compose Clean Architecture UI Layer in `app/src/main/java/com/madruga665/bookmarks/ui/`.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|---|---|---|
| None | N/A | Fully compliant with Constitution and architectural constraints. |
