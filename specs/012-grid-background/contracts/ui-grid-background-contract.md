# UI Contract: Neobrutalist Grid Background Modifier

**Feature**: `012-grid-background`  
**Date**: 2026-08-22

---

## 1. Modifier API Specification

Package: `com.madruga665.bookmarks.ui.theme`

```kotlin
/**
 * Custom Neobrutalism modifier applying a subtle repeating square grid pattern.
 *
 * @param gridColor The color of the grid lines (typically [NeobrutalismColors.gridLine]).
 * @param gridSize The spacing between grid lines in density-independent pixels. Default is 24.dp.
 * @param strokeWidth The width of the grid line strokes. Default is 1.dp.
 */
fun Modifier.neobrutalistGridBackground(
    gridColor: Color,
    gridSize: Dp = 24.dp,
    strokeWidth: Dp = 1.dp
): Modifier
```

---

## 2. Behavioral Rules & Constraints

1. **Draw Phase Execution**: The grid lines MUST be drawn during the draw pass using `Modifier.drawBehind` so child composables naturally render on top.
2. **Viewport Boundary Clipping**: Grid lines MUST only be drawn within the measured bounds (`size.width` and `size.height`) of the layout node to which the modifier is attached.
3. **Fixed Background Anchor**: When attached to the outermost root container (`Box` / `Scaffold`), the grid origin `(0, 0)` is anchored to the screen viewport and does not scroll when inner child elements scroll.
4. **Opacity Preservation**: Containers intended to show content with solid cards (e.g. `NeobrutalistBookmarkCard`, `NeobrutalistFolderCard`, `YourLibraryCard`, `BasicTextField`, Dialogs, BottomSheets) MUST use solid surface backgrounds (`NeobrutalismTheme.colors.surface`) so that grid lines are cleanly obscured beneath them.
5. **No Layout Mutation**: The modifier MUST NOT alter the intrinsic measurement, layout constraints, or padding of the composable it attaches to.

---

## 3. Screen Integration Contract

| Screen | Target Container | Modifier Chain |
|---|---|---|
| `HomeScreen` | Root `Box` | `modifier.fillMaxSize().background(NeobrutalismTheme.colors.background).neobrutalistGridBackground(NeobrutalismTheme.colors.gridLine)` |
| `SearchScreen` | Root `Box` | `modifier.fillMaxSize().background(NeobrutalismTheme.colors.background).neobrutalistGridBackground(NeobrutalismTheme.colors.gridLine).statusBarsPadding()` |
| `SettingsScreen` | Root `Box` | `modifier.fillMaxSize().background(NeobrutalismTheme.colors.background).neobrutalistGridBackground(NeobrutalismTheme.colors.gridLine)` |
| `CollectionDetailScreen` | Root `Box` | `modifier.fillMaxSize().background(NeobrutalismTheme.colors.background).neobrutalistGridBackground(NeobrutalismTheme.colors.gridLine)` |
| `BookmarkDetailScreen` | Root `Box` / `Column` | `modifier.fillMaxSize().background(NeobrutalismTheme.colors.background).neobrutalistGridBackground(NeobrutalismTheme.colors.gridLine)` |
