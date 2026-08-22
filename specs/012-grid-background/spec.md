# Feature Specification: Subtle Neobrutalist Grid Background Pattern

**Feature Branch**: `012-grid-background`

**Created**: 2026-08-22

**Status**: Aligned

**Input**: User description: "no fundo vamos add linhas formando um quadriculado, essas linhas vao ser em um tom cinza claro bem discreto"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Discreet Grid Pattern on App Backgrounds (Priority: P1)

Users browsing the application (Home, Search, Settings, Collection Detail, Bookmark Detail) see a subtle, discreet grid pattern composed of fine horizontal and vertical intersecting lines in a light gray tone on the background surface. The grid has uniform 24dp x 24dp cells with a 1dp stroke, evoking a technical neobrutalist notebook/canvas aesthetic. The grid background is fixed in place, allowing cards, buttons, and content to scroll smoothly over it without visual jitter.

**Why this priority**: Core visual enhancement requested by the user, providing an authentic Neobrutalist canvas backdrop across all screens.

**Independent Test**: Can be tested by navigating to any screen (Home, Search, Collection Detail, Bookmark Detail, Settings) in Light mode and verifying that a subtle, non-intrusive 24dp x 24dp light gray grid pattern is visible as a fixed background while content scrolls over it.

**Acceptance Scenarios**:

1. **Given** the user is on any main screen (Home, Search, Settings, Collection Detail, Bookmark Detail) in Light Mode, **When** the screen is displayed, **Then** the background canvas renders a repeating 24dp x 24dp square grid with subtle, discrete light gray lines (1dp stroke).
2. **Given** a screen with scrollable content, **When** the user scrolls vertically or horizontally, **Then** the background grid remains fixed while content cards and lists scroll smoothly over it at 60/120fps with zero jitter or artifacting.

---

### User Story 2 - Dark Mode (Catppuccin Mocha) Adaptive Grid (Priority: P2)

When Dark Mode is enabled, the grid background automatically harmonizes with the Catppuccin Mocha theme. Instead of light gray, the grid lines adopt a subtle, low-opacity dark slate tone against the Mocha Base (`#1E1E2E`) background, maintaining the subtle technical aesthetic without causing glare or distraction in low-light environments.

**Why this priority**: Ensures visual comfort, accessibility, and theme consistency across both Light and Dark modes (Constitution Principle V).

**Independent Test**: Can be tested by switching between Light and Dark themes in Settings and observing the grid pattern adapting cleanly with appropriate subtle contrast in each theme.

**Acceptance Scenarios**:

1. **Given** the app is in Dark Mode, **When** viewing any screen, **Then** the grid lines render in a subtle dark-toned color harmonized with the Catppuccin Mocha palette.
2. **Given** the user toggles theme between Light and Dark, **When** the theme switches, **Then** the grid background colors update dynamically and instantly.

---

### User Story 3 - Visual Hierarchy and Container Opacity (Priority: P3)

All cards, buttons, text fields, bottom sheets, and dialog overlays have opaque surface backgrounds that sit cleanly above the fixed grid background. The grid lines remain visible only in margins, gutters, and unoccupied background areas, preserving 100% readability of content, typography, and interactive controls.

**Why this priority**: Preserves content legibility and high visual contrast so the grid pattern serves purely as an atmospheric backdrop without compromising usability.

**Independent Test**: Can be tested by inspecting cards, bottom sheets, and input fields on Home, Search, and Detail screens, confirming that grid lines do not bleed through solid containers.

**Acceptance Scenarios**:

1. **Given** cards (e.g. Bookmark cards, Collection cards) or interactive fields displayed on any screen, **When** rendered over the grid background, **Then** the content surfaces are fully opaque and obscure the grid lines behind them.
2. **Given** an open bottom sheet or dialog (e.g., Save Bookmark, Collection Actions, Edit Dialog), **When** displayed, **Then** the overlay surface completely covers the background grid beneath its bounds.

---

### Edge Cases

- **Large & High-Density Displays (Tablets, Foldables, Large Phones)**:
  - The grid pattern scales gracefully and tiles uniformly across large viewports without visible seams, aliasing, or distortion.
- **Rapid Scrolling Performance**:
  - The fixed pattern rendering introduces negligible GPU/CPU overhead, ensuring sustained 60fps/120fps scrolling in LazyColumns, LazyRows, and Scrollable Columns.
- **Dynamic Orientation Changes (Portrait <-> Landscape)**:
  - When the screen rotates, the grid redraws seamlessly to fill the new viewport dimensions without visual glitches.
- **Overlays and Semi-Transparent Scrims**:
  - When modal sheets or dialogs display a dimmed backdrop scrim, the grid background underneath is appropriately dimmed along with the rest of the background canvas.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST define visual design tokens for the grid pattern (cell size: 24dp, stroke width: 1dp, grid line color for Light and Dark themes).
- **FR-002**: In Light Mode, the grid lines MUST be rendered in a discreet, subtle light-gray color that provides soft contrast against the light background (`#F8F8F8`).
- **FR-003**: In Dark Mode (Catppuccin Mocha), the grid lines MUST be rendered in a discreet dark-slate/surface color that provides soft contrast against the dark background (`#1E1E2E`).
- **FR-004**: The grid pattern MUST be composed of uniform repeating 24dp x 24dp square cells (equidistant horizontal and vertical lines).
- **FR-005**: The grid background MUST be fixed in place so that scrollable content flows over it without translating the grid itself.
- **FR-006**: The grid background MUST be applied to all primary screens across the application (Home, Search, Collection Detail, Bookmark Detail, Settings).
- **FR-007**: The grid background MUST NOT degrade scrolling or transition performance (maintaining 60+ FPS).
- **FR-008**: Opaque UI components (cards, headers with background fill, bottom sheets, dialogs, buttons) MUST properly occlude the grid pattern within their bounding shapes.

### Key Entities

- **GridBackgroundStyle**: Visual tokens defining grid line color, stroke width (1dp), and cell spacing (24dp) for each theme mode.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Fixed 24dp x 24dp grid lines are clearly visible yet subtle across 100% of primary app screens in both Light and Dark themes.
- **SC-002**: 100% of text and foreground elements maintain required contrast and readability with zero bleed-through on solid cards and sheets.
- **SC-003**: App maintains fluid 60+ FPS frame rates during continuous list scrolling over the fixed grid background.
- **SC-004**: All existing and new automated UI and unit tests pass with zero regressions.

## Assumptions

- The grid cell size is fixed at 24dp x 24dp with 1dp stroke as selected during design alignment.
- The grid background is fixed in viewport space while content scrolls over it.
- The grid is a background canvas embellishment and does not alter the layout or spacing of functional UI components.
- The grid pattern is enabled by default as the core visual canvas style for the Neobrutalist theme.
