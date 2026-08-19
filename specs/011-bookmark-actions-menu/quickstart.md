# Quickstart & Verification Guide: Bookmark Long-Press Actions Menu

**Feature**: `011-bookmark-actions-menu`  
**Date**: 2026-08-18

This guide provides end-to-end verification scenarios to validate the bookmark long-press actions menu across the application.

## Prerequisites
- Android Studio / Gradle environment configured
- Emulated device or physical Android device (API 26+)

## Automated Unit & UI Tests Execution

Run the complete test suite:

```bash
./gradlew testDebugUnitTest
```

---

## Manual Verification Scenarios

### Scenario 1: Long-Press Gesture & Radial Overlay Activation
1. Launch the app and open any collection with bookmarks (e.g. "IA").
2. Press and hold on any bookmark card for ~350ms.
3. **Verify**:
   - Background dims with a semi-transparent black overlay (60% alpha).
   - The active bookmark card scales slightly (`1.03x`) and tilts (`-3.5°`).
   - 4 circular floating action buttons appear in a radial arc around the card/touch point:
     - **Abrir** (External launch icon)
     - **Pinnar / Desafixar** (Pushpin icon)
     - **Compartilhar** (Share icon)
     - **Excluir** (Trash icon)

---

### Scenario 2: Continuous Drag & Hover Highlight
1. With the radial menu active, drag your finger across each of the 4 buttons.
2. **Verify**:
   - The button under your finger scales up with its distinct accent color highlight.
   - A subtle haptic vibration occurs when entering each button's boundary.
   - Moving your finger away removes the highlight smoothly.

---

### Scenario 3: Open Action Execution
1. Long-press a bookmark card with a valid URL.
2. Drag and release over the **Abrir** (Open) button.
3. **Verify**:
   - Menu overlay dismisses.
   - The device web browser or Custom Tab launches loading the URL.

---

### Scenario 4: Pin / Unpin Action Execution
1. Long-press an unpinned bookmark card in the collection grid.
2. Drag and release over the **Pinnar** button.
3. **Verify**:
   - Menu overlay dismisses.
   - The bookmark moves into the "Fixados" (Pinned) section at the top of the collection grid.
4. Long-press the pinned bookmark and release over the **Desafixar** button.
5. **Verify**:
   - The bookmark returns to the unpinned section.

---

### Scenario 5: Share Action Execution
1. Long-press a bookmark card.
2. Drag and release over the **Compartilhar** (Share) button.
3. **Verify**:
   - Android system share sheet opens with text: `"<Title> - <URL>"`.

---

### Scenario 6: Delete Action with Confirmation Dialog
1. Long-press a bookmark card.
2. Drag and release over the **Excluir** (Delete) button.
3. **Verify**:
   - Menu overlay dismisses and the Neobrutalist Delete Confirmation Dialog appears ("Excluir link?").
4. Tap **Cancelar**: Dialog closes, bookmark remains.
5. Long-press again, select **Excluir**, and tap **Excluir** in the dialog:
   - Dialog closes, bookmark is deleted from the database and disappears immediately from the grid.

---

### Scenario 7: Single Tap Navigation Integrity
1. Perform a quick tap (<300ms) on a bookmark card.
2. **Verify**:
   - App navigates directly to `BookmarkDetailScreen`.
   - The radial actions menu does NOT open.
