# Quickstart & Verification Guide: Settings Screen

This guide outlines end-to-end steps to verify the Settings screen, theme switching, language localization, and usage statistics.

## Prerequisites
- Android device or emulator running API 26+
- Project build passing: `./gradlew test`

---

## Verification Scenarios

### Scenario 1: Navigate to Settings & Validate Top Usage Hero Card
1. Launch the application.
2. From the Home screen top bar, tap the Settings icon.
3. **Expected Result**:
   - The app navigates to `NavRoutes.SETTINGS`.
   - The Top App Bar displays `<` Back button and "Settings" title.
   - The Yellow Hero card renders at the top with:
     - App icon + "Tuckii Free" (or "Bookmarks") title and "FREE PLAN" badge.
     - "Links today": shows current count of links added today (e.g., `0/4` or actual count).
     - "Collections": shows count of total collections (e.g., `2/3` or actual count).
     - "Upgrade for more →" button.

### Scenario 2: Switch Theme Mode
1. On the Settings screen, scroll to the "PREFERENCES" section.
2. Tap "Theme".
3. Select "Dark" (Catppuccin Mocha).
4. **Expected Result**:
   - The screen immediately updates to dark background (`#1E1E2E`) with Mocha surface colors, vibrant accents, and high-contrast borders.
   - Tap `<` to return to the Home screen and verify the dark theme is active across the entire app.
   - Close and reopen the app: theme remains Dark.
5. In Settings, select "Light":
   - The app instantly switches back to the Light Neobrutalism palette (`#F4F0EA`).

### Scenario 3: Switch Language (English / Portuguese)
1. On the Settings screen, tap "Language".
2. Select "Português (Brasil)".
3. **Expected Result**:
   - The UI texts update to Portuguese ("Configurações", "Preferências", "Tema", "Idioma", "Feedback Tátil", "Links hoje", "Coleções").
   - Navigate back to Home and verify collection titles/labels and action texts update appropriately.
4. Return to Settings, select "English":
   - The UI texts revert immediately to English.

### Scenario 4: Toggle Haptic Feedback
1. On the Settings screen, toggle the "Haptic Feedback" switch.
2. **Expected Result**:
   - Switch state flips smoothly.
   - Preference is persisted in DataStore.
   - When enabled, button taps trigger tactile feedback.

---

## Automated Test Execution

```bash
./gradlew testDebugUnitTest
```
