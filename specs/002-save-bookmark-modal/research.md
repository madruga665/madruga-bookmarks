# Research & Technical Decisions: Add Bookmark Bottom Sheet & Collection Selector Modal

## Overview

This document captures technical decisions and best practices for implementing the Neobrutalist Add Bookmark Bottom Sheet Modal in native Android using Kotlin and Jetpack Compose.

## Technical Decisions

### Decision 1: Compose ModalBottomSheet with Custom Neobrutalism Styling

- **Decision**: Wrap Material3 `ModalBottomSheet` inside a custom Neobrutalist container (`Modifier.neobrutalistShadow()` with thick 2.5dp black borders and zero-blur offset shadow).
- **Rationale**: Standard Material3 bottom sheets apply rounded top corners with gaussian elevation shadows. Applying custom Neobrutalism modifiers guarantees visual alignment with the design print (thick black outline, hard offset shadow, top drag handle) while maintaining native Android drag-to-dismiss behavior.
- **Alternatives Considered**:
  - *Full Custom Animated Box Overlay*: Rejected because `ModalBottomSheet` handles window insets, scrim tap-to-dismiss, and accessibility out of the box.

---

### Decision 2: State Flow & Dynamic Save Button Pattern

- **Decision**: The modal state (`SaveBookmarkModalUiState`) tracks `targetUrl`, `selectedCollectionId`, `isPinned`, `availableCollections`, and `isCreatingFolder`.
- **Rationale**:
  - Default `selectedCollectionId` defaults to `"col_unsorted"` (Unsorted) upon opening.
  - Tapping a collection card updates `selectedCollectionId`.
  - The primary action button dynamically formats text: `Save to "${selectedCollection.name}"` in a yellow accent container (`#FFD600` / `#F9E2AF`).
- **Alternatives Considered**:
  - *Multi-Screen Wizard*: Rejected because single-view bottom sheet matches the reference screenshot and minimizes user taps.

---

### Decision 3: Inline Folder Creation Strategy

- **Decision**: Tapping the top-right "New Folder" icon button reveals an inline creation card inside the modal.
- **Rationale**: Users can type a folder name (e.g. "Design") and choose a color accent without abandoning the bookmark save workflow. Saving the new folder inserts it into Room DB and auto-assigns `selectedCollectionId` to the newly created folder ID.
- **Alternatives Considered**:
  - *Navigating to Full Collection Screen*: Rejected because navigating away loses current clipboard/URL context.

---

### Decision 4: Collection Card Selection UI Component

- **Decision**: Build `NeobrutalistSelectableFolderCard` composable:
  - **Selected State**: Yellow container fill, yellow border, dark checkmark icon on the right, bold title text.
  - **Unselected State**: Surface background (`#FFFFFF` or `#313244`), standard solid border, icon square container.
- **Rationale**: Replicates the reference design screenshot faithfully while maintaining high-contrast visual feedback.
