# UI Contract: `NeobrutalistArcActionsMenu`

## Composable Signature

```kotlin
@Composable
fun <T> NeobrutalistArcActionsMenu(
    items: List<ArcActionItem<T>>,
    anchorPosition: Offset,
    hoveredItemId: T?,
    onHoveredItemChange: (T?) -> Unit,
    onSelectItem: (T) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dragPosition: Offset? = null,
    radiusDp: Dp = 100.dp,
    isActive: Boolean = true
)
```

## Behavior Contract

1. **Geometry & Placement**:
   - Anchors at stationary `anchorPosition` in window space.
   - Evaluates screen dimensions via `LocalConfiguration` and `LocalDensity`.
   - Distributes `items` along calculated arc using `ArcGeometryCalculator`.
2. **Animation**:
   - On appearance (`isActive == true`): Radiates items from radius $0 \to R$ using medium bouncy spring physics (`Spring.DampingRatioMediumBouncy`, `Spring.StiffnessLow`).
   - On hover: Scaled up to $1.25\times$ with active color fill.
3. **Event Dispatching**:
   - `onHoveredItemChange`: Fired whenever `dragPosition` or pointer enters/leaves item radius.
   - `onSelectItem`: Fired when pointer is released over an item.
   - `onDismiss`: Fired when pointer is released outside all items or back button is pressed.
4. **Design System Adherence**:
   - 2.5dp/3.5dp black border on circular satellite buttons.
   - 2dp/4dp shadow offsets.
   - High-contrast text badge alongside each button.
