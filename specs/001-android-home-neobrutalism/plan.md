# Implementation Plan: Native Android Neobrutalism Home Screen

**Branch**: `001-android-home-neobrutalism` | **Date**: 2026-08-11 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/001-android-home-neobrutalism/spec.md`

## Summary

Implement the Native Android Home Screen using Kotlin and Jetpack Compose featuring a Neobrutalism aesthetic (crisp solid outlines, zero-blur hard offset drop shadows, bold typography, tabbed folder cards). The feature supports a Light Theme (replicating the design reference print) and a Dark Theme (mapped to Catppuccin Mocha colors), quick bookmark saving with clipboard auto-paste, and navigation handlers for Search, Settings, and Collections.

## Technical Context

**Language/Version**: Kotlin 1.9+ / Java 17 (Android SDK 26+, Target SDK 34/35)

**Primary Dependencies**: Jetpack Compose (Material3, Compose UI, Foundation), Navigation Compose, Lifecycle ViewModel, Kotlin Coroutines, StateFlow, Hilt DI

**Storage**: Room Database (local offline cache for Bookmarks and Collections), DataStore (Theme Preference)

**Testing**: JUnit 5 / JUnit 4, Compose UI Test Framework (`androidx.compose.ui.test`), MockK

**Target Platform**: Native Android Mobile (API Level 26+)

**Project Type**: Mobile App (Android Native)

**Performance Goals**: 60/120 fps Compose rendering, immediate tactile button touch feedback (<50ms), instant theme switching (<50ms)

**Constraints**: Strict Neobrutalism styling (solid 2-3dp borders, zero-blur hard offset shadows), Light Theme matching reference screenshot, Dark Theme matching Catppuccin Mocha palette

**Scale/Scope**: Home screen UI, Quick save bar + clipboard paste, My Collections grid with tabbed folder cards, Search & Settings navigation triggers

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Principle I: API-First & Cross-Platform Sync**: PASSED. Backend API contracts defined in `contracts/bookmark-api-contract.json` ensure single source of truth alignment.
- **Principle II: Frictionless Capture & OS Share Target**: PASSED. Home quick-save bar supports one-tap paste and instant saving.
- **Principle III: Flexible Folder Organization**: PASSED. Folder cards displayed on home screen; link saving defaults to unorganized state if no folder picked.
- **Principle IV: Dedicated Search & Instant Discovery**: PASSED. Top bar search button triggers navigation to dedicated search view.
- **Principle V: Cross-Platform UI Consistency & Offline Resiliency**: PASSED. Dual theme system (Light Reference & Catppuccin Mocha Dark) maintains visual quality; local Room cache supports offline viewing.

## Project Structure

### Documentation (this feature)

```text
specs/001-android-home-neobrutalism/
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output (/speckit-plan command)
├── data-model.md        # Phase 1 output (/speckit-plan command)
├── quickstart.md        # Phase 1 output (/speckit-plan command)
├── contracts/           # Phase 1 output (/speckit-plan command)
│   ├── home-screen-ui-contract.md
│   └── bookmark-api-contract.json
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
    │   │   │   │   ├── theme/
    │   │   │   │   │   ├── Color.kt             # Design Print Light & Catppuccin Mocha Dark palettes
    │   │   │   │   │   ├── Theme.kt             # NeobrutalismTheme provider
    │   │   │   │   │   ├── Type.kt              # Neobrutalism typography
    │   │   │   │   │   └── Modifiers.kt         # Custom neobrutalistShadow & border modifier
    │   │   │   │   ├── components/
    │   │   │   │   │   ├── NeobrutalistButton.kt
    │   │   │   │   │   ├── NeobrutalistTextField.kt
    │   │   │   │   │   └── NeobrutalistFolderCard.kt # Tabbed folder card composable
    │   │   │   │   └── home/
    │   │   │   │       ├── HomeScreen.kt        # Home Screen composable UI
    │   │   │   │       ├── HomeViewModel.kt     # ViewModel + StateFlow
    │   │   │   │       └── HomeScreenUiState.kt # UI state model
    │   │   │   ├── data/
    │   │   │   │   ├── local/                   # Room DB, DAOs, Entities
    │   │   │   │   └── repository/              # Bookmark & Collection Repository
    │   │   │   └── di/                          # Hilt Dependency Injection modules
    │   └── test/                                # Local Unit Tests (ViewModel, Repositories)
    └── androidTest/                             # Compose UI & Instrumentation Tests
```

**Structure Decision**: Mobile App structure using standard Android Jetpack architecture (UI composables, Theme design system, ViewModel, Data repositories, Room local cache).

## Complexity Tracking

*No violations detected. All design choices comply with project Constitution.*
