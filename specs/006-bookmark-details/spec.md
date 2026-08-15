# Feature Specification: Bookmark Details View

**Feature Branch**: `006-bookmark-details`

**Created**: 2026-08-15

**Status**: Draft

**Input**: User description: "ao clicar em um link exibir detalhes dele /home/madruga665/Downloads/madruga665-bookmarks/Screenshot_20260815_031808_Tuckii.jpg"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Full-Screen Bookmark Details Navigation & Header Actions (Priority: P1) 🎯 MVP

When a user taps on any bookmark card in a collection or search view, the app navigates to a dedicated full-screen Bookmark Details route (`NavRoutes.bookmarkDetail(bookmarkId)`). The top bar displays:
- **Top Left**: Source platform or collection badge in uppercase bold.
- **Top Right**: Action button row with Neobrutalist styling:
  1. **Refresh Metadata** (circular arrow): Re-extracts title, preview image, and description from the URL.
  2. **Share** (share icon): Opens native Android share sheet with title and URL.
  3. **Move Collection** (4-way arrows): Opens a Neobrutalist bottom sheet to select a new destination collection.
  4. **Delete** (red trash bin): Opens a confirmation dialog to delete the bookmark.

**Why this priority**: Essential top-level screen and navigation flow for inspecting bookmark content and executing item-level management operations.

**Independent Test**: Tap any bookmark card in a collection list; verify that the app navigates to the Bookmark Details screen, shows the top bar with platform badge and 4 action buttons, and pressing back returns cleanly to the collection.

**Acceptance Scenarios**:

1. **Given** a user is viewing bookmarks in a collection, **When** they tap a bookmark card, **Then** the app navigates to the Bookmark Details screen.
2. **Given** the Bookmark Details screen, **When** the user taps the Share button, **Then** the native Android Share sheet opens with the bookmark URL and title.
3. **Given** the Bookmark Details screen, **When** the user taps the device or app back button, **Then** the screen pops back to the previous collection view.

---

### User Story 2 - Hero Image Preview with Pin/Unpin Toggle (Priority: P1) 🎯 MVP

The screen presents a full-width Hero thumbnail preview (or themed Neobrutalist placeholder pattern if none exists). An overlay button at the top-right of the hero image allows toggling the bookmark's pinned status (`isPinned`). When pinned, the bookmark appears in a dedicated "PINNED" section at the top of the collection list view, preceding the "ALL LINKS" section.

**Why this priority**: Visual identification of content and fast-access prioritization via pinning.

**Independent Test**: Tap the pin button on the hero image to toggle `isPinned`; navigate back to the collection screen and verify the item appears in the "PINNED" section.

**Acceptance Scenarios**:

1. **Given** the Bookmark Details screen, **When** the hero image renders, **Then** an overlay Pin icon button is visible on the top-right of the thumbnail.
2. **Given** an unpinned bookmark, **When** the user taps the Pin overlay button, **Then** `isPinned` is set to `true`, the icon highlights as pinned, and on the collection screen this bookmark is displayed in the "PINNED" section.
3. **Given** a pinned bookmark, **When** the user taps the Pin overlay button, **Then** `isPinned` is set to `false` and the bookmark moves back to the regular "ALL LINKS" section.

---

### User Story 3 - Inline Title Editing & URL Action Card (Priority: P1) 🎯 MVP

Beside the high-contrast Title, an Edit pencil button allows transforming the title into an inline editable `TextField` directly on the page, with "Salvar" and "Cancelar" action buttons. Below the metadata subtitle, a prominent yellow Neobrutalist URL card displays the link and launches it in the external web browser upon tap.

**Why this priority**: Quick inline title corrections and frictionless single-tap URL navigation.

**Independent Test**: Tap the edit pencil icon beside the title, modify the text, click "Salvar", and confirm the new title is persisted; tap the yellow URL card to verify it opens in the default web browser.

**Acceptance Scenarios**:

1. **Given** the Bookmark Details screen, **When** the user taps the Edit pencil button next to the title, **Then** the title switches to an inline text field displaying "Salvar" and "Cancelar" buttons.
2. **Given** title editing mode, **When** the user modifies the text and taps "Salvar", **Then** the new title is saved locally, queued for sync, and the view reverts to display mode.
3. **Given** title editing mode, **When** the user taps "Cancelar", **Then** changes are discarded and the original title is restored.
4. **Given** the Bookmark Details screen, **When** the user taps the yellow URL card, **Then** the URL launches in the default web browser.

---

### User Story 4 - Expandable Description, Personal Notes & Tag Management (Priority: P2)

- **Description**: Displays the scraped excerpt/summary with a "Show more" / "Show less" toggle for long text.
- **Tags**: Displays tag chips with 'X' deletion buttons, and a "+ Add" button that opens a compact input dialog to type and attach new tags.
- **Notes**: Displays a multi-line "NOTES" text area ("Tap to add notes...") with explicit "Salvar" and "Cancelar" buttons when editing/focused, saving personal notes locally.

**Why this priority**: Enriches bookmark context and enables flexible post-capture knowledge management (Constitution Principle III).

**Independent Test**: Expand/collapse the description, add and remove tags via the "+ Add" dialog, and write/save notes verifying they persist across screen reopens.

**Acceptance Scenarios**:

