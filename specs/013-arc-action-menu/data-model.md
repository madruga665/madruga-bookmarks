# Data Model & Geometry Entities: Touch-Anchored Arc Actions Menu

## Entities & Value Objects

### 1. `ArcActionItem<T>`

Represents an individual satellite action item displayed on the radial arc.

```kotlin
data class ArcActionItem<T>(
    val id: T,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String,
    val activeColor: Color,
    val onClick: () -> Unit
)
```

- **Fields**:
  - `id: T`: Unique identifier or enum value for the action (e.g. `CollectionOption.EDIT`, `BookmarkOption.OPEN`).
  - `icon: ImageVector`: Material or custom Vector icon rendered inside the satellite circle.
  - `label: String`: Human-readable localized text displayed on the persistent badge.
  - `contentDescription: String`: Accessibility description.
  - `activeColor: Color`: High-contrast Neobrutalist background color applied when hovered or active.
  - `onClick: () -> Unit`: Callback triggered when this action is tapped or released upon.

---

### 2. `ArcItemPosition`

Represents calculated Cartesian offset and badge alignment for an item along the arc.

```kotlin
data class ArcItemPosition(
    val offset: Offset,       // (x, y) offset in pixels relative to anchor
    val angleDegrees: Float,  // Angle in degrees from positive X axis
    val badgeOnLeft: Boolean  // True if label badge should render on the left of the button
)
```

- **Fields**:
  - `offset: Offset`: Computed translation vector $(dx, dy)$ from the anchor center.
  - `angleDegrees: Float`: Polar angle in degrees for diagnostics/animation.
  - `badgeOnLeft: Boolean`: True if the satellite button is in the right half of the arc (to position text on left without clipping).

---

### 3. `ArcGeometryConfig`

Immutable configuration holding geometric parameters for arc calculation.

```kotlin
data class ArcGeometryConfig(
    val radiusPx: Float,
    val startAngleDegrees: Float,
    val sweepAngleDegrees: Float,
    val screenWidthPx: Float,
    val screenHeightPx: Float,
    val hitPaddingPx: Float = 40f
)
```

---

### 4. `ArcGeometryCalculator` (Domain / Logic Engine)

Pure Kotlin utility to calculate positions for $N$ items given touch anchor and screen bounds:

```kotlin
object ArcGeometryCalculator {
    fun calculateSector(
        anchor: Offset,
        screenWidth: Float,
        screenHeight: Float,
        radius: Float
    ): Pair<Float, Float> // (startAngleDegrees, sweepAngleDegrees)

    fun calculateItemPositions(
        anchor: Offset,
        itemCount: Int,
        radius: Float,
        startAngle: Float,
        sweepAngle: Float
    ): List<ArcItemPosition>

    fun findHoveredItemIndex(
        touchPosition: Offset,
        anchor: Offset,
        itemPositions: List<ArcItemPosition>,
        buttonRadius: Float,
        hitPadding: Float = 40f
    ): Int?
}
```

---

## State Model

### Overlay State Integration (reused in ViewModels)

- **Collection Overlay State**:
  - `activeMenuCollection: CollectionEntity?`
  - `activeCardOffset: Offset?`
  - `activeCardSize: IntSize?`
  - `touchPositionInWindow: Offset?`
  - `hoveredOption: CollectionOption?`

- **Bookmark Overlay State**:
  - `activeMenuBookmark: BookmarkEntity?`
  - `activeCardOffset: Offset?`
  - `activeCardSize: IntSize?`
  - `touchPositionInWindow: Offset?`
  - `hoveredOption: BookmarkOption?`
