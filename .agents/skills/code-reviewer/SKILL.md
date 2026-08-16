---
name: code-reviewer
description: Automated dual-axis code reviewer for madruga665-bookmarks-app. Evaluates code changes against repository standards (Clean Architecture, Jetpack Compose Neobrutalism, Fowler smells) and feature specifications (spec.md, plan.md, tasks.md, constitution.md). Always executed after speckit-implement.
---

# Code Reviewer Agent Skill (madruga665-bookmarks-app)

This skill performs a rigorous, two-axis code review on the changes between `HEAD` and the target baseline branch (usually `main` or merge-base). It is automatically invoked after `speckit-implement` to guarantee code quality, architectural consistency, and specification alignment before merging.

---

## 1. The Two-Axis Framework

The review is split into two independent axes that MUST NOT mask or dilute each other:

### Axis 1: Standards (Architecture, Quality, DDD, SOLID & Code Smells)
Evaluates whether the changes respect the repository's documented coding conventions, architectural paradigms, and baseline quality heuristics:
1. **Domain-Driven Design (DDD)**:
   - **Rich Domain Models**: Business rules and invariants are encapsulated within domain entities and value objects (avoiding anemic domain models).
   - **Layer Independence**: Domain layer is pure Kotlin, completely agnostic of UI frameworks, Android SDK, Room DB annotations, or network libraries.
   - **Value Objects vs. Entities**: Distinguish identity-based Entities from immutable Value Objects (e.g., `BookmarkUrl`, `Tag`, `CollectionId`).
   - **Aggregates & Repositories**: Clear aggregate roots protecting consistency boundaries; domain repositories define contracts fulfilled by data layer.
   - **Ubiquitous Language**: Consistent naming matching domain terms across code, specs, and database models.
2. **SOLID Principles**:
   - **Single Responsibility (SRP)**: Each class (ViewModel, UseCase, Repository, Composable) has one well-defined responsibility and reason to change.
   - **Open/Closed (OCP)**: Components and strategies (e.g., metadata parsers, sync handlers) are extensible without modifying existing core logic.
   - **Liskov Substitution (LSP)**: Implementations cleanly adhere to domain/repository contracts without altering expected behavioral invariants.
   - **Interface Segregation (ISP)**: Interfaces are fine-grained and focused; clients are not forced to depend on methods they do not use.
   - **Dependency Inversion (DIP)**: High-level policy (UseCases/ViewModels/Domain) depends upon abstractions (interfaces), never concrete infrastructure implementations (Room/Retrofit/DataStore).
3. **Android Clean Architecture & UDF**:
   - UI Composables consume immutable `UiState` from `ViewModel` via `StateFlow`.
   - ViewModels use `stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ...)`.
   - Data access and network operations are abstracted behind repository interfaces (`BookmarkRepository`, `CollectionRepository`, `ThemeRepository`).
   - Dependency injection configured correctly using Hilt (`@HiltViewModel`, `@Inject`, `@Module`, `@InstallIn`).
4. **Neobrutalism UI Guidelines**:
   - Use tokens from `ui/theme/Color.kt`.
   - Distinct black borders (`BorderStroke(2.dp, Color.Black)` or design tokens), crisp shadow offsets, bold typography, flat surfaces.
   - Fluid responsiveness and proper dark/light theme handling.
5. **Android & Kotlin Idioms**:
   - Kotlin 2.2.10 idioms, safe nullability, sealed interfaces/classes for UI state & events.
   - Coroutine scope safety, appropriate Dispatchers (`Dispatchers.IO` for disk/network, `Dispatchers.Default` for heavy computation).
   - Room DAOs with suspend functions / Flow, proper SQLite index annotations.
6. **Testing Standards**:
   - Unit tests covering ViewModels, UseCases, Repositories using JUnit 4, MockK, and `kotlinx-coroutines-test`.
   - All tests pass (`./gradlew test`).
