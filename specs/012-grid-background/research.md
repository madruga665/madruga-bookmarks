# Research & Technical Decisions: Subtle Neobrutalist Grid Background Pattern

**Feature**: `012-grid-background`  
**Date**: 2026-08-22

---

## 1. Background Pattern Rendering in Jetpack Compose

### Decision
Implement the grid pattern using a custom Compose Modifier extension function `Modifier.neobrutalistGridBackground(...)` leveraging `drawBehind { ... }`.

### Rationale
- `drawBehind` executes directly inside Compose's `DrawScope` on the GPU render pass, avoiding intermediate view hierarchies or extra composables.
- Zero allocations during rendering (reuses `DrawScope.drawLine` with primitive floats `x` and `y` offsets).
- Hardware-accelerated and capable of 60/120 FPS rendering on all supported Android versions (API 26+).
- Cleanly composes with other modifiers like `.fillMaxSize()`, `.statusBarsPadding()`, and `.background(...)`.

### Alternatives Considered
- **Tiled Vector/Bitmap Drawable (`BitmapShader` / XML background)**: Rejected because bitmap tiling requires raster scaling, risks artifacting across multiple DPI densities (mdpi to xxxhdpi), and does not dynamically adapt to Compose theme switches as cleanly as vector DrawScope lines.
- **Dedicated Composable Layout (`Canvas { ... }`)**: Rejected because wrapping entire screen content in a custom layout adds unnecessary layout hierarchy depth compared to a straightforward `Modifier.drawBehind`.

---

## 2. Fixed Viewport Background vs. Content Scroll Layering

### Decision
Apply `Modifier.neobrutalistGridBackground(...)` to root viewport containers (e.g. root `Box` or `Scaffold` of each screen) so the grid remains fixed in place while content cards, headers, and scrollable columns flow smoothly over it.

### Rationale
- Matches the user's aligned design decision for a fixed drafting-mat / notebook canvas backdrop.
- Prevents visual fatigue and motion nausea caused by dense grid lines translating rapidly across the screen during fast flings/scrolling.
- Content elements with solid surfaces (`NeobrutalismTheme.colors.surface`, `LightSurface`, `MochaSurface0`) naturally occlude the grid lines, maintaining perfect text legibility and card boundaries.

### Alternatives Considered
- **Translating / Scrolling Grid Pattern**: Applying the modifier directly to scrolling containers (`verticalScroll`, `LazyColumn`). Rejected during alignment interview to prevent visual noise.

---

## 3. Theme Color Tokens & Contrast Ratios

### Decision
Extend `NeobrutalismColors` in `Color.kt` with a `gridLine: Color` token:
- **Light Theme**: `LightGridLine = Color(0xFFE2E2E2)` / `Color(0xFFE5E5E5)` against `LightBackground = Color(0xFFF8F8F8)`.
  - Contrast ratio is soft and subtle (~1.15:1 against background), ensuring it acts as a discreet texture rather than high-contrast structural borders (which remain 2.5dp solid black `Color(0xFF000000)`).
- **Dark Theme (Catppuccin Mocha)**: `MochaGridLine = Color(0xFF28283D)` against `MochaBase = Color(0xFF1E1E2E)`.
  - Harmonizes with the Catppuccin Mocha palette, providing a non-glare, dark-slate blueprint texture.

### Rationale
- Centralized in `NeobrutalismColors` and `Theme.kt`, ensuring full support for dynamic light/dark theme switching and future theme extensions.
- Meets WCAG readability principles by keeping background texture low-contrast while foreground text and borders maintain maximum contrast (>7:1).

---

## 4. Screen-Level Integration & Scope

### Decision
Apply the grid pattern to all primary screens in `app/src/main/java/com/madruga665/bookmarks/ui/`:
1. `HomeScreen.kt`
2. `SearchScreen.kt`
3. `CollectionDetailScreen.kt`
4. `BookmarkDetailScreen.kt`
5. `SettingsScreen.kt`

### Rationale
- Provides a cohesive, unified visual identity across the entire application navigation graph without inconsistent blank screens.

---

## 5. Automated Verification & Testing Strategy

### Decision
- **Unit & Theme Tests**: Validate `NeobrutalismTheme` and `NeobrutalismColors` instances correctly supply `gridLine` colors for both light and dark palettes.
- **Compose UI Tests**: Verify screens and components render with `gridBackground` without throwing layout/draw exceptions.
- **Build & Regression Verification**: Ensure `./gradlew test` and `./gradlew check` pass with zero regressions.
