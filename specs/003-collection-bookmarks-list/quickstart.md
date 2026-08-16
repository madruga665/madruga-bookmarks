# Quickstart & Manual Validation Guide: Collection Bookmarks List

## Prerequisites
- Android Studio / Android SDK with Gradle wrapper working.
- Android Emulator or physical device connected (`adb devices`).

## Build & Test Commands

### 1. Execute Unit Tests
Run unit tests for Room DAOs, Repositories, and Collection Detail ViewModel:

```bash
./gradlew testDebugUnitTest
```

### 2. Launch App on Emulator/Device
Build and install debug APK:

```bash
./gradlew installDebug
```

---

## Validation Scenarios

### Scenario 1: Navigate to Collection Details & Verify Header
1. Launch app on device/emulator.
2. Tap on the **Vagas** collection folder card from the Home screen.
3. **Verify**:
   - Navigation opens `folder_detail/col_vagas`.
   - Header title displays **Vagas**.
   - Subtitle displays link and subcollection count (e.g. `2 links · 0 subcollections`).
   - Top-left back button is visible and active.
   - Top-right quick add link button (yellow accent with link icon) is visible.

### Scenario 2: Verify 2-Column Neobrutalist Grid Layout
1. In the **Vagas** collection screen, locate the **ALL LINKS (2)** section header.
2. **Verify**:
   - Bookmarks render in a 2-column vertical grid.
   - Each card displays solid black borders (2.5dp stroke), rounded corners (16dp), and shadow depth.
   - Top container shows image thumbnail or geometric fallback pattern.
   - Bookmark title is displayed cleanly with max 3 line truncation.
   - Bottom metadata displays platform icon and tag (e.g. `@LinkedIn`).

### Scenario 3: Quick Add Bookmark Pre-Selection
1. Inside the **Vagas** collection screen, tap the yellow quick add button in the top right header.
2. **Verify**:
   - The Save Bookmark Bottom Sheet Modal slides up.
   - Destination folder is automatically selected as **Vagas** with yellow container fill and checkmark icon.
   - Bottom save button reads `Save to "Vagas"`.

### Scenario 4: Navigate Back to Home
1. Tap the top-left back button.
2. **Verify**:
   - View returns smoothly to the main Home screen.
