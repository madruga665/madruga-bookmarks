# Implementation Plan: Full App Localization & Internationalization (i18n)

**Branch**: `007-app-localization` | **Date**: 2026-08-15 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/007-app-localization/spec.md`

## Summary

Implement comprehensive localization and internationalization (i18n) across all screens and components in the app. All user-facing strings are extracted to `res/values/strings.xml` (English default) and `res/values-pt-rBR/strings.xml` (Brazilian Portuguese). Dynamic locale switching in Settings immediately updates the running app's locale via reactive Compose `CompositionLocalProvider` and `AppCompatDelegate.setApplicationLocales` without requiring an app restart.

## Technical Context

**Language/Version**: Kotlin 2.2.10, JVM Target 17

**Primary Dependencies**: Android Jetpack Compose, Material 3, AndroidX Core / AppCompat, DataStore Preferences, Hilt

**Storage**: DataStore Preferences (`app_language` key)

**Testing**: JUnit4, MockK, Kotlinx Coroutines Test, Compose UI Test

**Target Platform**: Android (API 26+)

**Project Type**: Mobile Application (Android)

**Performance Goals**:
- Dynamic locale switch applies across all active screens in <100ms.
- 0 hardcoded strings in Composable UI components.

**Constraints**: Neobrutalism typography and tokens preserved across both languages; clean layout support for variable text lengths.

**Scale/Scope**: Resource files (`values/strings.xml`, `values-pt-rBR/strings.xml`), root composition wrapper in `MainActivity.kt`, and string resource replacements across all 7 UI feature packages (`home/`, `collection/`, `bookmark/`, `savemodal/`, `settings/`, `components/`).

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Principle I: API-First & Cross-Platform Sync**: PASS. Language preference saved in client preferences; ready for profile sync.
- **Principle II: Frictionless Capture**: PASS. Share and quick-add modals fully localized for instant comprehension.
- **Principle III: Flexible Folder Organization**: PASS. All folder and collection actions properly translated.
- **Principle IV: Dedicated Search & Instant Discovery**: PASS. Search placeholders and hints localized.
- **Principle V: Cross-Platform UI Consistency & Offline Resiliency**: PASS. Consistent Neobrutalism styling; local strings available offline.

All constitution check gates PASS with 0 violations.

## Project Structure

### Documentation (this feature)

```text
specs/007-app-localization/
├── plan.md              # This file (/speckit-plan output)
├── research.md          # Technical research & decisions
├── data-model.md        # Keys and supported languages
├── quickstart.md        # Verification and manual test guide
├── contracts/
│   └── localization_contract.json
└── checklists/
    └── requirements.md  # Spec quality checklist
```

### Source Code (repository root)

```text
android/app/src/main/
├── res/
│   ├── values/
│   │   └── strings.xml                  # English default string resources
│   └── values-pt-rBR/
│       └── strings.xml                  # Brazilian Portuguese string resources
└── java/com/madruga665/bookmarks/
    ├── MainActivity.kt                  # Root dynamic locale wrapper
    ├── data/repository/
    │   └── SettingsRepository.kt        # AppLanguage enum & DataStore integration
    └── ui/
        ├── home/                        # Home UI strings -> stringResource(R.string.*)
        ├── collection/                  # Collection list, header, and actions strings -> stringResource
        ├── bookmark/                    # Bookmark detail, notes, tags, delete dialog strings -> stringResource
        ├── savemodal/                   # Save modal strings -> stringResource
        └── settings/                    # Settings UI and dialogs -> stringResource
```

**Structure Decision**: Standard Android string resource files + Jetpack Compose stringResource() integration.

## Complexity Tracking

> **No Constitution violations. Complexity tracking table empty.**
