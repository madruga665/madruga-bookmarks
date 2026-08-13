# Feature Specification: Collection Long-Press Actions Menu

**Feature Branch**: `004-collection-actions-menu`

**Created**: 2026-08-12

**Status**: Draft

**Input**: User description: "ao tocar e segurar em uma colection abre um menu com opções para editar uma collection compartilhar e excluir, todas essas acções já devem estar funcionais nessa tarefa /home/madruga665/Downloads/madruga665-bookmarks/Screenshot_20260811_183702_Tuckii.jpg"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Long-Press Gesture & Floating Actions Menu Trigger (Priority: P1) 🎯 MVP

When a user touches and holds (long-presses) any collection card in the application (e.g., on the home screen "My Collections" grid), the UI dims the surrounding background and presents a floating action menu attached to the pressed collection card with three quick-action buttons: Edit (pencil icon), Share (share icon), and Delete (trash bin icon).

**Why this priority**: Core interaction gesture and visual layout enabling instant access to collection management without navigating into settings menus.

**Independent Test**: Can be tested by performing a long-press gesture on any collection card (e.g. "IA") and verifying that background drapes dim and three floating circular action buttons (Edit, Share, Delete) appear dynamically around the card.

**Acceptance Scenarios**:

1. **Given** a user is viewing collection cards, **When** they touch and hold a collection card for 500ms (long-press), **Then** a background backdrop dims the rest of the interface and floating action buttons (Edit, Share, Delete) animate into view attached to the selected card.
2. **Given** the collection actions menu is open, **When** the user taps anywhere outside the action buttons or selected card, **Then** the actions menu dismisses cleanly without taking any action and the screen returns to normal state.
3. **Given** the collection actions menu is open, **When** the user taps the device back button or performs a back gesture, **Then** the actions menu dismisses immediately.

---

### User Story 2 - Functional Collection Editing (Priority: P1) 🎯 MVP

When a user taps the Edit (pencil) button on the floating actions menu, an Edit Collection dialog/modal opens pre-filled with the current collection's details (title, accent color, icon). Saving the changes updates the collection instantly in storage and across the UI.

**Why this priority**: Essential functionality enabling users to update folder names, icons, or visual themes.

**Independent Test**: Can be tested by opening the actions menu on a collection, tapping the Edit button, modifying the title (e.g. from "IA" to "Inteligência Artificial"), submitting, and verifying the updated name appears immediately on the card and local storage.

**Acceptance Scenarios**:

1. **Given** the actions menu for collection "IA" is active, **When** the user taps the Edit (pencil) button, **Then** the menu closes and the Edit Collection modal opens with fields populated with "IA", its current accent color, and icon.
2. **Given** the Edit Collection modal is open, **When** the user changes the name and taps "Save", **Then** the changes are persisted to storage/API, the modal closes, and the collection card updates its title on screen immediately.
3. **Given** the Edit Collection modal is open, **When** the user clears the title field and taps "Save", **Then** a validation error message indicates that collection title cannot be empty.

---

### User Story 3 - Functional Collection Sharing (Priority: P1) 🎯 MVP

When a user taps the Share button on the floating actions menu, the application invokes the system OS share sheet (or share modal on web) pre-configured with a shareable link or text summary for the selected collection.

**Why this priority**: Core value driver for sharing curated bookmark collections with friends, team members, or social platforms.

**Independent Test**: Can be tested by opening the actions menu on a collection, tapping Share, and confirming that the native share sheet or share overlay opens containing the collection title and URL.

**Acceptance Scenarios**:

1. **Given** the actions menu for collection "IA" is active, **When** the user taps the Share icon button, **Then** the menu closes and the system share sheet / modal is launched containing the collection title and deep link / share URL.
2. **Given** the share interface is launched, **When** the user selects a target app (e.g., messaging or email), **Then** the target receives a formatted payload containing the collection name, description, and link.

---

### User Story 4 - Functional Collection Deletion with Confirmation (Priority: P1) 🎯 MVP

