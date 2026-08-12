# Quickstart Validation Guide: Native Android Neobrutalism Home Screen

## Overview

This guide describes how to run and validate the Neobrutalism Home Screen implementation in native Android (Kotlin + Jetpack Compose) for both Light Mode and Catppuccin Mocha Dark Mode.

---

## Prerequisites

- **Android Studio**: Jellyfish (2024.1.1) or newer / Ladybug.
- **JDK**: Java 17.
- **Android SDK**: API 34+ installed.
- **Gradle**: 8.4+.

---

## Validation Scenarios

### Scenario 1: Compose UI Previews (Light & Dark Theme)

1. Open `HomeScreen.kt` or `HomeScreenPreview.kt` in Android Studio.
2. Locate `@Preview` functions:
   - `@Preview(name = "Light Mode")`
   - `@Preview(name = "Catppuccin Mocha Dark Mode", uiMode = UI_MODE_NIGHT_YES)`
3. Verify in the Design Preview tab that:
   - **Light Mode**: Off-white background, black thick borders, solid black offset drop shadows, bright yellow/purple/orange folder tabs.
   - **Catppuccin Mocha Dark Mode**: `#1e1e2e` background, `#313244` surface cards, `#cdd6f4` text, Mauve/Yellow/Peach accents, solid `#11111b` offset drop shadows.

---

### Scenario 2: Quick Save & Clipboard Integration Test

1. Launch app on an Android Emulator (API 26+) or physical device.
2. Copy a URL to device clipboard (e.g. `https://kotlinlang.org`).
3. Tap the clipboard paste icon in the quick save input bar.
4. Verify the input field populates with `https://kotlinlang.org`.
5. Tap the yellow Add (`+`) button.
6. Verify link saves successfully and input field clears.

---

### Scenario 3: Automated Unit & UI Tests

Run the following test commands from project root or IDE:

```bash
# Run ViewModel & Unit Tests
./gradlew testDebugUnitTest

# Run Compose UI Component Tests
./gradlew connectedAndroidTest
```

**Expected Results**:
- All unit tests in `HomeViewModelTest` pass.
- Compose UI tests in `HomeScreenTest` verify element tags:
  - `"tag_top_bar_settings"`
  - `"tag_top_bar_manage_collections"`
  - `"tag_top_bar_search"`
  - `"tag_quick_save_input"`
  - `"tag_quick_save_paste_btn"`
  - `"tag_quick_save_add_btn"`
  - `"tag_collection_card_*"`
