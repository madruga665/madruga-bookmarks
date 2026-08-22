# Feature Specification: Touch-Anchored Arc Actions Menu

**Feature Branch**: `013-arc-action-menu`

**Created**: 2026-08-22

**Status**: Ready for Planning

**Input**: User description: "correçao do menu de açoes, corrigir comportamento do menu de açoes de collection e bookmarks, o arco deve se formar com base no click do usuario: https://hasmukhbhadani.blogspot.com/2014/04/arc-menu-satellite-menu-in-android.html uma base de como eh, mas vamos manter em jetpack compose"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Dynamic Touch-Anchored Arc Menu Deployment (Priority: P1) 🎯 MVP

When a user presses and holds (long-presses) anywhere on a Collection card or Bookmark card, the action menu opens dynamically as a curved arc (satellite layout) centered directly at the user's exact touch coordinate. Satellite action buttons radiate smoothly outward from the exact contact point with an animated spring blossom effect, rather than appearing in a rigid vertical list or fixed card offset.

**Why this priority**: Anchors the radial satellite menu around the user's active touch position, delivering an intuitive, responsive gesture experience.

**Independent Test**: Can be tested by pressing and holding at various points across different cards (top, center, bottom, left side, right side) and observing the satellite action buttons blossom outward in an arc radiating from the exact finger contact location.

**Acceptance Scenarios**:

1. **Given** a user is viewing Collection cards on the Home screen or Bookmark cards in a list/search view, **When** they press and hold on any card, **Then** the satellite action buttons animate smoothly outward in a curved arc originating from the user's touch coordinate.
2. **Given** an active arc menu, **When** the menu opens, **Then** the background is dimmed with a semi-transparent backdrop and the source card remains visually elevated/focused while the satellite items blossom around the touch point.
3. **Given** the arc menu is triggered, **When** the satellite buttons finish their blossom animation, **Then** each satellite button displays its corresponding icon, high-contrast Neobrutalist styling, and its persistent label badge alongside the button for maximum clarity.

---

### User Story 2 - Boundary-Aware Arc Angle & Orientation Calculation (Priority: P1) 🎯 MVP

When the user triggers the arc menu near screen edges or corners (e.g. near the right edge, bottom edge, top bar, or left edge), the system dynamically computes the optimal arc angle range (start angle, sweep angle, radius) so that all satellite action buttons and their label badges remain fully visible and reachable within the screen viewport without clipping or overflowing off-screen.

**Why this priority**: Critical usability requirement ensuring that regardless of where a card is positioned or where the user taps, all actions remain 100% accessible and legible.

**Independent Test**: Can be tested by long-pressing cards in the top-left, top-right, bottom-left, bottom-right, and central regions of the screen, verifying that satellite buttons always project inward toward visible screen area and never get clipped by screen boundaries.

**Acceptance Scenarios**:

1. **Given** a touch event occurs near the right edge of the screen, **When** the arc menu expands, **Then** the arc projects outward to the left (between 90° and 270° relative to right horizontal) so all items stay on screen.
2. **Given** a touch event occurs near the left edge of the screen, **When** the arc menu expands, **Then** the arc projects outward to the right (between -90° and 90°).
3. **Given** a touch event occurs near the bottom of the screen, **When** the arc menu expands, **Then** the arc projects upward (towards negative Y / upper semi-circle).
4. **Given** a touch event occurs in an open center area, **When** the arc menu expands, **Then** the arc distributes items along an ergonomic semi-circular arc optimized for thumb reach.

---

### User Story 3 - Touch-and-Hold Drag-and-Release Gesture Interaction (Priority: P1) 🎯 MVP

The arc menu operates strictly while the user holds their finger pressed against the screen:
1. **Drag-and-Release (Fluid Gesture)**: The user holds down on a card to open the arc menu, keeps their finger pressed while sliding over a satellite action (which scales up with accent color highlight and haptic feedback), and releases their finger over the item to trigger that action immediately.
2. **Release-to-Dismiss (Finger Lift)**: The arc menu remains visible and active ONLY while the user's finger is in contact with the screen. If the user lifts their finger without hovering over any satellite button, the menu immediately collapses and dismisses without executing any action.

**Why this priority**: Delivers a swift, frictionless, and lightweight power-user gesture without leaving floating UI elements when the user finishes touching the screen.

**Independent Test**: Can be tested by (1) long-pressing and sliding over an action item before releasing, verifying immediate execution and dismissal; and (2) long-pressing and lifting finger outside satellite items, verifying immediate menu dismissal.

**Acceptance Scenarios**:

1. **Given** the arc menu is expanding, **When** the user drags their finger over a satellite action button, **Then** the target button scales up, activates its highlight accent color, and emits haptic feedback.
2. **Given** the user is hovering over a satellite button, **When** they release their finger, **Then** the menu dismisses and the hovered action is immediately executed.
3. **Given** the user is holding down to view the arc menu, **When** they lift their finger anywhere outside the satellite action buttons, **Then** the arc menu immediately closes and dismisses with no action taken.
4. **Given** the arc menu is deployed, **When** the gesture is cancelled or interrupted by the system, **Then** the arc menu collapses cleanly.

