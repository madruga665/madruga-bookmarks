# Feature Specification: Settings Screen, Theme & Language Preferences, and Usage Overview

**Feature Branch**: `005-settings-screen`

**Created**: 2026-08-14

**Status**: Draft

**Input**: User description: "tela de configurações, o usuário tera a opção de alterar o tema e selecionar o idioma, vamos manter inglê e add o português. No card amarelo do top vamos ter o uso mostrando o total de links add e o total de links add hoje, e o total de collections \n /home/madruga665/Downloads/madruga665-bookmarks/Screenshot_20260814_184938_Tuckii.jpg"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Settings Screen & Top Yellow Usage Hero Card (Priority: P1) 🎯 MVP

When a user navigates to the Settings screen (from the home navigation or drawer), they see a Neobrutalist settings layout with a top navigation bar ("Settings" title with Back navigation button) and a prominent yellow Hero summary card at the top. The hero card displays key usage metrics:
1. Total links saved across the app
2. Links added today (since 00:00:00 local time)
3. Total collections created

**Why this priority**: Immediate visual entry point that provides instant feedback on the user's bookmark activity and collection volume in accordance with the Neobrutalist design language.

**Independent Test**: Can be tested by opening the Settings screen with existing bookmarks and collections in the database, verifying the top app bar renders with the back button, and checking that the yellow hero card accurately calculates and displays total links, links added today, and total collections.

**Acceptance Scenarios**:

1. **Given** a user is on any main screen, **When** they tap the Settings entry point, **Then** the Settings screen opens with a top bar displaying a back button (`<`) and title "Settings".
2. **Given** 12 total bookmarks in the database (3 of which were created today) and 4 collections, **When** the Settings screen loads, **Then** the top yellow hero card displays "Links today: 3", "Total links: 12", and "Collections: 4".
3. **Given** the user is on the Settings screen, **When** they tap the back button (`<`), **Then** the app navigates back to the previous screen.

---

### User Story 2 - Theme Preference Selection (Priority: P1) 🎯 MVP

Users can customize the visual appearance of the application by selecting their preferred theme mode: Light, Dark, or System Default. When changed, the application immediately updates its color tokens, backgrounds, card surfaces, and high-contrast Neobrutalist borders across all screens and persists the selection across app restarts.

**Why this priority**: Essential for accessibility, personalization, and battery conservation in dark environments, maintaining Neobrutalism tokens in both light and dark modes.

**Independent Test**: Can be tested by changing the theme from Light to Dark in the Settings screen and verifying all UI surfaces update their background, card colors, text, and border styling immediately without restarting the application.

**Acceptance Scenarios**:

1. **Given** the Settings screen is open, **When** the user navigates to the Theme preference section, **Then** the currently active theme (Light, Dark, or System Default) is clearly indicated.
2. **Given** the app is in Light theme, **When** the user selects Dark theme, **Then** the entire application instantly applies the dark Neobrutalist palette and saves the preference.
3. **Given** a theme preference is saved, **When** the app is closed and reopened, **Then** the saved theme preference is restored automatically.

---

### User Story 3 - Language Selection (English & Portuguese) (Priority: P1) 🎯 MVP

Users can switch the application language between English ("English") and Brazilian Portuguese ("Português (Brasil)"). Changing the language immediately updates all interface labels, buttons, headers, and dialogs across the app to the selected locale without requiring a manual app reboot.

**Why this priority**: Expands accessibility to Portuguese-speaking users while preserving full English localization.

**Independent Test**: Can be tested by selecting "Português" in the language options, verifying all strings on the Settings screen (headers, titles, descriptions, button labels) switch to Portuguese, and confirming that English strings are restored when switching back to "English".

**Acceptance Scenarios**:

1. **Given** the language is set to English, **When** the user opens the Settings screen, **Then** labels appear in English ("Settings", "Preferences", "Theme", "Language", "Links today", "Collections").
2. **Given** the user selects "Português", **Then** the app locale updates to Brazilian Portuguese (`pt-BR`), and interface labels immediately reflect Portuguese translations ("Configurações", "Preferências", "Tema", "Idioma", "Links hoje", "Coleções").
3. **Given** the user closes and relaunches the app, **Then** the chosen language remains active.

---

### User Story 4 - Haptic Feedback & Interaction Preferences (Priority: P2)

