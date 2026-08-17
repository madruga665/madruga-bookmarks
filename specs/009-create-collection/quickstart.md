# Quickstart & Verification Guide: Create New Collection Modal

## 1. Automated Verification

Run all unit tests across the application:

```bash
./gradlew testDebugUnitTest
```

Specifically run the `CreateCollectionViewModelTest`:

```bash
./gradlew testDebugUnitTest --tests "com.madruga665.bookmarks.ui.collection.create.CreateCollectionViewModelTest"
```

Compile and build the debug APK:

```bash
./gradlew assembleDebug
```

---

## 2. Manual Verification Scenarios

### Scenario 1: Open Modal from Home Top Bar & UI Inspection
1. Launch the app and observe the top navigation bar on the Home screen.
2. Tap the Folder icon button on the top right (`tag_top_bar_manage_collections`).
3. **Expected Result**:
   - The "Add new collection" bottom sheet slides up smoothly.
   - Header shows "Add new collection" and 'X' close button.
   - Name input has placeholder "e.g. Travel, Design Inspiration..." and character counter "0/40".
   - 16 color swatches are rendered in a clean grid; Yellow is selected with a checkmark.
   - ~43 icons are rendered in a clean 8-column grid; Folder icon is selected and highlighted in Yellow with black border and offset shadow.
   - "Create collection" button is disabled.

---

### Scenario 2: Customize Color & Icon Selection
1. Tap the Pink color swatch in the COLOR section.
2. **Expected Result**:
   - Pink swatch displays a checkmark indicator.
   - Selected Folder icon background instantly changes to Pink.
3. Tap the Gamepad icon in the ICON section.
4. **Expected Result**:
   - Gamepad icon card becomes highlighted with a Pink background, black border, and offset shadow.
   - Folder icon returns to neutral unselected surface.

---

### Scenario 3: Character Limit & Validation
1. Type a collection name (e.g. "Gaming & Emulation").
2. **Expected Result**:
   - Character counter displays `19/40`.
   - "Create collection" button becomes enabled (active Neobrutalism yellow/accent style).
3. Try typing or pasting more than 40 characters.
4. **Expected Result**:
   - Text field stops at 40 characters; counter shows `40/40`.

---

### Scenario 4: Successful Creation Flow
1. With "Gaming & Emulation", Pink color, and Gamepad icon selected, tap "Create collection".
2. **Expected Result**:
   - Modal dismisses smoothly.
   - Home screen displays the new "Gaming & Emulation" folder card with Pink accent and Gamepad icon in `MyCollectionsGrid`.
   - The new collection is persisted in Room database.

---

### Scenario 5: Dismissal
1. Open the modal, type something, then tap the 'X' button or swipe down the sheet.
2. **Expected Result**:
   - Modal dismisses without saving any new collection.

