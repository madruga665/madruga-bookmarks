# AGENTS.md - AI Agent Context & Guidelines

Welcome AI Agent! This document serves as the official operational guide and architectural reference for working within the **madruga665-bookmarks-app** repository.

---

## 1. Project Overview & Core Principles

**madruga665-bookmarks-app** is a cross-platform Bookmarks Synchronization system designed for instant link capture, folder organization, fast search discovery, and offline resiliency.

All development in this repository is governed by the project constitution in [`.specify/memory/constitution.md`](.specify/memory/constitution.md):

1. **API-First & Cross-Platform Sync**: Backend API is the single source of truth for bookmarks, folders, and metadata.
2. **Frictionless Capture**: Saving links must be instant via app quick-input or native OS Share Targets.
3. **Flexible Folder Organization**: Support bookmark categorization during share or post-capture without blocking initial saves.
4. **Dedicated Search & Instant Discovery**: Fast query responses (<200ms target) searching titles, URLs, folders, and tags.
5. **UI Consistency & Offline Resiliency**: Consistent Neobrutalism design system with offline local caching and sync status indicators.

---

## 2. Directory Structure

```text
madruga665-bookmarks-app/
├── .specify/                   # SpecKit framework configuration & project constitution
│   ├── memory/constitution.md  # Governance & core architectural principles
│   └── templates/              # Markdown templates (spec, plan, tasks, checklist)
├── .agents/skills/             # Workflow skills for SpecKit (specify, plan, tasks, implement)
├── specs/                      # Feature specifications & technical plans
│   ├── 001-android-home-neobrutalism/
│   ├── 002-save-bookmark-modal/
│   ├── 003-collection-bookmarks-list/
│   ├── 004-collection-actions-menu/
│   ├── 005-settings-screen/
│   ├── 006-bookmark-details/
│   └── 007-app-localization/
├── build.gradle.kts            # Root Gradle build script
├── settings.gradle.kts         # Gradle settings (rootProject.name = "madruga665-bookmarks-app")
├── gradlew / gradlew.bat       # Gradle wrapper executables
└── app/                        # Main Android application module (`com.madruga665.bookmarks`)
```

---

## 3. Technology Stack

### Android Client
- **Language**: Kotlin 2.2.10 (JVM Target 17)
- **UI Framework**: Jetpack Compose + Material 3 with Neobrutalism Design Tokens
- **Architecture**: Clean Architecture (UI / Domain-Repository / Data) + Unidirectional Data Flow (UDF)
- **Dependency Injection**: Hilt 2.60.1
- **Local Persistence**: Room Database 2.8.4 & DataStore Preferences 1.2.1
- **Metadata Extraction & Media**: JSoup 1.23.1, Coil Compose 2.7.0
- **Asynchronous Execution**: Kotlin Coroutines & Flow (`StateFlow`, `SharedFlow`)
- **Testing**: JUnit 4, MockK 1.14.11, kotlinx-coroutines-test 1.11.0

---

## 4. Development & Build Commands

All Gradle commands are executed directly from the project root (`madruga665-bookmarks-app/`):

```bash
# Run all unit tests
./gradlew test

# Build debug APK
./gradlew assembleDebug

# Run static checks & linting
./gradlew check
```

---

## 5. SpecKit Feature Development Workflow

When implementing or modifying features, follow the SpecKit specification process:

1. **Specify (`/speckit-specify`)**:
   - **Branch Creation**: Always creates and checks out a new Git branch named after the spec (e.g., `git checkout -b 008-feature-name`).
   - **Spec Definition**: Defines feature spec in `specs/XXX-feature-name/spec.md` and generates quality checklist.
   - **Alignment Interview**: **Always invokes `/grill-me`** immediately after `specify` to conduct an interactive interview with the user and resolve design/scope decisions before planning.
2. **Plan (`/speckit-plan`)**: Outline technical design and data model changes in `specs/XXX-feature-name/plan.md`.
3. **Tasks (`/speckit-tasks`)**: Break plan into actionable tasks in `specs/XXX-feature-name/tasks.md`.
4. **Implement (`/speckit-implement`)**: Execute tasks and verify code against tests and specs. **Always delegate implementation tasks to subagents (`invoke_subagent`)**, keeping the main agent as the orchestrator for tracking progress, managing dependencies, and validating overall project criteria.
5. **Code Review (`/code-reviewer`)**: **Always executed immediately after implementation**. The dedicated `code-reviewer` agent executes a mandatory dual-axis evaluation:
   - **Standards Axis**: Verifies Domain-Driven Design (DDD - rich models, aggregate boundaries, layer independence), SOLID principles (SRP, OCP, LSP, ISP, DIP), Android Clean Architecture, Jetpack Compose Neobrutalism tokens (`ui/theme/Color.kt`), immutable `UiState` flows, Fowler code smells baseline, and unit test coverage.
   - **Spec Axis**: Verifies full requirement coverage from `specs/XXX-feature-name/spec.md`, `plan.md`, and constitution compliance (`.specify/memory/constitution.md`), flagging any missing features or unrequested scope creep.

---

## 6. Coding & Architectural Standards

### Domain-Driven Design (DDD) & SOLID Principles
- **Domain Independence**: Pure Kotlin domain layer independent of Android SDK, UI, or persistence (Room/Retrofit) annotations.
- **Rich Domain Models & Value Objects**: Encapsulate domain logic and invariants inside entities and value objects (e.g. `BookmarkUrl`, `TagId`), avoiding anemic models.
- **SOLID Principles**: Adhere to Single Responsibility (focused classes), Open/Closed (extensible design), Liskov Substitution, Interface Segregation (lean contracts), and Dependency Inversion (high-level domain depends on abstractions).

### Clean Architecture & UI State
- **UI Layer**: Composables must consume immutable `UiState` exposed by `ViewModel` via `StateFlow`.
- **ViewModels**: Expose read-only `StateFlow<UiState>` using `stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ...)` to avoid memory leaks.
- **Repository Layer**: Abstract data source interactions (Room DAOs, Datastore, APIs) behind Repository interfaces (`BookmarkRepository`, `CollectionRepository`, `ThemeRepository`).

### Neobrutalism UI Guidelines
- Use high-contrast color palettes defined in `ui/theme/Color.kt`.
- Apply distinct black borders, crisp shadow offsets, bold typography, and flat surfaces with clean visual separation.

### Quality & Verification Discipline
- **Always verify changes**: Never declare a task complete without running `./gradlew test` to ensure zero regressions.
- **Error Handling**: Log and handle exceptions explicitly in repositories/ViewModels; expose user-friendly error states in `UiState`.
