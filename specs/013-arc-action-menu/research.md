# Research & Technical Decisions: Touch-Anchored Arc Actions Menu

## Research Questions & Findings

### 1. Arc Mathematics & Screen Boundary Adaptability

**Context**: Satellite/Arc menus need to blossom outward from a variable user touch point $(x, y)$ in window coordinates while ensuring no satellite button or text badge clips against screen edges.

**Decision**: Implement a pure, deterministic mathematical layout engine (`ArcGeometryCalculator`) that evaluates:
1. Screen bounds $(W, H)$ in pixels (derived from `LocalConfiguration` / `LocalDensity`).
2. Touch coordinate $(x, y)$ in window pixels.
3. Satellite radius $R \approx 100\text{dp}$.
4. Item count $N$ ($N = 3$ for Collections, $N = 4$ for Bookmarks).

**Sector / Sweep Calculation Strategy**:
- Determine primary quadrant / edge proximity:
  - `isRightHalf = x > W / 2`
  - `isBottomHalf = y > H / 2`
  - `nearRight = (W - x) < (R + padding)`
  - `nearLeft = x < (R + padding)`
  - `nearBottom = (H - y) < (R + padding)`
  - `nearTop = y < (R + padding)`
- Based on proximity:
  - **Right Edge / Bottom-Right**: Arc projects into second/third quadrant (e.g. $135^\circ$ to $225^\circ$, or $120^\circ$ to $240^\circ$).
  - **Left Edge / Bottom-Left**: Arc projects into fourth/first quadrant (e.g. $-45^\circ$ to $45^\circ$ / $315^\circ$ to $45^\circ$).
  - **Bottom Edge / Center**: Arc projects upwards into upper semi-circle (e.g. $200^\circ$ to $340^\circ$).
  - **Top Edge / Center**: Arc projects downwards (e.g. $20^\circ$ to $160^\circ$).
  - **General Center**: Defaults to an ergonomic upper semi-circle or inward arc facing the screen center.
- Angle per item:
  $$\theta_i = \text{startAngle} + i \cdot \frac{\text{sweepAngle}}{N - 1}$$
- Position for item $i$:
  $$x_i = x_{\text{anchor}} + R \cdot \cos(\theta_i)$$
  $$y_i = y_{\text{anchor}} + R \cdot \sin(\theta_i)$$

**Rationale**: Pure trigonometric computation allows zero-overhead, frame-perfect placement and eliminates hardcoded static offsets.

---

### 2. Dual Interaction Flow: Drag-to-Select & Tap-to-Select

**Context**: The user wants both fluid swipe-to-select and discrete tap-to-select modes.

**Decision**:
- When long-press starts at $t_0 \ge 350\text{ms}$:
  - Active menu is triggered, recording `touchPositionInWindow`.
  - Spring animation starts expanding radius from $0 \to R$.
- Pointer Drag:
  - Pointer moves update `touchPositionInWindow`.
  - Real-time hit detection checks distance to each satellite item center $(x_i, y_i)$ with hit padding ($40\text{px}$).
  - If a button is hovered:
    - Button scales to $1.25\times$.
    - Accent color activates.
    - Haptic feedback (`HapticFeedbackType.TextHandleMove` or `LongPress`) is triggered on state transition.
- Pointer Up / Release:
  - If `hoveredOption != null`: Execute selected action immediately and dismiss menu.
  - If `hoveredOption == null`: Keep menu open on the backdrop so user can inspect badges and tap an action button directly (`clickable`).
- Tap on Backdrop: Dismisses menu.
- System Back Gesture: Dismisses menu.

**Rationale**: Accommodates both power-users who want instant single-gesture execution and casual users who want to read the options at leisure.

---

### 3. Neobrutalist Component Architecture & Separation of Concerns

**Context**: The codebase currently has `NeobrutalistFolderCard` and `NeobrutalistBookmarkCard` embedding their own inline floating action buttons. This mixes card rendering with menu overlay rendering.

**Decision**:
1. Create a generic, reusable, decoupled composable: `NeobrutalistArcActionsMenu`.
2. Extract action item specifications into a clear data contract: `ArcActionItem<T>`.
3. Update `CollectionActionsOverlay` and `BookmarkActionsOverlay` to use `NeobrutalistArcActionsMenu`.
4. Remove legacy hardcoded inline floating action buttons from `NeobrutalistFolderCard` and `NeobrutalistBookmarkCard`, allowing the card to focus strictly on card contents and visual elevation/tilt during active menu state.

**Rationale**:
- Eliminates code duplication between Collection and Bookmark overlays.
- Establishes a single source of truth for arc geometry, spring animations, hit-testing, and Neobrutalism styling.
- Highly testable with unit tests on `ArcGeometryCalculator`.

---

### 4. Animation & Performance

**Context**: Neobrutalism design language requires bouncy spring animations without UI thread frame drops.

**Decision**:
- Use Jetpack Compose `animateFloatAsState` with `spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)` for radius expansion, card scale/tilt, and hover scaling.
- Leverage `graphicsLayer(translationX, translationY, scaleX, scaleY)` to ensure hardware-accelerated drawing on the render node without triggering unnecessary recompositions of parent layouts.

**Rationale**: Guarantees consistent 60+ fps performance on mobile devices.
