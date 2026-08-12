# Feature Specification: Native Android Neobrutalism Home Screen

**Feature Branch**: `001-android-home-neobrutalism`

**Created**: 2026-08-11

**Status**: Draft

**Input**: User description: "app mobile em android nativo(kotlin) com jetpackcompose implementar tela inicial com estilo neobrutalism, ter tema claro de acordo com os print e ter tema escuro baseado nas cores do catppuccin mocha \n tela inical: /home/madruga665/Downloads/madruga665-bookmarks/Screenshot_20260811_183632_Tuckii.jpg"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Neobrutalism Home Screen & Dual-Theme Experience (Priority: P1)

Users opening the mobile app see a high-contrast, neobrutalist home screen featuring bold typography, solid black outlines, hard offset drop shadows, and vibrant collection cards. When switching between light mode (based on design reference) and dark mode (based on Catppuccin Mocha), the layout seamlessly preserves high contrast and visual hierarchy.

**Why this priority**: Core visual identity and primary entry point for mobile bookmark management.

**Independent Test**: Can be tested by launching the Android application in both Light and Dark device modes, verifying all components (buttons, input fields, folder cards, top bar) render correct neobrutalist borders, hard shadows, typography, and theme colors.

**Acceptance Scenarios**:

1. **Given** the device is set to Light Mode, **When** the user opens the app, **Then** the home screen displays an off-white background, black 2dp/3dp outlines, hard black offset shadows, and bright accent colors (yellow, purple, orange) for folder tabs.
2. **Given** the device is set to Dark Mode, **When** the user opens the app, **Then** the home screen displays Catppuccin Mocha colors (Base `#1e1e2e`, Surface `#313244`, Text `#cdd6f4`, accents in Mauve, Yellow, Peach, Blue) with dark solid offset shadows.

---

### User Story 2 - Quick Link Capture from Home Screen (Priority: P1)

Users can quickly paste a URL from their device clipboard or type a web address into the main input bar and tap the prominent Add (`+`) button to save a bookmark immediately without navigating away.

**Why this priority**: Essential requirement for frictionless bookmark capture (Constitution Principle II).

**Independent Test**: Can be tested by copying a URL to the clipboard, tapping the paste icon in the quick save bar, and tapping the Add button to confirm the bookmark is queued/saved.

**Acceptance Scenarios**:

1. **Given** a valid URL in the clipboard, **When** the user taps the clipboard icon in the quick save bar, **Then** the URL is inserted into the text field.
2. **Given** a non-empty valid URL in the quick save field, **When** the user taps the Add (`+`) button, **Then** the link is saved, the input field resets, and visual confirmation is shown.
3. **Given** an invalid or empty input field, **When** the user taps the Add (`+`) button, **Then** an inline neobrutalist error message indicates that a valid web link is required.

---

### User Story 3 - Collection Browsing & Search Navigation (Priority: P2)

Users can view their bookmark collections formatted as tabbed folder cards showing icons, titles, and link counts, and tap a folder card to browse its contents or tap the top search button to access dedicated search.

**Why this priority**: Key navigation journey enabling organized browsing and instant discovery (Constitution Principles III & IV).

**Independent Test**: Can be tested by tapping a folder card (e.g., "IA" or "Vagas") to trigger folder detail navigation, or tapping the magnifying glass top button to navigate to the search screen.

**Acceptance Scenarios**:

1. **Given** existing collections ("IA", "Vagas", "Programação"), **When** the home screen renders, **Then** each collection appears as a tabbed folder card with its assigned icon, link count subtext, and background tab accent color.
2. **Given** the home screen, **When** the user taps the Search magnifying glass icon in the top right, **Then** the application navigates to the dedicated search screen.
3. **Given** a collection card, **When** the user taps on the card, **Then** the application navigates to that collection's detail list.

---

### User Story 4 - App Settings & Collection Management Triggers (Priority: P3)

Users can access app settings or manage collections directly from top-bar action buttons.

**Why this priority**: Secondary management access.