1. **Given** a long description, **When** the user taps "Show more", **Then** the full description expands and the button becomes "Show less".
2. **Given** the TAGS section, **When** the user taps "+ Add", **Then** a compact dialog appears to enter a tag name; on submit, a new Neobrutalist tag chip is added to the bookmark.
3. **Given** a tag chip, **When** the user taps the 'X' icon on the chip, **Then** the tag is removed from the bookmark.
4. **Given** the NOTES section, **When** the user edits the notes field and taps "Salvar", **Then** the updated notes are saved.

---

### User Story 5 - Move Collection & Delete Bookmark (Priority: P2)

- **Move Collection**: Tapping the top 4-way arrows button opens a Neobrutalist bottom sheet listing all user collections. Selecting a collection moves the bookmark and updates the collection ID.
- **Delete Bookmark**: Tapping the top red trash bin button opens a Neobrutalist confirmation dialog with "Excluir" and "Cancelar". Confirming deletes the bookmark from local storage, queues deletion for sync, and returns to the previous screen.

**Why this priority**: Essential lifecycle operations for organizing folders and removing unwanted bookmarks.

**Independent Test**: Tap Move to reassign the bookmark's collection, or tap Delete, confirm in the dialog, and verify the bookmark is removed from the list.

**Acceptance Scenarios**:

1. **Given** the Bookmark Details screen, **When** the user taps the Move icon in the top bar, **Then** a bottom sheet displays all available collections; selecting a collection reassigns the bookmark.
2. **Given** the Bookmark Details screen, **When** the user taps the red Delete icon in the top bar, **Then** a confirmation dialog opens with "Excluir" and "Cancelar".
3. **Given** the delete confirmation dialog, **When** the user taps "Excluir", **Then** the bookmark is deleted, the details screen pops back, and the collection list no longer shows that bookmark.

---

### Edge Cases

- **Missing Hero Thumbnail**: Links without an image thumbnail render a styled geometric Neobrutalist placeholder pattern with the collection accent color.
- **Long URLs**: Very long URLs inside the URL card truncate in the middle or end with ellipsis while remaining fully clickable.
- **Network Metadata Refresh Failure**: If metadata refresh fails (e.g. offline or 404), display a user-friendly toast/snackbar error without erasing existing title or description.
- **Offline Editing**: All title, note, tag, collection, and pin modifications persist immediately to local Room database and set `sync_status = "PENDING_SYNC"`.
- **Browser Launch Failure**: If no browser app is available to handle the URL, a toast notification informs the user.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Clicking any bookmark card in collection and search views MUST navigate to the Bookmark Details screen (`NavRoutes.bookmarkDetail(bookmarkId)`).
- **FR-002**: The top bar MUST display the platform/collection badge on the left, and 4 Neobrutalist action buttons on the right: Refresh Metadata, Share, Move Collection, and Delete.
- **FR-003**: The Hero thumbnail MUST render at the top with a top-right overlay button to toggle `isPinned`.
- **FR-004**: The collection view MUST display pinned bookmarks in a dedicated "PINNED" section above "ALL LINKS".
- **FR-005**: Tapping the Edit pencil icon next to the title MUST toggle an inline editable text field with "Salvar" and "Cancelar" buttons.
- **FR-006**: The view MUST display a metadata subtitle showing category/tags and formatted timestamp (e.g., `11 Aug 2026, 3:09 PM`).
- **FR-007**: The view MUST display a DESCRIPTION section with a "Show more" / "Show less" toggle for descriptions longer than 4 lines.
- **FR-008**: The view MUST display a prominent yellow Neobrutalist URL action card that opens the URL in the default browser when tapped.
- **FR-009**: The view MUST display a TAGS section with tag chips (featuring 'X' remove buttons) and a "+ Add" button that opens a dialog to add new tags.
- **FR-010**: The view MUST display a NOTES section with a multi-line text field and "Salvar" / "Cancelar" actions when editing.
- **FR-011**: Tapping the Move button MUST open a Neobrutalist collection selection bottom sheet.
- **FR-012**: Tapping the red Delete button MUST prompt with a Neobrutalist confirmation dialog before removing the bookmark and popping back.
- **FR-013**: All UI components MUST adhere to the Neobrutalism design system (2.5dp black borders, 4dp shadow offsets, bold high-contrast colors).

### Key Entities

- **Bookmark**: Link entity containing ID, URL, title, description, faviconUrl, thumbnailUrl, sourcePlatform, collectionId, tags (list of strings), notes (string), isPinned, createdAt, updatedAt, syncStatus.
- **Collection**: Folder entity containing ID, name, iconKey, colorAccent.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Navigating from a bookmark card to Bookmark Details occurs in under 150ms.
- **SC-002**: 100% of UI elements match Neobrutalism design tokens across light and dark themes.
- **SC-003**: Tapping the URL card launches the external browser in under 100ms.
- **SC-004**: Title, notes, tags, and pin changes persist locally immediately with zero data loss even when offline.
- **SC-005**: Pinned bookmarks appear in the "PINNED" section of their respective collection with 100% accuracy.

## Assumptions

- Room schema for `bookmarks_table` is updated (or migrated) to include `description`, `notes`, and `tags` fields if not already present.
- Deep linking and standard Compose navigation handle passing `bookmarkId`.