7. **Fowler Code Smells Baseline**:
   - **Mysterious Name**: Names that fail to reveal purpose.
   - **Duplicated Code**: Identical or nearly identical logic shapes in multiple places.
   - **Feature Envy**: Methods accessing external object data more than their own.
   - **Data Clumps**: Fields/parameters frequently passed together without a domain wrapper.
   - **Primitive Obsession**: Raw types used instead of domain value classes.
   - **Repeated Switches**: Redundant `when`/`if` cascades that should be polymorphic or mapped.
   - **Shotgun Surgery**: Single logical change scattered across too many unrelated files.
   - **Divergent Change**: A single file modified for multiple unrelated concerns.
   - **Speculative Generality**: Over-engineered hooks or abstractions not required by the spec.
   - **Message Chains**: Excessive chaining (`a.b().c().d()`).
   - **Middle Man**: Classes that merely delegate without adding value.
   - **Refused Bequest**: Classes overriding or ignoring inherited contracts awkwardly.

---

### Axis 2: Spec (Functional & Contractual Alignment)
Evaluates whether the changes faithfully implement the specification without omissions or scope creep:
1. **Spec Alignment (`specs/XXX-feature-name/spec.md`)**:
   - All user scenarios and functional requirements are fully implemented.
   - Missing or partial requirements are explicitly identified.
2. **Plan & Task Alignment (`plan.md` & `tasks.md`)**:
   - All tasks in `tasks.md` are accounted for and marked `[X]`.
   - File structure matches proposed changes in `plan.md`.
3. **Constitution Compliance (`.specify/memory/constitution.md`)**:
   - Principle I: API-First & Cross-Platform Sync readiness.
   - Principle II: Frictionless Capture & OS Share Target integration.
   - Principle III: Flexible Folder Organization without blocking capture.
   - Principle IV: Dedicated Search & Instant Discovery (<200ms target).
   - Principle V: UI Consistency & Offline Resiliency (Room caching, sync states).
4. **Scope Creep & Extraneous Changes**:
   - Identifies code, configurations, or dependencies added that were not requested in the spec or plan.

---

## 2. Review Procedure

### Step 1: Establish Base & Context
1. Identify the active feature directory under `specs/XXX-.../` (or from branch name / recent commits).
2. Determine the fixed baseline reference (default: `main` or merge-base `git merge-base main HEAD`).
3. Run `rtk git diff main...HEAD` and `rtk git log main..HEAD --oneline` to inspect the full changeset.

### Step 2: Parallel Evaluation
Evaluate both axes thoroughly:
- Run the **Standards** analysis against codebase rules (`AGENTS.md`, `.specify/memory/constitution.md`, and Fowler baseline).
- Run the **Spec** analysis against `specs/XXX-feature-name/spec.md`, `plan.md`, and `tasks.md`.

### Step 3: Run Automated Verification
Execute project verification to guarantee build and test integrity:
```bash
./gradlew test
```

### Step 4: Generate Report
Output the structured review report using the exact template below.

---

## 3. Review Report Format

```markdown
# 🔍 Code Review Report

**Feature**: [Feature Name / Spec ID]
**Baseline**: `main...HEAD` ([N] commits, [M] files changed)
**Test Suite**: ✅ PASS ([X] tests) / ❌ FAIL

---

## 📐 Axis 1: Standards & Architecture

### 🟢 Conformance & Strengths
- [List positive architectural alignments, Compose Neobrutalism compliance, Clean Architecture patterns]

### ⚠️ Violations & Smells
| File / Location | Type | Category | Severity | Description & Suggested Fix |
|-----------------|------|----------|----------|-----------------------------|
| `path/to/File.kt:L42` | Documented Standard / Code Smell | Clean Architecture / Fowler Smell | Hard / Judgement | Clear actionable fix |

---

## 📋 Axis 2: Specification & Requirements

### 🟢 Requirements Satisfied
- [List verified requirements from spec.md with references]

### 🔴 Discrepancies & Scope Issues
- **Missing / Incomplete**: [Any requirement from spec.md not found in diff]
- **Scope Creep**: [Any changes in diff not requested by spec.md / plan.md]
- **Behavior Mismatch**: [Any implementation differing from spec intent]

---

## 🎯 Final Verdict & Action Items

- **Standards Axis**: [PASS / ACTION REQUIRED]
- **Spec Axis**: [PASS / ACTION REQUIRED]
- **Overall**: [APPROVED / REVISE]

### Required Next Steps (if any):
1. [Action item 1]
2. [Action item 2]
```
