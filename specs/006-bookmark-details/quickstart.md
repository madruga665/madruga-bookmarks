# Quickstart & Verification Guide: Bookmark Details View

**Feature**: `006-bookmark-details`
**Date**: 2026-08-15

This guide provides runnable instructions to verify the Bookmark Details View feature locally.

---

## 1. Prerequisites

- Android Studio / Android SDK installed
- Local environment with Gradle and Java 17

---

## 2. Automated Test Verification

Run all unit tests from the `android/` directory:

```bash
cd android
./gradlew test
```

### Specific Unit Tests for Feature 006

```bash
# Run Bookmark Detail ViewModel tests
./gradlew testDebugUnitTest --tests "com.madruga665.bookmarks.ui.bookmark.*"

# Run Repository & Entity tests
./gradlew testDebugUnitTest --tests "com.madruga665.bookmarks.data.*"
```

---

## 3. Manual UI Flow Validation

1. **Launch App**: Build and install debug APK on emulator or connected device:
   ```bash
   cd android
   ./gradlew installDebug
   ```
2. **Open Collection**: Tap on a collection card (e.g. "IA" or "Vagas").
3. **Open Bookmark Details**: Tap on any bookmark card in the list:
   - Verify navigation to `BookmarkDetailScreen`.
   - Verify top bar with platform tag, Reload, Share, Move, and Delete buttons.
   - Verify hero preview image with top-right Pin button.
   - Verify Title with pencil edit button.
   - Verify Description with "Show more / Show less" toggle.
   - Verify yellow Neobrutalist URL card.
   - Verify TAGS section with chips and "+ Add" button.
   - Verify NOTES section with editable text area.
4. **Test Pinning**:
   - Tap the Pin button on the hero image; navigate back to collection and verify the bookmark is in the "PINNED" section.
5. **Test Inline Title Edit**:
   - Tap the edit pencil icon beside the title; change title text, tap "Salvar", and verify the new title persists.
6. **Test Notes & Tags**:
   - Add notes in the NOTES field and tap "Salvar".
   - Tap "+ Add" in TAGS to create a tag; tap 'X' on a tag chip to remove it.
7. **Test Move Collection**:
   - Tap the Move icon in the top bar; select another collection from the bottom sheet.
8. **Test Delete**:
   - Tap the red trash bin icon in the top bar; confirm deletion in the dialog; verify navigation pops back and item is removed from the collection.
9. **Test External URL Opening**:
   - Tap the yellow URL card and verify the device's web browser opens with the target URL.
