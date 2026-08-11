<!--
Sync Impact Report
- Version change: Initial template → 1.0.0
- Modified principles:
  - [PRINCIPLE_1_NAME] → I. API-First & Cross-Platform Sync
  - [PRINCIPLE_2_NAME] → II. Frictionless Capture & OS Share Target Integration
  - [PRINCIPLE_3_NAME] → III. Flexible Folder Organization via Share & App
  - [PRINCIPLE_4_NAME] → IV. Dedicated Search & Instant Discovery
  - [PRINCIPLE_5_NAME] → V. Cross-Platform UI Consistency & Offline Resiliency
- Added sections: Technical Stack & Architectural Constraints, Development & Testing Discipline
- Removed sections: None
- Templates requiring updates:
  - ✅ .specify/templates/plan-template.md (Constitution Check gates aligned with API-first & multi-platform sync)
  - ✅ .specify/templates/spec-template.md (Requirements & User Scenarios aligned)
  - ✅ .specify/templates/tasks-template.md (Task categories aligned with API contract and multi-platform sync)
- Follow-up TODOs: None
-->

# Bookmarks Sync Constitution

## Core Principles

### I. API-First & Cross-Platform Sync
The backend API MUST serve as the single source of truth for all bookmarks, folders, and metadata across mobile and web clients. All features MUST be designed API-first with standardized contracts to ensure seamless synchronization between platforms.

### II. Frictionless Capture & OS Share Target Integration
Saving a link MUST be instantaneous and require minimal user interaction. The application MUST support adding links via the home page quick-input control as well as native OS share targets (mobile share sheet and web share target API).

### III. Flexible Folder Organization via Share & App
Users MUST be able to organize bookmarks into folders either during the sharing workflow or at any time within the application. Initial bookmark creation MUST NOT be blocked by mandatory folder selection (sensible default folder or unorganized state MUST be used if omitted).

### IV. Dedicated Search & Instant Discovery
The main interface MUST provide a clear navigation trigger to a dedicated search page containing an input field. Search query responses MUST evaluate bookmark URLs, titles, folder names, and tags with minimal latency (<200ms target).

### V. Cross-Platform UI Consistency & Offline Resiliency
Web and mobile clients MUST present a consistent visual language, responsive interaction model, and clear synchronization status (offline, syncing, synced, error). Local storage or caching MUST allow offline access to existing bookmarks and queue new additions for sync when connectivity is restored.

## Technical Stack & Architectural Constraints

- **Backend API**: Standardized RESTful or GraphQL Web API responsible for data persistence, synchronization, authentication, and metadata extraction (fetching page titles/favicons).
- **Web Client**: Responsive web application supporting modern browsers, share target integration, and fast client-side navigation.
- **Mobile Client**: Native or cross-platform mobile application supporting OS Share Extensions / Intents and local persistence.
- **Data Model**: Bookmarks belong to Folders, support Tags, and maintain Sync Metadata (timestamps, sync tokens/versioning).

## Development & Testing Discipline

- **API Contracts**: OpenAPI or JSON Schema contracts MUST be defined, reviewed, and validated with contract tests before client features depend on them.
- **TDD / Automated Testing**: Core synchronization, link extraction logic, search query filters, and API endpoints MUST have automated unit and integration tests.
- **End-to-End User Verification**: Critical flows (capture link via home input, share link into folder, sync between clients, search links) MUST pass automated or manual verification before release.

## Governance

- This Constitution supersedes all informal architectural decisions. Amendments require updating this document, bumping the version according to Semantic Versioning (MAJOR for breaking principle redefinitions, MINOR for new principles/sections, PATCH for non-semantic updates), and documenting the rationale in the Sync Impact Report.
- All Pull Requests and feature implementations MUST satisfy the Constitution Check gates before merging.

**Version**: 1.0.0 | **Ratified**: 2026-08-11 | **Last Amended**: 2026-08-11
