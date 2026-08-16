# Quickstart & Verification Guide: Collection Long-Press Actions Menu

This document describes how to execute manual and automated verification scenarios for the Collection Long-Press Actions Menu feature.

---

## Prerequisites

- Android Studio Hedgehog (2023.1.1+) or Android SDK 34 installed
- Android Emulator or physical device connected via ADB (`adb devices`)
- Repository root: `/home/madruga665/Projetos/madruga665-bookmarks/madruga665-bookmarks-app`

---

## Setup & Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests for HomeViewModel & Repository
./gradlew testDebugUnitTest
```

---

## Validation Scenarios

### Scenario 1: Long-Press Menu Activation & Backdrop Dismissal
1. Launch app on device: `./gradlew installDebug`.
2. On Home Screen ("My Collections"), locate any collection card (e.g. "IA").
3. Touch and hold the card for >500ms.
4. **Expected Outcome**:
   - Screen background dims.
   - 3 floating circular action buttons (Edit pencil, Share icon, Delete trash bin) pop up anchored around the card top-right border.
5. Tap anywhere on the dimmed background outside the buttons.
6. **Expected Outcome**: Actions menu overlay closes and screen returns to normal state.

---

### Scenario 2: Functional Collection Editing
1. Long-press a collection card ("IA") to activate the actions menu.
2. Tap the **Edit (pencil)** action button.
3. **Expected Outcome**: `EditCollectionDialog` pops up pre-filled with name "IA".
4. Modify title to "Inteligência Artificial", select accent color "PURPLE", and tap "Save".
5. **Expected Outcome**: Modal closes, Room database updates, and collection card title updates to "Inteligência Artificial" with purple tab styling.

---

### Scenario 3: Functional Collection Sharing
1. Long-press a collection card ("Vagas") to activate the actions menu.
2. Tap the **Share** action button.
3. **Expected Outcome**: Actions menu overlay closes and Android OS Share Sheet launches with text:
   `Check out my collection "Vagas" on Tuckii Bookmarks!`

---

### Scenario 4: Functional Collection Deletion
1. Long-press a test collection card (e.g. "Programação").
2. Tap the **Delete (trash bin)** action button.
3. **Expected Outcome**: `DeleteCollectionConfirmationDialog` pops up asking "Delete 'Programação' collection?".
4. Tap **Delete**.
5. **Expected Outcome**: Modal closes, collection is deleted from database, and "Programação" card is removed from grid.
