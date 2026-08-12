# Feature Specification: Add Bookmark Bottom Sheet & Collection Selector Modal

**Feature Branch**: `002-save-bookmark-modal`

**Created**: 2026-08-11

**Status**: Draft

**Input**: User description: "funcionalidade de add um novo bookmark abre um bottom modal, para add o link e escolher em qual pasta ele vai ficar e a opção de add uma nova pasta para esse link \n /home/madruga665/Downloads/madruga665-bookmarks/Screenshot_20260811_183801_Tuckii.jpg"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Save Bookmark Bottom Sheet Modal (Priority: P1) 🎯 MVP

When a user initiates adding a new bookmark (via the quick save bar on the home screen or OS share target), a Neobrutalist bottom sheet modal opens. The modal displays the target URL, a "Pin this link" option, a list of available folder collections (with "Unsorted" selected by default), and a prominent save button reflecting the chosen destination.

**Why this priority**: Core user journey for organizing bookmarks into folders during saving (Constitution Principle III).

**Independent Test**: Can be tested by tapping the quick add button with a valid URL, verifying the bottom modal opens, displays the URL, lists available folders, and shows a bottom action button labeled `Save to "Unsorted"`.

**Acceptance Scenarios**:

1. **Given** a valid URL entered in the app or shared via OS share, **When** the user triggers the add bookmark action, **Then** a Neobrutalist bottom sheet modal slides up displaying the drag handle, title "Save to Bookmarks", target URL, and folder list.
2. **Given** the bottom sheet is open, **When** no folder selection has been changed, **Then** "Unsorted" is selected by default with a yellow container fill, checkmark icon, and the main button reads `Save to "Unsorted"`.
3. **Given** the bottom sheet is open, **When** the user taps outside the modal or drags the handle down, **Then** the modal dismisses without saving the bookmark.

---

### User Story 2 - Select Destination Collection Folder (Priority: P1) 🎯 MVP

Users can tap any collection folder in the modal list to change where the bookmark will be stored. The selected collection highlights immediately with a yellow accent fill and checkmark icon, and the main save button text updates dynamically.

**Why this priority**: Essential for organizing bookmarks into custom folders at capture time.

**Independent Test**: Can be tested by tapping a different folder card (e.g. "IA" or "Vagas") in the list and verifying the item highlights and the save button updates to `Save to "IA"`.

**Acceptance Scenarios**:

1. **Given** the open bottom modal, **When** the user taps the "IA" collection, **Then** "IA" becomes highlighted with a yellow background fill and checkmark, "Unsorted" is unselected, and the main button text changes to `Save to "IA"`.
2. **Given** a selected collection in the modal, **When** the user taps the `Save to "[Collection]"` button, **Then** the bookmark is saved into that collection, the modal closes, and a success confirmation toast is shown.

---

### User Story 3 - Create New Collection from Modal (Priority: P2)

Users can create a new folder directly inside the save modal by tapping the top-right "Create New Folder" icon button. An inline form allows entering a new folder name and choosing a color accent. Upon creation, the new folder is automatically created and selected for the current bookmark.

**Why this priority**: Prevents breaking the bookmark saving flow when a user wants to store a link in a folder that does not exist yet.

**Independent Test**: Can be tested by tapping the folder creation button in the top right of the modal, typing a new name (e.g., "Design"), confirming creation, and verifying "Design" is added to the list and selected.

**Acceptance Scenarios**:

1. **Given** the open bottom modal, **When** the user taps the top-right "Create New Folder" button, **Then** an inline creation input field or sub-modal opens requesting a collection name and color token.
2. **Given** a valid new collection name entered, **When** the user taps "Create Folder", **Then** the folder is added to the local database, appears in the list, and is automatically selected as the destination folder for the current bookmark.

---

### User Story 4 - Pin Bookmark Toggle (Priority: P3)

Users can toggle "Pin this link" in the bottom modal to mark the bookmark as pinned for priority display.

**Why this priority**: Provides quick bookmark prioritization.

**Independent Test**: Can be tested by toggling "Pin this link" and verifying `isPinned = true` is saved with the bookmark.

**Acceptance Scenarios**:

1. **Given** the open bottom modal, **When** the user taps "Pin this link", **Then** the pushpin icon highlights to indicate pinned state, and saving the bookmark persists the pinned flag.

---

### Edge Cases

- **Long Collection Names**: Collection titles in the list truncate gracefully without pushing the checkmark or icon outside card bounds.
- **Many Collections**: The collection folder list inside the bottom sheet is contained within a scrollable container so the header and main save button remain pinned and visible.
- **Empty Folder Name**: Attempting to create a new folder with a blank name displays an inline validation error inside the modal.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST open a Neobrutalist Bottom Sheet Modal when initiating bookmark creation, styled with rounded top corners, thick black 2.5dp borders, and offset shadow containers.
- **FR-002**: The bottom sheet header MUST display the title "Save to Bookmarks", the target URL in secondary text, and a top-right "Create New Folder" neobrutalist icon button.
- **FR-003**: The modal MUST render a "Pin this link" toggle row with a pushpin icon.
- **FR-004**: The modal MUST list all available user collections with their assigned icon squares, names, and a default "Unsorted" option.
- **FR-005**: Tapping any collection item in the list MUST select it, highlight it with a yellow background container and checkmark icon, and unselect the previous selection.
- **FR-006**: The primary action button at the bottom of the modal MUST dynamically format its label as `Save to "[Selected Collection Name]"`.
- **FR-007**: Tapping the "Create New Folder" action button MUST reveal a folder creation prompt allowing entry of a collection name and accent color.
- **FR-008**: Upon creating a new folder, it MUST be inserted into the database and automatically set as the selected collection in the modal.
- **FR-009**: Tapping the `Save to "[Selected Collection Name]"` button MUST save the bookmark with the chosen collection ID and pin status, dismiss the modal, and display confirmation.

### Key Entities

- **Bookmark**: Entity representing a saved link (ID, URL, title, collectionId, isPinned, createdAt).
- **Collection**: Entity representing a folder category (ID, name, colorAccent, iconKey, linkCount).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of bottom sheet components (modal container, header, folder items, pin toggle, save button) adhere to Neobrutalism design specifications in Light and Catppuccin Mocha Dark themes.
- **SC-002**: Users can select a destination folder and save a bookmark in under 3 seconds with 2 taps inside the modal.
- **SC-003**: Creating a new folder inside the modal auto-selects the new folder in under 200ms without dismissing the save workflow.

## Assumptions

- Implemented using Jetpack Compose `ModalBottomSheet` or custom Neobrutalism bottom sheet layout.
- The default destination when no specific folder is picked is "Unsorted" (`col_unsorted`).
