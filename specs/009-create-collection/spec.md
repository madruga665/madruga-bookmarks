# Feature Specification: Create New Collection Modal

**Feature Branch**: `009-create-collection`

**Created**: 2026-08-16

**Status**: Draft

**Input**: User description: "criação de nova collection \n /home/madruga665/Downloads/Screenshot_20260816_140400_Tuckii.jpg"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Add New Collection Bottom Sheet Modal (Priority: P1) 🎯 MVP

When a user initiates the creation of a new collection (e.g. from the home screen top action bar or from within the bookmark save flow), an "Add new collection" bottom sheet modal opens. The user can enter a collection name (up to 40 characters), select a custom accent color from a 16-color palette, choose a custom icon from a comprehensive icon grid (~43 icons), and submit the form to create the collection.

**Why this priority**: Core capability for organizing bookmarks into customized folders, fulfilling Constitution Principle III (Flexible Folder Organization).

**Independent Test**: Open the modal, enter a collection name (e.g., "Design Inspiration"), select a color and icon, tap "Create collection", and verify the new collection is saved and immediately visible on the home screen collections grid.

**Acceptance Scenarios**:

1. **Given** the user is on the home screen or bookmark save modal, **When** tapping the add collection trigger, **Then** the "Add new collection" bottom sheet modal slides up with a title, close button ('X'), name text field, color selection grid, icon selection grid, and a "Create collection" action button.
2. **Given** the modal is open with the name input empty, **When** viewing the "Create collection" button, **Then** the button is in a disabled state preventing accidental empty submissions.
3. **Given** the user enters a valid collection name and taps "Create collection", **Then** the collection is persisted, the modal dismisses, and the user receives feedback that the collection was created.
4. **Given** the modal is open, **When** the user taps the 'X' button, taps outside the modal, or drags the modal handle down, **Then** the modal is dismissed and no collection is created.

---

### User Story 2 - Real-Time Color and Icon Customization (Priority: P1) 🎯 MVP

Users can personalize their collection by picking an accent color and an illustrative icon from rich pre-defined sets. The selected color immediately applies as the background highlight of the chosen icon in the grid, providing immediate visual preview before creating.

**Why this priority**: Enables fast visual recognition and customized folder categorization according to Neobrutalism aesthetics.

**Independent Test**: Select different colors from the 16-color palette and different icons from the grid; verify that the selected color renders inside the selected icon tile and persists upon collection creation.

**Acceptance Scenarios**:

1. **Given** the color picker section with 16 distinct color swatches, **When** the user taps any color swatch, **Then** that color displays a selection checkmark indicator and updates the highlight fill of the active selected icon.
2. **Given** the icon picker section with diverse category icons, **When** the user taps any icon tile, **Then** that icon becomes the active selection with a solid background fill in the chosen color, while all unselected icons remain on a neutral surface background.
3. **Given** a default state when opening the modal, **Then** a sensible default color (Yellow) and default icon (Folder) are pre-selected.

---

### User Story 3 - Collection Name Input Validation & Character Counter (Priority: P2)

The collection name input field enforces a maximum length of 40 characters and provides dynamic counter feedback (`current/40`) to guide the user.

**Why this priority**: Prevents layout clipping on folder cards and ensures consistent naming lengths across devices.

**Independent Test**: Type a name exceeding 40 characters or attempt to paste long text; verify the input caps at 40 characters and the counter reflects `40/40`.

**Acceptance Scenarios**:

1. **Given** the name text field, **When** the user types characters, **Then** the character counter dynamically updates (e.g., `0/40` to `18/40`).
2. **Given** the user attempts to enter more than 40 characters, **Then** the text input prevents entry beyond 40 characters.
3. **Given** the name input contains only whitespace, **Then** the "Create collection" action remains disabled.

---

### Edge Cases

- **Duplicate Collection Names**: If a user creates a collection with a name matching an existing collection, the system allows it (using distinct unique IDs) or displays a distinct naming helper without crashing.
- **Rapid Double-Tap on Submit**: Tapping "Create collection" multiple times in quick succession only triggers a single creation event and prevents duplicate entity insertions.
- **Device Rotation or Keyboard Appearance**: The bottom sheet content is scrollable so that the text input, color palette, icon grid, and submit button remain fully accessible when the soft keyboard is open.
- **Theme Adaptability**: All color swatches, icon tiles, borders, and text labels render legibly in both Light mode and Catppuccin Mocha Dark mode.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST provide an "Add new collection" bottom sheet modal accessible from the home screen top action bar and the bookmark saving flow.
- **FR-002**: The modal header MUST display the title "Add new collection" (localized in English and Portuguese) and a top-right dismiss button ('X').
- **FR-003**: The modal MUST provide a single-line text input field for the collection name with placeholder text (e.g., `e.g. Travel, Design Inspiration...`) and a dynamic character counter (`0/40`).
- **FR-004**: The system MUST restrict collection name input to a maximum of 40 characters and disallow blank or whitespace-only submissions.
- **FR-005**: The modal MUST provide a color selection palette containing 16 distinct high-contrast colors (including Yellow, Pink, Purple, Blue, Mint, Green, Lime, Orange, Cream, Gray, Slate Blue, Mauve, Brown, Dark Slate, Coral, and Indigo).
- **FR-006**: Tapping a color swatch MUST mark it as selected with a checkmark and dynamically update the background accent of the selected icon.
- **FR-007**: The modal MUST provide a curated grid of ~43 category icons (including folder, star, heart, book, music, camera, flag, moon, sun, cloud, pin, calendar, globe, gift, leaf, luggage, shopping cart, plane, car, coffee, film, headphones, palette, gamepad, dumbbell, money, phone, computer, clock, lightbulb, graduation cap, shield, cutlery, tv, bell, key, cube, layers, code, lightning, bookmark, tag, home).
- **FR-008**: The selected icon MUST be visually highlighted with the active color fill, high-contrast black border, and offset shadow, while unselected icons display on a neutral background.
- **FR-009**: The modal MUST feature a primary action button labeled "Create collection" (localized) pinned or accessible at the bottom of the modal.
- **FR-010**: Tapping "Create collection" with valid input MUST persist the new collection to local storage, dismiss the modal, and refresh the collections list.
- **FR-011**: All modal elements MUST adhere to Neobrutalism design tokens (thick 2.5dp borders, crisp shadow offsets, rounded corners, and vibrant accents).

### Key Entities

- **Collection**: Category entity containing `id` (unique identifier), `name` (up to 40 characters), `colorAccent` (color hex or token identifier), `iconKey` (identifier mapping to the chosen vector icon), `createdAt`, and `updatedAt`.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users can create a new personalized collection in under 10 seconds.
- **SC-002**: 100% of the 16 colors and ~43 icons render correctly and are selectable with immediate visual feedback (<50ms state update).
- **SC-003**: The bottom sheet modal maintains a smooth 60fps scrolling and entry/exit animation on standard Android test devices.
- **SC-004**: 100% of UI components in the modal adhere to Neobrutalism tokens across both Light and Dark themes.

## Assumptions

- Pre-selected defaults on opening the modal are Yellow accent color and Folder icon.
- The collection entity is persisted in the local Room database (`CollectionEntity`) and queued for cross-platform synchronization according to Constitution Principle I.
- Localization strings are provided for `en` (English) and `pt-BR` (Portuguese).
