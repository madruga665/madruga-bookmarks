# Feature Specification: Bookmark Long-Press Actions Menu

**Feature Branch**: `011-bookmark-actions-menu`

**Created**: 2026-08-18

**Status**: Ready for Planning

**Input**: User description: "add o mesmo mecanismo de precionar e segugar que temos nas collections nos links, ao precionar e segurar vamos ter as opções de: Abrir, Pinnar, compartilhar e exluir um link"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Long-Press Gesture & Radial Floating Actions Menu Overlay (Priority: P1) 🎯 MVP

When a user touches and holds (presses and holds) any bookmark card across the application (in `CollectionDetailScreen`, `SearchScreen`, or any bookmark list), the UI dims the surrounding background with a semi-transparent backdrop and highlights the active bookmark card with subtle elevation and tilt. Around the touch point / card edge, a radial/arc menu displays 4 quick-action buttons:
1. **Abrir** (Open link in browser)
2. **Pinnar / Desafixar** (Toggle Pin state)
3. **Compartilhar** (Share link)
4. **Excluir** (Delete link with confirmation)

The user can drag their finger across the radial arc with real-time hover highlight and haptic feedback. Releasing their finger over an action triggers it immediately; releasing outside or tapping the backdrop dismisses the menu cleanly.

**Why this priority**: Core interaction gesture and visual layout enabling instant contextual actions on bookmarks without navigating into details first.

**Independent Test**: Can be tested by pressing and holding any bookmark card, observing the dimming backdrop and 4 radial arc action buttons appear around the interaction point, dragging over each option to feel haptics and see visual highlight state, and releasing to trigger or releasing outside to cancel.

**Acceptance Scenarios**:

1. **Given** a user is viewing bookmark cards in `CollectionDetailScreen` or `SearchScreen`, **When** they touch and hold a bookmark card for ~350ms, **Then** the interface dims with a backdrop, the card elevates with subtle tilt/scale, and 4 quick-action buttons (Open, Pin/Unpin, Share, Delete) appear in a smooth radial arc layout anchored around the touch point / card.
2. **Given** the radial actions menu is active, **When** the user drags their finger over any action button along the arc, **Then** that button scales up with active accent color highlighting and a subtle haptic feedback vibration is performed.
3. **Given** the radial actions menu is active, **When** the user releases their finger while hovering over an action, **Then** the menu dismisses and that specific action is immediately executed.
4. **Given** the radial actions menu is active, **When** the user lifts their finger away from all buttons or taps the dimmed backdrop, **Then** the menu dismisses cleanly with no action taken.
5. **Given** a user performs a quick single tap (<350ms) on a bookmark card, **When** they tap the card, **Then** the app navigates directly to the `BookmarkDetailScreen` without opening the actions menu.

---

### User Story 2 - Open Bookmark Action (Priority: P1) 🎯 MVP

When the user selects the **Abrir** (Open) option from the radial actions menu, the system immediately launches the bookmark's target URL in the device's default web browser / custom tab.

**Why this priority**: Direct web navigation is the fundamental primary use case for saved links.

**Independent Test**: Can be tested by long-pressing a bookmark card, selecting the "Abrir" action, and verifying that the external web browser launches with the bookmark's URL.

**Acceptance Scenarios**:

1. **Given** the bookmark actions menu is open for a link with a valid URL, **When** the user selects the "Abrir" (Open) action, **Then** the actions menu dismisses and the external web browser is launched loading the URL.
2. **Given** an invalid or empty URL, **When** the user attempts to open the link, **Then** an error toast notifies the user of the invalid URL.

---

### User Story 3 - Toggle Pin / Unpin Bookmark Action (Priority: P1) 🎯 MVP

When the user selects the **Pinnar / Desafixar** (Pin / Unpin) option from the radial actions menu, the system toggles the bookmark's pinned state (`isPinned`), updating local persistence and dynamically reordering the bookmark in the collection grid (moving it to/from the "Pinned" section) in real-time.

