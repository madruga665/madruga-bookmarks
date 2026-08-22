# Quickstart & Verification Guide: Subtle Neobrutalist Grid Background Pattern

**Feature**: `012-grid-background`  
**Date**: 2026-08-22

---

## 1. Prerequisites

- Android SDK 35 installed
- JDK 17+ configured (`JAVA_HOME=/opt/android-studio/jbr` or standard JDK 17)
- Working directory: `/home/madruga665/Projetos/madruga665-bookmarks/madruga665-bookmarks-app`

---

## 2. Automated Test Execution

Run all automated unit and UI tests to ensure zero regressions:

```bash
JAVA_HOME=/opt/android-studio/jbr ./gradlew test
```

Run static analysis and lint checks:

```bash
JAVA_HOME=/opt/android-studio/jbr ./gradlew check
```

---

## 3. End-to-End Visual Verification Scenarios

### Scenario 1: Light Mode Grid Background on Home Screen
1. Launch the app in Light Mode (default).
2. Observe the Home screen canvas background.
3. **Expected**: Subtle, light-gray (`#E2E2E2`) 24dp x 24dp grid lines are visible in the background margins and gutters. Solid cards (My Collections, Quick Save bar, Hero text) sit cleanly on top without grid bleed-through.

### Scenario 2: Scrolling Fixed Canvas Behavior
1. On the Home screen or Collection Detail screen with multiple items, scroll down.
2. **Expected**: The collection cards and bookmark items scroll smoothly over the grid. The grid pattern remains fixed to the screen viewport with zero stutter or lag (60/120 FPS).

### Scenario 3: Dark Mode (Catppuccin Mocha) Adaptive Grid
1. Navigate to **Settings** -> **Theme** -> Select **Dark Mode** (Catppuccin Mocha).
2. Observe the background across Settings, Home, and Search screens.
3. **Expected**: The grid background displays subtle dark-slate (`#28283D`) lines over `#1E1E2E` Mocha Base background, providing low-glare, aesthetic texture.

### Scenario 4: Search Screen Discovery and Results
1. Navigate to **Search** screen.
2. In empty query state, verify the "YOUR LIBRARY" card and "RECENTLY SAVED" carousel sit cleanly on top of the fixed grid background.
3. Type a query: search results list renders with opaque cards over the grid background.