---

### User Story 4 - Unified Arc Actions for Collections & Bookmarks (Priority: P2)

The dynamic arc menu mechanism operates consistently across all entity types in the application:
- **Collection Cards**: Arc displays **Edit**, **Share**, and **Delete** actions.
- **Bookmark Cards**: Arc displays **Open**, **Pin / Unpin**, **Share**, and **Delete** actions.

**Why this priority**: Unifies the interaction model across the entire application so users enjoy a coherent, predictable, and delightful UX.

**Independent Test**: Can be tested by triggering the arc menu on Collection cards in `HomeScreen` and Bookmark cards in `CollectionDetailScreen` and `SearchScreen`, verifying correct contextual actions and consistent behavior.

**Acceptance Scenarios**:

1. **Given** a user long-presses a Collection card, **When** the arc menu expands, **Then** the satellite buttons correspond to Collection actions: Edit (pencil), Share (share icon), and Delete (trash bin).
2. **Given** a user long-presses a Bookmark card, **When** the arc menu expands, **Then** the satellite buttons correspond to Bookmark actions: Open (open external), Pin/Unpin (pushpin), Share (share icon), and Delete (trash bin).

---

### Edge Cases

- **Touch Near Screen Corners**: When a touch occurs in extreme screen corners (e.g. bottom-right corner), the arc angle range narrows to a quadrant (~90° span) pointing diagonally into the visible screen area so all buttons remain unobstructed.
- **Quick Tap vs. Long Press**: A quick tap (<300-350ms) on a card must immediately trigger standard card navigation (opening Collection detail or Bookmark details) without showing any flash of the arc menu.
- **Scroll Conflict**: Starting a list scroll gesture immediately cancels the long-press timer without deploying the arc menu.
- **Rapid Multi-Touch / Interrupted Gestures**: If a second touch occurs or the app is sent to background while the arc menu is active, the menu dismisses cleanly without orphan state.
- **Dynamic Pin State**: When opening a bookmark arc menu, the Pin action correctly reflects the bookmark's current pinned status (showing "Desafixar" / filled pin if pinned, "Pinnar" / outlined pin if unpinned).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST detect a touch-and-hold (long-press) gesture on Collection and Bookmark cards and record the exact touch coordinates in window space (`touchPositionInWindow`).
- **FR-002**: The system MUST render action items as satellite buttons distributed along a radial arc centered directly at the recorded touch position.
- **FR-003**: The system MUST dynamically calculate the arc opening angle and orientation based on the touch position relative to the screen dimensions to prevent items from rendering outside the viewport.
- **FR-004**: Each satellite action item MUST animate outward from the touch anchor point to its final radial distance using a smooth spring blossom animation.
- **FR-005**: When the arc menu is deployed, the surrounding UI MUST be dimmed with a backdrop while the anchor card remains visually prominent.
- **FR-006**: Each satellite action button MUST display its text badge alongside the circular icon button for immediate identification.
- **FR-008**: The arc menu MUST remain visible strictly while the user keeps their finger held on the screen.
- **FR-009**: Releasing the pointer over an active satellite item MUST trigger the corresponding action immediately and dismiss the menu.
- **FR-010**: Releasing the pointer outside of all satellite items or cancelling the gesture MUST immediately collapse and dismiss the arc menu without executing any action.
- **FR-011**: The satellite action buttons and text badges MUST adhere to the Neobrutalism design system tokens (high-contrast surfaces, bold borders, crisp drop shadows, vibrant action accents).
- **FR-012**: The arc menu MUST support Collection cards with 3 actions (Edit, Share, Delete) and Bookmark cards with 4 actions (Open, Pin/Unpin, Share, Delete).

### Key Entities

- **ArcActionItem**: Data model defining a satellite action (id, icon, label, content description, active accent color, onClick callback).
- **ArcMenuConfiguration**: Geometric calculation model defining the center anchor point, radial distance, start angle, sweep angle, item spacing, and viewport boundary constraints.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: The arc menu begins blossoming from the exact touch position within 50ms of long-press threshold detection.
- **SC-002**: 100% of satellite action buttons and their text badges remain fully visible and unobstructed within the screen viewport across all screen sizes and touch locations.
- **SC-003**: Drag-to-select interaction provides immediate visual and haptic feedback (<16ms frame time / 60+ fps animation).
- **SC-004**: Quick taps (<300ms) consistently trigger primary navigation with 0% false-positive arc menu openings.
- **SC-005**: Zero regressions in existing Collection and Bookmark management actions (Edit, Share, Delete, Pin/Unpin, Open).

## Assumptions

- Satellite radius is dimensioned comfortably for one-handed mobile touch reach (approximately 90dp to 110dp radial offset).
- Spring animation physics provide an organic Neobrutalist pop effect matching existing app animations.
- The arc menu is built as a reusable, decoupled Jetpack Compose component (`NeobrutalistArcMenu` or similar) usable across different screens.
