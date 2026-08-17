# Quickstart & Verification Guide: Search Screen with Library Statistics & Discovery

**Feature Branch**: `008-search-screen-discovery`
**Spec Reference**: [spec.md](./spec.md)

## Verification Scenarios

### Scenario 1: Open Search Screen & View Library Statistics
1. Launch the Android application.
2. From the Home screen top bar, tap the Search icon (magnifying glass).
3. **Verify**:
   - Navigation smoothly opens the Search screen (`NavRoutes.SEARCH`).
   - Top search input appears with placeholder text and a "Cancel" button.
   - "YOUR LIBRARY" Neobrutalist card renders with 4 columns (Collections, Links, Pinned, Tags).
   - The numbers in each column match the database counts (e.g. 3 Collections, 5 Links, 0 Pinned, 0 Tags).

### Scenario 2: Browse Recently Saved Bookmarks Carousel
1. On the Search screen with an empty search query, observe the "RECENTLY SAVED" section.
2. **Verify**:
   - Section has a clock icon and "RECENTLY SAVED" header.
   - Horizontal `LazyRow` allows smooth scrolling through recent bookmark cards.
   - Each card displays preview image, collection pill (e.g. "Unsorted", "IA"), title, and source platform icon/name.
3. Tap on any bookmark card in the carousel.
4. **Verify**:
   - App navigates to `NavRoutes.bookmarkDetail(bookmarkId)`.

### Scenario 3: Real-Time Instant Search Filtering
1. On the Search screen, type a query in the search bar (e.g. "Instagram" or "IA").
2. **Verify**:
   - "YOUR LIBRARY" card and "RECENTLY SAVED" carousel are hidden.
   - Vertical search results list displays all matching bookmarks within <50ms.
   - Clear (`X`) button appears inside the search text field.
3. Tap the Clear (`X`) button.
4. **Verify**:
   - Search query is cleared and Discovery mode (Library Card + Recently Saved carousel) is restored.

### Scenario 4: Empty Search Results
1. Type a nonsense query in the search bar (e.g. "xyz123nonexistent").
2. **Verify**:
   - Screen displays "No results found for 'xyz123nonexistent'".
   - Layout renders cleanly with no crashes or errors.

### Scenario 5: Cancel Search Navigation
1. On the Search screen, tap the "Cancel" button in the top bar.
2. **Verify**:
   - App navigates back to the previous screen (Home).

### Scenario 6: Dual Theme & Localization
1. Toggle between Light Mode and Catppuccin Mocha Dark Mode in Settings.
2. Switch language between English and Portuguese (`pt-BR`).
3. **Verify**:
   - Search screen displays high contrast Neobrutalist styling in Light mode and Catppuccin Mocha palette in Dark mode.
   - All strings ("YOUR LIBRARY" / "SUA BIBLIOTECA", "Cancel" / "Cancelar", etc.) render correctly in the active language.

## Automated Verification

Run unit tests:
```bash
./gradlew testDebugUnitTest --tests "com.madruga665.bookmarks.ui.search.*"
```

Build verification:
```bash
./gradlew assembleDebug
```