**Why this priority**: Instant prioritization of critical bookmarks directly from the list without opening edit screens.

**Independent Test**: Can be tested by long-pressing an unpinned bookmark card, selecting "Pinnar", and verifying the card moves into the pinned section; then repeating to "Desafixar" and verifying it returns to the regular list.

**Acceptance Scenarios**:

1. **Given** an unpinned bookmark (`isPinned == false`), **When** the user opens the actions menu and selects "Pinnar", **Then** the menu closes, the bookmark's `isPinned` status updates to `true`, and it moves to the Pinned header section with immediate visual feedback.
2. **Given** an already pinned bookmark (`isPinned == true`), **When** the user opens the actions menu, **Then** the pin button visually indicates the "Desafixar" (Unpin) state, and when selected, the bookmark's `isPinned` status updates to `false` and it returns to the standard bookmark grid.

---

### User Story 4 - Share Bookmark Action (Priority: P1) 🎯 MVP

When the user selects the **Compartilhar** (Share) option from the radial actions menu, the system launches the native OS share sheet populated with the bookmark title and URL.

**Why this priority**: Enables effortless sharing of saved links with third-party apps or contacts.

**Independent Test**: Can be tested by long-pressing a bookmark card, selecting "Compartilhar", and confirming that the native Android share sheet opens containing the bookmark title and link URL.

**Acceptance Scenarios**:

1. **Given** the actions menu is active for a bookmark, **When** the user selects "Compartilhar", **Then** the actions menu closes and the system share sheet displays formatted text: `"<Bookmark Title> - <Bookmark URL>"`.
2. **Given** the system share sheet is shown, **When** the user selects a target app (e.g., messaging or email), **Then** the selected target receives the bookmark URL.

---

### User Story 5 - Delete Bookmark with Confirmation Dialog (Priority: P1) 🎯 MVP

When the user selects the **Excluir** (Delete) option from the radial actions menu, the menu closes and a Neobrutalist confirmation dialog prompts the user to confirm deletion. Upon confirmation, the bookmark is permanently removed from storage and disappears from the UI immediately.

**Why this priority**: Essential lifecycle operation with safety confirmation against accidental data loss.

**Independent Test**: Can be tested by long-pressing a bookmark card, selecting "Excluir", confirming in the dialog, and observing the card disappear from the collection in under 150ms.

**Acceptance Scenarios**:

1. **Given** the actions menu is active for a bookmark, **When** the user selects "Excluir", **Then** the actions menu closes and a confirmation dialog appears asking the user to confirm deletion of the bookmark.
2. **Given** the deletion confirmation dialog, **When** the user taps "Cancelar", **Then** the dialog closes and the bookmark remains intact.
3. **Given** the deletion confirmation dialog, **When** the user taps "Excluir", **Then** the bookmark is permanently deleted from the database and the UI updates in real-time.

---

### Edge Cases

- **Scroll Gesture Conflict**: If a user drags or scrolls the bookmark grid, the long-press gesture recognizer must cancel immediately to ensure standard scroll fluidity is never impeded.
- **Screen Boundary Positioning for Radial Arc**: The radial arc dynamically clamps within screen boundaries based on the touch position / card location (e.g., fanning left if touch is near the right screen edge, fanning right if touch is near the left edge, and adjusting angle if near top/bottom).
- **Short Tap vs Long Press**: A quick tap (<350ms) navigates directly to `BookmarkDetailScreen`, while holding (>=350ms) engages the radial actions overlay.
- **Rapid Actions & Double Taps**: Action button selections are debounced to prevent duplicate share intents or duplicate delete dialogs.
- **Cross-Screen Usability**: Reusable across `CollectionDetailScreen`, `SearchScreen`, and future bookmark lists.
- **Offline Mode**: Pinning and deleting actions succeed locally and update Room DB immediately, queueing remote sync.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST detect a long-press touch gesture (~350ms threshold) on any `NeobrutalistBookmarkCard` instance across `CollectionDetailScreen`, `SearchScreen`, and all bookmark list surfaces.
- **FR-002**: Upon long-press trigger, the system MUST display a full-screen contextual actions overlay that dims the background and renders the pressed bookmark card in an elevated layer.
- **FR-003**: The overlay MUST display 4 distinct Neobrutalist circular quick-action buttons arranged in an interactive radial / arc menu around the touch point / card:
  1. **Abrir** (Open URL - external browser icon)
  2. **Pinnar / Desafixar** (Pin / Unpin toggle - pushpin / bookmark icon with dynamic state indicator)
  3. **Compartilhar** (Share - share icon)
  4. **Excluir** (Delete - trash icon)