When a user taps the Delete (trash) button on the floating actions menu, a destructive confirmation dialog asks the user to confirm deletion of the collection, warning them of the action. Upon confirmation, the collection is removed from storage/API and removed from the screen grid immediately.

**Why this priority**: Mandatory lifecycle action allowing users to purge unneeded collections safely with confirmation safety guards.

**Independent Test**: Can be tested by long-pressing a collection, tapping Delete, confirming deletion in the dialog, and verifying the card disappears from the grid and cannot be retrieved from storage.

**Acceptance Scenarios**:

1. **Given** the actions menu for collection "IA" is active, **When** the user taps Delete, **Then** the actions menu closes and a confirmation dialog pops up asking "Delete 'IA' collection?".
2. **Given** the deletion confirmation dialog, **When** the user taps "Cancel", **Then** the dialog closes and the collection remains intact.
3. **Given** the deletion confirmation dialog, **When** the user taps "Delete", **Then** the collection is permanently deleted from storage/API, a success notification appears, and the collection card is removed from the grid.

---

### Edge Cases

- **Long-Press during Scroll**: If a user initiates a drag/scroll gesture on the collections list, the long-press timer is canceled so scrolling is not interrupted.
- **Deleting Collection with Bookmarks**: When deleting a collection containing bookmarks, the confirmation dialog offers options to either delete contained bookmarks or move them to unorganized bookmarks (defaulting to deleting collection and keeping bookmarks in unorganized state unless user chooses otherwise).
- **Rapid Multi-Taps**: Tapping an action button twice rapidly processes only a single action event to prevent duplicate modals or API calls.
- **Offline Mode**: Editing, sharing, or deleting collections while offline queues the changes locally and updates the UI immediately according to Constitution Principle V.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST detect a long-press gesture (touch and hold threshold ~500ms) on any collection card component.
- **FR-002**: Upon long-press trigger, the system MUST display a contextual actions overlay highlighting the selected collection card with dimmed background overlay.
- **FR-003**: The actions overlay MUST display three distinct circular Neobrutalist quick action buttons anchored around the selected card: Edit (pencil icon), Share (share icon), and Delete (trash bin icon).
- **FR-004**: Tapping outside the action buttons or pressing the back hardware button MUST immediately dismiss the actions overlay.
- **FR-005**: Tapping the Edit action button MUST launch the Edit Collection interface pre-populated with current collection name, icon, and accent color.
- **FR-006**: Submitting valid updates in the Edit Collection interface MUST persist changes to storage and update the UI in real time.
- **FR-007**: Tapping the Share action button MUST trigger the OS native share target API or system share modal populated with the collection name and share URL.
- **FR-008**: Tapping the Delete action button MUST prompt a modal confirmation dialog before executing collection deletion.
- **FR-009**: Confirming deletion MUST permanently delete the collection record from local persistence / API backend and remove the card from the UI grid with smooth transition.
- **FR-010**: All action triggers and modals MUST adhere to cross-platform UI consistency and Neobrutalism design system tokens (2.5dp black borders, strong shadows, high-contrast action buttons).

### Key Entities

- **Collection**: Folder entity containing `id`, `name`, `accentColor`, `iconKey`, `linkCount`, `createdAt`, `updatedAt`.
- **Collection Action Event**: Transient UI state capturing `selectedCollectionId`, `isMenuVisible`, and active overlay coordinates.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Long-press gesture triggers and renders the quick actions menu in under 100ms on touch release or threshold completion.
- **SC-002**: 100% of collection management actions (Edit, Share, Delete) function end-to-end with persistence updates.
- **SC-003**: Deleting a collection updates local storage and removes the visual card from the grid in under 150ms.
- **SC-004**: 0 accidental menu activations during fast scrolling or tapping to open collection details.

## Assumptions

- Tapping a collection card quickly (<300ms) opens the Collection Bookmarks List View (Feature 003), whereas holding (>500ms) triggers the Actions Menu.
- Deleting a collection defaults to moving contained bookmarks to the "Unorganized" root folder unless the user opts to purge all contained bookmarks.
- Share link format relies on standard deep link scheme or web URL generated by backend API.