Users can toggle haptic feedback on or off. When enabled, tactile vibration feedback is provided on key actions such as button presses, bookmark saves, and collection selections.

**Why this priority**: Enhances tactile engagement and feedback for Neobrutalist physical/tactile buttons while allowing users who prefer silent/non-vibrating interaction to disable it.

**Independent Test**: Can be tested by toggling the "Haptic Feedback" switch in Settings and verifying state persistence.

**Acceptance Scenarios**:

1. **Given** Haptic Feedback is enabled, **When** the user taps interactive buttons or cards, **Then** a short haptic pulse is triggered on supported devices.
2. **Given** Haptic Feedback is disabled, **When** the user taps interactive elements, **Then** no vibration feedback occurs.

---

### User Story 5 - Data Management & Information Cards (Priority: P3)

The Settings screen displays Neobrutalist action cards for data management and app info, including "Export Backup", "Restore Backup", "Import Bookmarks", and App Version details.

**Why this priority**: Provides clear data ownership and future extensibility for backup and import workflows.

**Independent Test**: Can be tested by verifying that data action cards render with appropriate icons, descriptions, and Neobrutalist card styling.

**Acceptance Scenarios**:

1. **Given** the user scrolls through the Settings screen, **Then** sections for "Preferences", "Your Data", and "About" are rendered with high-contrast card borders and icons.

---

### Edge Cases

- **Zero State Usage**: When a new user has 0 bookmarks and 0 collections, the hero card displays "0" for all metrics without layout distortion or errors.
- **Timezone Transition**: "Links added today" updates accurately across midnight boundary without requiring database cache invalidation.
- **Language Switch with Active Input**: If language is changed, active form fields preserve user-entered text while updating UI labels.
- **System Theme Synchronization**: When "System Default" is selected, changes to the OS dark mode automatically propagate to the app in real time.
- **Device without Vibration**: Toggling Haptic Feedback on devices without a vibrator motor fails gracefully without throwing runtime exceptions.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST provide a dedicated Settings screen accessible via top bar / navigation in the application.
- **FR-002**: System MUST render a Neobrutalist yellow hero card at the top of the Settings screen displaying real-time usage metrics:
  - Total Bookmarks count
  - Bookmarks added today count (from local 00:00:00)
  - Total Collections count
- **FR-003**: System MUST provide theme selection supporting three modes: Light, Dark, and System Default.
- **FR-004**: System MUST persist the selected theme preference locally and apply color token changes instantly across all Composables.
- **FR-005**: System MUST provide language selection supporting English (`en`) and Brazilian Portuguese (`pt-BR`).
- **FR-006**: System MUST persist the selected language preference and dynamically update all UI localized strings.
- **FR-007**: System MUST provide a toggle switch for Haptic Feedback and persist the user's preference.
- **FR-008**: System MUST provide a Top App Bar with a back navigation button (`<`) and "Settings" screen title.
- **FR-009**: System MUST support Neobrutalist styling across all Settings components (solid black borders, distinct shadow offsets, high-contrast container surfaces).

### Key Entities *(include if feature involves data)*

- **UserSettings / UserPreferences**:
  - `themeMode`: Enum (`LIGHT`, `DARK`, `SYSTEM`)
  - `language`: Enum (`EN`, `PT_BR`)
  - `hapticFeedbackEnabled`: Boolean (default: `true`)
- **UsageStatistics**:
  - `totalBookmarks`: Integer count of all saved bookmarks
  - `bookmarksToday`: Integer count of bookmarks saved on the current calendar day
  - `totalCollections`: Integer count of all existing collections

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Navigating to the Settings screen loads and displays usage statistics in under 200ms.
- **SC-002**: Changing theme or language updates the entire visible UI instantaneously (<100ms) without visual flicker or application restart.
- **SC-003**: 100% of user interface texts on the Settings screen and core navigation are fully localized in both English and Brazilian Portuguese.
- **SC-004**: Usage metrics in the yellow hero card match the exact count of items in the local database at all times.
- **SC-005**: All UI components on the Settings screen adhere to the Neobrutalism design system tokens (sharp borders, bold typography, vibrant yellow highlights).

## Assumptions

- English (`en`) is the default language if the device system locale is not Portuguese.
- "System Default" is the initial theme mode on fresh app installation.
- "Links today" is calculated using the device's local timezone starting from 00:00:00 of the current day.
- Existing bookmarks and collections in the Room database provide the data source for usage statistics.
