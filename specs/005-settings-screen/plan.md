# Implementation Plan: Settings Screen, Theme & Language Preferences, and Usage Overview

**Branch**: `005-settings-screen` | **Date**: 2026-08-14 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/005-settings-screen/spec.md` and UI mockup `Screenshot_20260814_184938_Tuckii.jpg`

## Summary

Implement a full-featured Neobrutalist Settings screen in Kotlin and Jetpack Compose. The screen features a yellow Hero summary card showing usage stats (links today, total links, total collections), user preferences (Theme mode selection: Light, Dark, System; Language selection: English, Português; Haptic feedback toggle), data management action items (Backup/Restore/Import), and seamless navigation from the Home screen.

## Technical Context

**Language/Version**: Kotlin 2.2.10 (JVM Target 17)

**Primary Dependencies**: Jetpack Compose, Material 3, Navigation Compose, Hilt 2.60.1, Jetpack DataStore Preferences 1.2.1, Room 2.8.4, Coroutines & Flow

**Storage**: Jetpack DataStore Preferences (`"settings_prefs"` / `"theme_prefs"`), Room Database (`bookmarks_table`, `collections_table`)

**Testing**: JUnit 4, MockK 1.14.11, kotlinx-coroutines-test 1.11.0, Compose UI Testing

**Target Platform**: Android Native (API Level 26+)

**Project Type**: Android Mobile App (Clean Architecture + UDF)

**Performance Goals**: Settings screen loads usage statistics in <100ms; theme and language changes apply dynamically in <50ms without flickering.

**Constraints**: Neobrutalism UI style (2.5dp black borders, sharp offset drop-shadows, yellow `#FFE600` hero card, distinct card surfaces), Light & Catppuccin Mocha Dark themes, full localization for English and Brazilian Portuguese.

**Scale/Scope**: `SettingsScreen` composable, `SettingsViewModel`, `SettingsUiState`, `SettingsRepository` (or expanded `ThemeRepository` / `UserPreferencesRepository`), `UsageHeroCard`, `PreferencesSection`, `ActionItemCard`, and `NavGraph` integration.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Principle I: API-First & Cross-Platform Sync**: PASSED. Schema contract defined in `contracts/settings-preferences-contract.json` standardizes preferences across clients.
- **Principle II: Frictionless Capture & OS Share Target**: PASSED. Independent settings screen does not add friction to link capture.
- **Principle III: Flexible Folder Organization**: PASSED. Reflects collection counts accurately.
- **Principle IV: Dedicated Search & Instant Discovery**: PASSED. Settings navigation integrates smoothly with main bar.
- **Principle V: Cross-Platform UI Consistency & Offline Resiliency**: PASSED. Full local DataStore & Room persistence ensures 100% offline functionality. Consistent Neobrutalism design tokens across light and dark modes.

## Project Structure

### Documentation (this feature)

```text
specs/005-settings-screen/
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output (/speckit-plan command)
├── data-model.md        # Phase 1 output (/speckit-plan command)
├── quickstart.md        # Phase 1 output (/speckit-plan command)
├── contracts/           # Phase 1 output (/speckit-plan command)
│   ├── settings-ui-contract.md
│   └── settings-preferences-contract.json
└── tasks.md             # Phase 2 output (/speckit-tasks command - NOT created by /speckit-plan)
```

### Source Code Layout (Android Native)

```text
android/app/src/main/
├── java/com/madruga665/bookmarks/
│   ├── data/
│   │   ├── repository/
│   │   │   ├── SettingsRepository.kt            # DataStore repository for theme, language & haptics
│   │   │   ├── ThemeRepository.kt               # Theme mode repository (or combined with Settings)
│   │   │   ├── BookmarkRepository.kt            # Query for total bookmarks & links added today
│   │   │   └── CollectionRepository.kt          # Query for total collections
│   ├── ui/
│   │   ├── navigation/
│   │   │   └── NavGraph.kt                      # Route to SettingsScreen
│   │   ├── settings/
│   │   │   ├── SettingsScreen.kt                # Main Settings composable
│   │   │   ├── SettingsViewModel.kt             # Settings ViewModel (combines flows to UiState)
│   │   │   ├── SettingsUiState.kt               # UI state & events
│   │   │   ├── components/
│   │   │   │   ├── UsageHeroCard.kt             # Top yellow card with usage metrics
│   │   │   │   ├── PreferenceItemCard.kt        # Option / Switch preference row cards
│   │   │   │   ├── ThemeSelectionDialog.kt      # Dialog/chip selector for Light/Dark/System
│   │   │   │   └── LanguageSelectionDialog.kt   # Dialog/chip selector for EN/PT-BR
│   ├── di/
│   │   └── AppModule.kt                         # Provide SettingsRepository / ViewModel bindings
└── res/
    ├── values/
    │   └── strings.xml                          # English localization strings
    └── values-pt-rBR/
        └── strings.xml                          # Brazilian Portuguese strings
```

**Structure Decision**: Clean Architecture with MVI/UDF state in Jetpack Compose, modular components in `ui/settings/`, and reactive DataStore + Room repositories.
