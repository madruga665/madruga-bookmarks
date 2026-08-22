# Quickstart & Verification Guide: Touch-Anchored Arc Actions Menu

## Automated Test Verification

Run all unit tests across the project to ensure mathematical calculations, reducers, and ViewModels operate with zero regressions:

```bash
./gradlew testDebugUnitTest
```

### Specific Unit Tests Added / Verified
1. `ArcGeometryCalculatorTest`:
   - Validates sector calculation for right edge, left edge, top edge, bottom edge, and corners.
   - Validates item distribution angles for $N=3$ (Collections) and $N=4$ (Bookmarks).
   - Validates hit testing logic under various drag coordinates.
2. `HomeViewModelTest`:
   - Validates long-press drag and release flows for collection actions.
3. `CollectionDetailViewModelTest`:
   - Validates long-press drag and release flows for bookmark actions.
4. `SearchViewModelTest`:
   - Validates long-press drag and release flows for bookmark actions in search results.

---

## Manual Verification Scenarios

### Scenario 1: Collection Card Touch-Anchored Arc Menu (Home Screen)
1. Open the app on the Home screen with at least one collection card visible.
2. Press and hold near the bottom-right corner of a collection card.
3. **Verify**:
   - The screen backdrop dims.
   - The 3 collection satellite buttons (Edit, Share, Delete) blossom outward along an arc opening towards top-left / center of screen.
   - Each button displays its label badge next to it.
4. Drag your finger across the Edit, Share, and Delete buttons:
   - **Verify**: Hovered button scales up, displays accent color, and triggers haptic tick.
5. Release your finger over "Edit":
   - **Verify**: The Edit Collection dialog opens immediately.

---

### Scenario 2: Bookmark Card Touch-Anchored Arc Menu (Collection Detail Screen)
1. Navigate into any Collection containing bookmarks.
2. Press and hold on a bookmark card.
3. **Verify**:
   - The 4 bookmark satellite buttons (Open, Pin/Unpin, Share, Delete) blossom outward from the exact touch contact point.
   - All 4 buttons remain fully visible on screen.
4. Release your finger outside any action:
   - **Verify**: The arc menu remains visible and open.
5. Tap directly on "Open":
   - **Verify**: External browser launches opening the bookmark link.

---

### Scenario 3: Screen Edge & Corner Boundary Adaptability
1. Long-press a card near the extreme right edge of the screen:
   - **Verify**: The arc points inward towards the left without clipping.
2. Long-press a card near the extreme top of the list:
   - **Verify**: The arc points downward without clipping under the top bar.
3. Long-press a card near the extreme bottom:
   - **Verify**: The arc points upward without clipping below the bottom navigation / screen edge.
