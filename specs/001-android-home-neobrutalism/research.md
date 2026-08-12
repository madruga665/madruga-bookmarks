# Research & Technical Decisions: Native Android Neobrutalism Home Screen

## Overview

This document captures technical decisions and best practices for implementing the Neobrutalism Home Screen in native Android using Kotlin and Jetpack Compose, featuring dual themes (Light from design reference, Dark from Catppuccin Mocha).

## Technical Decisions

### Decision 1: Custom Neobrutalism Styling & Shadow Modifier in Jetpack Compose

- **Decision**: Implement a custom Jetpack Compose `Modifier.neobrutalism()` and `BorderStroke` helper rather than relying on standard Material3 elevation.
- **Rationale**: Material3 `Card` elevation applies gaussian blur ambient/spot shadows (`elevation = CardDefaults.cardElevation(...)`). Neobrutalism requires crisp 2dp-3dp solid black outlines with zero-blur hard offset shadows (e.g. 4.dp X and 4.dp Y translation of a solid black background).
- **Implementation Strategy**:
  ```kotlin
  fun Modifier.neobrutalistShadow(
      shadowColor: Color = Color.Black,
      borderWidth: Dp = 2.dp,
      shadowOffset: Dp = 4.dp,
      shape: Shape = RoundedCornerShape(12.dp)
  ): Modifier = this
      .graphicsLayer { shadowElevation = 0f } // Disable standard blur
      .drawBehind {
          // Draw solid offset shadow rectangle/path
          drawOutline(
              outline = shape.createOutline(size, layoutDirection, this),
              color = shadowColor,
              style = Fill
          )
      }
      .offset(x = -shadowOffset, y = -shadowOffset) // Offset content opposite to shadow
      .border(borderWidth, shadowColor, shape)
  ```
- **Alternatives Considered**:
  - *Standard Material3 Elevation*: Rejected because gaussian blur destroys the neobrutalist aesthetic.
  - *Third-party Neobrutalism Compose Libraries*: Rejected to avoid unmaintained external dependencies and retain full control over Catppuccin Mocha color tokens.

---

### Decision 2: Tabbed Folder Card Geometry Component

- **Decision**: Build a specialized, reusable `NeobrutalistFolderCard` composable layout that combines a top tab header with a main content card body.
- **Rationale**: The reference screenshot features folder cards with a colored top tab (e.g., Yellow, Purple, Orange) extending above the top-left boundary of the white content card.
- **Layout Approach**:
  - A vertical `Column` or layered `Box` containing:
    1. A top-aligned tab `Box` with rounded top corners, solid border, hard offset shadow, and fill color.
    2. A main card `Box` positioned directly below/underneath, with solid border, hard offset shadow, containing the icon square box, link count subtext, and collection title.
- **Alternatives Considered**:
  - *Canvas Path Drawing*: Rejected because composing standard Jetpack Compose containers allows richer layout flexibility, accessibility support, and easier click handling.

---

### Decision 3: Dual Theme Mapping (Light Reference vs. Catppuccin Mocha Dark)

- **Decision**: Define explicit color schemes for Light Mode (extracted from design screenshot) and Dark Mode (mapped to official Catppuccin Mocha color palette).
- **Color Palette Mapping**:

| Token Name | Light Theme Value (Design Print) | Dark Theme Value (Catppuccin Mocha) |
|---|---|---|
| `background` | `#F8F8F8` (Off-white) | `#1E1E2E` (Mocha Base) |
| `surface` | `#FFFFFF` (Pure white) | `#313244` (Mocha Surface0) |
| `onSurface` | `#000000` (Pure black) | `#CDD6F4` (Mocha Text) |
| `subtext` | `#555555` (Dark gray) | `#A6ADC8` (Mocha Subtext) |
| `border` / `shadow` | `#000000` (Solid Black) | `#11111B` (Mocha Crust/Dark Outline) |
| `accentYellow` | `#FFD600` / `#FFC107` | `#F9E2AF` (Mocha Yellow) |
| `accentPurple` | `#7C5CFF` | `#CBA6F7` (Mocha Mauve) |
| `accentOrange` | `#FF6B00` | `#FAB387` (Mocha Peach) |
| `accentBlue` | `#3399FF` | `#89B4FA` (Mocha Blue) |

- **Rationale**: Ensures complete visual fidelity to the reference screenshot in Light Mode while providing a comfortable, modern, high-contrast dark theme using Catppuccin Mocha.

---

### Decision 4: Clipboard Auto-Paste & URL Validation

- **Decision**: Access Android `ClipboardManager` directly on user click via `LocalContext.current` and validate URL format using `Patterns.WEB_URL`.
- **Rationale**: Tapping the paste icon reads clipboard text seamlessly without requiring user long-press. Pre-parsing with standard web URL pattern provides immediate feedback before attempting a save operation.
- **Alternatives Considered**:
  - *Implicit Clipboard Access on Resume*: Rejected due to Android 10+ privacy toast notifications and privacy guidelines prohibiting background clipboard reads.

---

### Decision 5: Architecture & State Management Pattern

- **Decision**: Use ViewModel + StateFlow following Android Unidirectional Data Flow (UDF) pattern (`HomeScreenUiState`).
- **Rationale**: Decouples UI composables from data fetching/persistence. Enables straightforward testing of state transitions (Loading, Success, Empty, Error).
