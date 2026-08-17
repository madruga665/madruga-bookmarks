# Quickstart & Verification Guide: Bookmark Tagging System

**Feature Branch**: `010-bookmark-tags` | **Date**: 2026-08-16 | **Spec**: [spec.md](./spec.md)

## Automated Verification

Run all unit tests:
```bash
./gradlew testDebugUnitTest --tests "com.madruga665.bookmarks.ui.search.SearchViewModelTest"
./gradlew testDebugUnitTest --tests "com.madruga665.bookmarks.ui.savemodal.SaveBookmarkViewModelTest"
./gradlew testDebugUnitTest --tests "com.madruga665.bookmarks.ui.bookmark.BookmarkDetailViewModelTest"
./gradlew testDebugUnitTest --tests "com.madruga665.bookmarks.ui.utils.TagPaletteTest"
./gradlew test
```

Run build & lint checks:
```bash
./gradlew assembleDebug check
```

---

## Manual Verification Scenarios

### Scenario 1: Add Tags during Bookmark Save
1. Open the app and enter a link into the quick-save bar or share via Intent.
2. In the `SaveBookmarkBottomSheet`, locate the **TAGS** section.
3. Type `android` and press Enter / comma / tap "Add". Verify a `#android` chip is created.
4. Type `compose` and tap "Add". Verify `#compose` chip is created.
5. Tap the 'X' on `#android`. Verify it is removed.
6. Tap Save. Open the collection and verify `#compose` badge is visible on the bookmark card.

### Scenario 2: Tag Filtering on Search Screen
1. Navigate to the Search Screen.
2. Observe the horizontal row of active tags below the search bar.
3. Tap the `#compose` tag chip.
4. Verify that only bookmarks with the `#compose` tag are listed in under 100ms.
5. Type a search query (e.g. `guide`) while `#compose` is selected. Verify results match both criteria.
6. Tap `#compose` again to clear filter.

### Scenario 3: View & Edit Tags on Bookmark Detail Screen
1. Tap a bookmark to open `BookmarkDetailScreen`.
2. Scroll to the Tags section.
3. Add a new tag `architecture` and verify it persists.
4. Remove a tag and return to collection list; verify updated badge rendering.
