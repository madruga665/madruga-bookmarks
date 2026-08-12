# Quickstart Validation Guide: Add Bookmark Bottom Sheet Modal

## Overview

This guide describes how to run and validate the Add Bookmark Bottom Sheet Modal in native Android (Kotlin + Jetpack Compose) in Light Mode and Catppuccin Mocha Dark Mode.

---

## Validation Scenarios

### Scenario 1: Compose UI Previews (Light & Catppuccin Mocha Dark Mode)

1. Open `SaveBookmarkBottomSheetPreview.kt` in Android Studio.
2. Verify Preview displays:
   - Rounded top corners bottom sheet with grabber handle.
   - Header with URL subtitle and top-right "New Folder" neobrutalist button.
   - "Pin this link" pushpin toggle row.
   - Scrollable folder collection list with "Unsorted" highlighted in yellow fill with checkmark icon.
   - Bottom action button reading `Save to "Unsorted"`.

---

### Scenario 2: Selecting Folder & Dynamic Button Text

1. Launch app on Android Emulator or physical device.
2. Enter a URL in the quick save input (e.g. `https://github.com/catppuccin/nvim`) and tap Add (`+`).
3. Verify the bottom sheet modal opens.
4. Tap the "Vagas" collection card.
5. Verify "Vagas" highlights in yellow with a checkmark and the main button label changes to `Save to "Vagas"`.
6. Tap `Save to "Vagas"`.
7. Verify the modal dismisses and bookmark is saved in the "Vagas" folder.

---

### Scenario 3: Inline Creating a New Folder

1. Open the save modal for a URL.
2. Tap the top-right "New Folder" icon button.
3. Type `"Design"` and tap "Create Folder".
4. Verify `"Design"` is added to the folder list, highlighted, and auto-selected for saving.

---

### Scenario 4: Automated Unit & UI Tests

Run tests via Gradle:

```bash
./gradlew testDebugUnitTest
./gradlew connectedAndroidTest
```
