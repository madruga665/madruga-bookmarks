# Feature Specification: Collection Bookmarks List View

**Feature Branch**: `003-collection-bookmarks-list`

**Created**: 2026-08-12

**Status**: Draft

**Input**: User description: "listar bookmarks dentro das collections /home/madruga665/Downloads/madruga665-bookmarks/Screenshot_20260811_183643_Tuckii.jpg"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Collection Header & Navigation (Priority: P1) 🎯 MVP

When a user selects a collection from the home screen or navigation menu, the app opens the collection view with a dedicated header showing the collection name, total link and subcollection count, a back button, a quick add-link button pre-selected for this collection, and a collection settings menu.

**Why this priority**: Essential top-level frame for viewing and managing any specific collection folder (Constitution Principle III).

**Independent Test**: Can be tested by tapping a collection card (e.g. "Vagas") on the home screen and verifying the detail view header displays "Vagas", "2 links · 0 subcollections", a functional back button, add-link button, and overflow menu button.

**Acceptance Scenarios**:

1. **Given** a user is on the home screen, **When** they tap on the "Vagas" collection card, **Then** the app navigates to the Collection Details view displaying "Vagas" in the center top title and "2 links · 0 subcollections" in the subtitle.
2. **Given** the collection details view is open, **When** the user taps the top-left back button, **Then** the view returns to the previous screen (Home).
3. **Given** the collection details view is open, **When** the user taps the top-right add-link button (yellow container with link icon), **Then** the Add Bookmark Modal opens with the current collection pre-selected as the destination.

---

### User Story 2 - Bookmark Cards Grid View inside Collection (Priority: P1) 🎯 MVP

Users can view all bookmarks stored in the current collection displayed in a 2-column Neobrutalist grid labeled "ALL LINKS ([N])". Each card displays a preview thumbnail, bookmark title, domain/platform icon, and platform handle or domain name.

**Why this priority**: Primary utility of the feature—letting users browse, locate, and interact with bookmarks inside a collection.

**Independent Test**: Can be tested by opening a collection with 2 bookmarks and confirming a 2-column grid renders under the section title "ALL LINKS (2)" with thumbnail previews, titles, and platform badges.

**Acceptance Scenarios**:

1. **Given** a collection containing 2 saved links, **When** the collection details view loads, **Then** a section heading "ALL LINKS (2)" is displayed above a 2-column grid containing 2 distinct bookmark cards.
2. **Given** a bookmark card in the grid, **When** viewed, **Then** it renders a Neobrutalist card border, top image preview or placeholder, title text truncated to a maximum of 3 lines, and a bottom row displaying the platform logo and source tag (e.g. "@LinkedIn").
3. **Given** a bookmark card in the grid, **When** the user taps it, **Then** the target URL opens in the browser or in-app viewer.

---

### User Story 3 - Empty Collection View (Priority: P2)

When a user opens a collection that contains zero bookmarks and zero subcollections, the view displays a friendly Neobrutalist empty state illustration and a prominent "Add your first link" action button.

**Why this priority**: Prevents a blank screen experience for newly created or empty collections.

**Independent Test**: Can be tested by navigating to an empty collection and verifying the empty state illustration and action button appear instead of an empty list.

**Acceptance Scenarios**:

1. **Given** an empty collection, **When** the user opens it, **Then** an empty state banner is displayed with text "No bookmarks yet" and a call-to-action button "Add Link".
2. **Given** the empty state banner, **When** the user taps "Add Link", **Then** the Add Bookmark Modal opens pre-selected for the current collection.

---

### User Story 4 - Subcollection Navigation & Filtering (Priority: P3)

When a collection has nested subcollections, a "SUBCOLLECTIONS" section appears above the main links section, allowing users to tap into nested folders or filter content.

**Why this priority**: Supports hierarchical bookmark organization for advanced folder management.

**Independent Test**: Can be tested by viewing a collection with 1 subcollection, verifying the subcollection appears in a dedicated section, and tapping it navigates into the subcollection view.

**Acceptance Scenarios**:

1. **Given** a collection with 1 subcollection and 2 links, **When** viewed, **Then** the header subtitle reads "2 links · 1 subcollection" and a subcollection card list is rendered above the "ALL LINKS (2)" section.
2. **Given** a subcollection item in the list, **When** the user taps it, **Then** the app navigates into that nested collection view with updated header context.

---

### Edge Cases

- **Long Bookmark Titles**: Bookmark titles exceeding 3 lines truncate with ellipsis without breaking the card container height or pushing metadata off-screen.
- **Missing Thumbnails**: Links without preview images render a styled geometric Neobrutalist fallback pattern matching the collection theme color.
- **Offline Link Access**: Cards display cached metadata seamlessly when device is offline in accordance with Constitution Principle V.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST render a Collection Details view featuring a top bar with a back button, collection title, total link count and subcollection count subtitle, quick add link action button, and collection options menu.
- **FR-002**: Tapping the top-left back button MUST navigate back to the previous navigation screen.
- **FR-003**: Tapping the top-right quick add link button MUST launch the bookmark creation modal pre-populated with the current collection as the target destination.
- **FR-004**: The view MUST display a section header formatted as "ALL LINKS ([Total Count])".
- **FR-005**: Bookmarks MUST be displayed in a responsive 2-column grid layout on mobile screens.
- **FR-006**: Each bookmark card MUST feature Neobrutalist styling with 2.5dp black borders, rounded corners, shadow offset, a top preview thumbnail (or geometric placeholder), title text, and source domain/platform badge with icon.
- **FR-007**: Tapping a bookmark card MUST trigger opening the saved URL in the default browser or custom tab.
- **FR-008**: For empty collections, the system MUST display an empty state banner with an inline "Add Link" button.
- **FR-009**: The system MUST update link counters dynamically when bookmarks are added to or removed from the collection.

### Key Entities

- **Collection**: Folder entity containing ID, name, parentCollectionId (optional), colorAccent, iconKey, linkCount, and subcollectionCount.
- **Bookmark**: Link entity containing ID, URL, title, thumbnailUri, sourcePlatform, collectionId, isPinned, createdAt.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of collection view UI components (header, grid, cards, empty states) adhere to Neobrutalism design system standards across light and dark themes.
- **SC-002**: The collection view renders and displays up to 50 bookmark cards in under 200ms from local persistence.
- **SC-003**: Users can open any bookmark in 1 tap with response time under 100ms.
- **SC-004**: Adding a link via the header action button pre-fills the current collection ID with 100% accuracy.

## Assumptions

- Navigation to collection details passes `collectionId` as a navigation parameter.
- Default link sorting inside collection view is by creation date (newest first), with pinned items pinned to top if applicable.