**Independent Test**: Can be tested by tapping the top-left Settings gear icon or top-right Manage Collections folder icon to verify navigation handlers fire.

**Acceptance Scenarios**:

1. **Given** the home screen top bar, **When** the user taps the Settings gear button, **Then** the settings screen or bottom sheet opens.
2. **Given** the home screen top bar, **When** the user taps the Manage Collections icon, **Then** the collection management interface opens.

---

### Edge Cases

- **Empty Collections**: When a user has zero collections, a neobrutalist empty state card encourages creating the first collection or using default categories.
- **Long Collection Titles**: Folder titles that exceed container width automatically truncate with ellipsis without breaking card layout or tab proportions.
- **Non-URL Clipboard Data**: If clipboard contains plain text without a valid URL schema (`http://` or `https://`), tapping the paste icon formats/prepends default schema or notifies user.
- **Dynamic System Theme Change**: Switching system theme while app is active updates color tokens immediately without losing input field state.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST render the home screen in native Android Jetpack Compose adhering to Neobrutalism design specifications (thick 2-3dp black outlines, zero-blur solid offset drop shadows, bold typography).
- **FR-002**: The Light Theme MUST replicate the visual design system of the provided reference screenshot (off-white background, solid black borders, hard black offset shadows, pastel vibrant folder accents: yellow, purple, orange).
- **FR-003**: The Dark Theme MUST use the Catppuccin Mocha color palette (Base `#1e1e2e`, Surface `#313244`, Text `#cdd6f4`, Subtext `#a6adc8`, with Mauve `#cba6f7`, Yellow `#f9e2af`, Peach `#fab387`, Blue `#89b4fa` accents and solid dark offset shadows `#11111b`).
- **FR-004**: The top bar MUST display a left Settings action button (gear icon) and right action buttons for Manage Collections (folder icon) and Search (magnifying glass icon), each formatted as neobrutalist icon buttons with solid borders and offset shadows.
- **FR-005**: The hero headline section MUST prominently display "Save now. Find anytime." in heavy-weight sans-serif typography.
- **FR-006**: The quick save input container MUST feature a URL input field with a prefix `#` icon, a paste button, and an adjacent Add (`+`) button with yellow/accent fill and offset shadow.
- **FR-007**: Tapping the paste button MUST read text from the Android system clipboard and populate the quick save field.
- **FR-008**: Tapping the Add button MUST validate input text, save the bookmark via backend sync/local storage, and clear the input field.
- **FR-009**: The "My Collections" section MUST display folder cards styled as file folders with colored top tabs, an internal icon square container, link count text, and collection title.
- **FR-010**: Tapping any collection card MUST initiate navigation to that collection's detail view.
- **FR-011**: Tapping the top Search button MUST initiate navigation to the dedicated search view.

### Key Entities

- **Bookmark**: Entity representing a saved link (URL, title, favicon, collection reference, timestamp).
- **Collection**: Entity representing a folder category (ID, name, color accent token, icon key, total link count).
- **ThemeConfig**: Active theme configuration state (Light, Catppuccin Mocha Dark, System).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of UI components on the home screen (buttons, inputs, cards, top bar) display visible neobrutalist outlines and hard offset shadows in both Light and Dark themes.
- **SC-002**: Users can complete saving a link from clipboard in under 3 seconds with no more than 2 taps.
- **SC-003**: Theme transitions between Light mode and Catppuccin Mocha Dark mode occur instantaneously (<50ms) with zero UI flickering or broken contrast.
- **SC-004**: Touch interaction feedback on all neobrutalist buttons occurs in under 50ms with tactile press down animation (translating towards the shadow on tap).

## Assumptions

- Target platform is native Android built with Kotlin and Jetpack Compose (Min SDK 26 / Android 8.0+).
- Theme implementation leverages Compose Material3 or custom Neobrutalism design system tokens (Colors, Shapes, Modifiers for hard offset shadows).
- Catppuccin Mocha standard hex values are used for dark theme mapping.
- Offline persistence and API synchronization comply with project Constitution v1.0.0.
