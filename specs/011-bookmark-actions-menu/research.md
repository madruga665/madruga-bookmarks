# Research: Bookmark Long-Press Actions Menu

**Feature**: `011-bookmark-actions-menu`  
**Date**: 2026-08-18

## 1. Radial / Arc Action Menu Geometry & Screen Bounds

### Decision
Calculate the 4 action button positions dynamically along a 90°–180° radial arc anchored at the touch point (or card edge) with a radius of 65–80dp:
- **Left Column / Left Half**: Arc fans out towards the right (angles from -60° to +60° or -45° to +75° relative to the touch point).
- **Right Column / Right Half**: Arc fans out towards the left (angles from 120° to 240° or 105° to 255°).
- **Top / Bottom Edge Clamping**: Apply viewport margin clamping (padding of 16dp from screen edges) to ensure the 4 floating buttons and their labels never clip out of bounds.

### Rationale
- Radial menus provide equal, minimal gesture-drag distance from the user's thumb to any of the 4 options (~70dp).
- Dynamic arc orientation guarantees accessibility for one-handed thumb use on both left and right columns of the 2-column grid in `CollectionDetailScreen` and full-width list items in `SearchScreen`.

### Alternatives Considered
- *Linear vertical column of 4 buttons*: Requires long vertical dragging (>180dp travel distance) and can easily overflow card height or screen top/bottom.
- *2x2 Grid*: More crowded, difficult for continuous swipe-and-release thumb gestures compared to a curved radial arc.

---

## 2. Gesture Handling & Touch Drag Tracking Pipeline

### Decision
Implement the low-level pointer input pipeline using Compose `awaitEachGesture` inside `pointerInput(bookmark.id)`:
1. `awaitFirstDown(requireUnconsumed = false)` captures touch down timestamp and initial coordinates.
2. If held past 350ms without movement past touch slop, trigger `onLongPressStart(bookmark, touchInWindow, cardOffset, cardSize)` and trigger a strong haptic feedback (`HapticFeedbackType.LongPress`).
3. While held, continuously process `awaitPointerEvent()` changes to compute Euclidean distance to the 4 button centers:
   $$\text{distance} = \sqrt{(x - x_{\text{btn}})^2 + (y - y_{\text{btn}})^2}$$
4. If distance $\le \text{hitRadius} \approx 36\text{dp}$, activate `BookmarkOption` hover state, trigger light tick haptic (`HapticFeedbackType.TextHandleMove`), and scale up the hovered button.
5. On pointer release:
   - If a `BookmarkOption` is currently hovered, invoke `onOptionSelected(bookmark, option)` and dismiss overlay.
   - If no option is hovered, dismiss overlay with no action.
   - If released before 350ms, trigger `onClick()` (navigate to `BookmarkDetailScreen`).

### Rationale
- Reuses the proven gesture architecture already established in `NeobrutalistFolderCard.kt`.
- Provides fluid "touch-hold-drag-release" single gesture execution or "touch-hold-lift-tap" discrete button selection.

---

## 3. Visual Styling & Neobrutalism Tokens

### Decision
Render the 4 radial action buttons with distinct Neobrutalist styling:
- **Abrir (Open)**: Icon `Icons.Outlined.OpenInBrowser` / `Icons.Outlined.OpenInNew`, Label "Abrir", Active Color `NeobrutalismTheme.colors.accentGreen` (`#4ADE80` or `#A3E635`), Normal Color `NeobrutalismTheme.colors.surface`.
- **Pinnar / Desafixar (Pin / Unpin)**: Icon `Icons.Outlined.PushPin` (or `Icons.Filled.PushPin` when pinned), Label dynamic "Pinnar" / "Desafixar", Active Color `NeobrutalismTheme.colors.accentYellow` (`#FFE169`), Normal Color `NeobrutalismTheme.colors.surface`.
- **Compartilhar (Share)**: Icon `Icons.Outlined.Share`, Label "Compartilhar", Active Color `NeobrutalismTheme.colors.accentBlue` (`#60A5FA`), Normal Color `NeobrutalismTheme.colors.surface`.
- **Excluir (Delete)**: Icon `Icons.Outlined.Delete`, Label "Excluir", Active Color `Color(0xFFFF4B4B)` / `NeobrutalismTheme.colors.accentOrange`, Normal Color `NeobrutalismTheme.colors.surface`.
- **Borders & Shadows**: 2.5dp solid black borders (`#000000`), 3dp neobrutalist offset shadows, bold black typography.

### Rationale
- Matches the high-contrast aesthetic of the collection action buttons and the project's Neobrutalism design constitution.

---

## 4. State Management & Layering Strategy

### Decision
- **Composable Overlay**: Create `BookmarkActionsOverlay.kt` living at the root of `CollectionDetailScreen.kt`, `SearchScreen.kt`, etc.
- **Overlay State**: Encapsulated in a transient state object or ViewModel fields:
  ```kotlin
  data class BookmarkActionsOverlayState(
      val activeBookmark: BookmarkEntity? = null,
      val cardOffset: Offset? = null,
      val cardSize: IntSize? = null,
      val touchPositionInWindow: Offset? = null,
      val hoveredOption: BookmarkOption? = null,
      val bookmarkToDelete: BookmarkEntity? = null
  )
  ```
- **Reactivity**: Pinning updates Room via `BookmarkRepository.togglePin(id)`, which immediately emits the updated bookmark list to existing `StateFlow` queries.