- **FR-004**: The system MUST support continuous touch-drag tracking, updating the currently hovered action button in the radial arc with scaling, color accent highlighting, and haptic feedback.
- **FR-005**: Releasing touch over an action button MUST execute that action immediately and dismiss the overlay.
- **FR-006**: Releasing touch outside of any action button, or tapping the dimmed backdrop, or pressing the device back key MUST immediately dismiss the overlay without performing any action.
- **FR-007**: Tapping or selecting the "Abrir" action MUST open the bookmark URL in an external browser or custom tab.
- **FR-008**: Tapping or selecting the "Pinnar" / "Desafixar" action MUST toggle the bookmark's `isPinned` state in the database and reorder/update the UI in real-time.
- **FR-009**: Tapping or selecting the "Compartilhar" action MUST invoke the native Android Share Intent with the bookmark title and URL.
- **FR-010**: Tapping or selecting the "Excluir" action MUST present a modal confirmation dialog before permanently removing the bookmark from local persistence.
- **FR-011**: Quick single-tap (<350ms) on a bookmark card MUST continue to navigate to the `BookmarkDetailScreen`.
- **FR-012**: All overlay elements, floating radial buttons, and confirmation dialogs MUST adhere to the Neobrutalism design system tokens (2.5dp black borders, bold shadows, high-contrast palette).
- **FR-013**: All user-facing text strings (button labels, toasts, dialogs) MUST support localization in Portuguese (default) and English via string resources.

### Key Entities

- **Bookmark**: The entity being managed, containing `id`, `url`, `title`, `description`, `collectionId`, `isPinned`, `createdAt`, `updatedAt`, `tags`.
- **BookmarkOption**: Enum representing the available long-press actions: `OPEN`, `PIN`, `SHARE`, `DELETE`.
- **BookmarkActionOverlayState**: Transient UI state capturing `activeBookmark`, `cardOffset`, `cardSize`, `touchPositionInWindow`, `hoveredOption`.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Long-press gesture triggers and displays the radial actions overlay within 100ms of reaching the hold threshold.
- **SC-002**: 100% of bookmark actions (Open, Pin/Unpin, Share, Delete) execute reliably with instant UI state updates across all bookmark screens (`CollectionDetailScreen`, `SearchScreen`).
- **SC-003**: Pinning/Unpinning a bookmark updates the local database and reorders the list visually in under 100ms.
- **SC-004**: Deleting a bookmark removes it from the screen and database in under 150ms after user confirmation.
- **SC-005**: 0 accidental long-press activations during standard grid scrolling or short taps.

## Assumptions

- Short tap (<350ms) on a bookmark navigates to `BookmarkDetailScreen`, while long-press (>=350ms) activates the radial actions overlay.
- The radial arc button coordinates dynamically adapt relative to the touch position and screen boundaries so that no buttons clip outside viewport edges.
- Pinning a bookmark updates its `isPinned` flag in Room DB, causing reactive Flows to automatically reorder the grid.
- Sharing a bookmark uses Android's native `Intent.ACTION_SEND` with `text/plain` mime type.
- Deletion is protected by a Neobrutalist confirmation dialog.
