# Technical Research & Architectural Decisions: Settings Screen

## 1. Usage Statistics Aggregation & Real-time Observation

### Decision
Calculate usage statistics in real-time by combining reactive Flows from `BookmarkRepository` and `CollectionRepository` inside `SettingsViewModel` (or via dedicated DAO queries).

### Technical Details
- **Total Bookmarks**: `bookmarkDao.getAllBookmarks().map { it.size }` (or `SELECT COUNT(*) FROM bookmarks_table`).
- **Links Added Today**: Filter bookmarks where `createdAt >= startOfDayMillis` calculated dynamically using `Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }.timeInMillis`.
- **Total Collections**: `collectionDao.getAllCollections().map { it.size }` (or `SELECT COUNT(*) FROM collections_table`).

### Rationale
- Purely reactive with Kotlin Coroutines `combine(bookmarksFlow, collectionsFlow)` ensures zero lag.
- When bookmarks are added, moved, or deleted, the Hero card counts update automatically in real-time.
- Local calculation guarantees offline accuracy and instantaneous rendering (<20ms).

### Alternatives Considered
- **One-off suspended fetch**: Only queries DB once on screen load. Rejected because it won't reflect background sync or share-sheet saves while on screen.
- **Server API polling**: Polling remote backend. Rejected because local Room is the single source of truth on device, supporting offline resiliency (Constitution Principle V).

---

## 2. Dynamic Theme Switching in Jetpack Compose

### Decision
Manage theme mode (`LIGHT`, `CATPPUCCIN_MOCHA`, `SYSTEM`) via `ThemeRepository` backed by Jetpack DataStore Preferences. `MainActivity` observes `ThemeRepository.themeMode` as state and passes the resolved dark/light flag to `NeobrutalismTheme`.

### Technical Details
- `AppThemeMode` enum: `LIGHT`, `CATPPUCCIN_MOCHA`, `SYSTEM`.
- DataStore stores string key `"app_theme_mode"`.
- `NeobrutalismTheme(darkTheme = isDark)` dynamically provides `LocalNeobrutalismColors` and `MaterialTheme`.
- In `SettingsScreen`, the user can toggle or select the theme via a Neobrutalist option group (Segmented Buttons or Modal/Dialog).

### Rationale
- Compose state updates re-render the composition smoothly without restarting `Activity`.
- High-contrast Neobrutalist tokens (solid 2.5dp borders, sharp offset drop-shadows, yellow accents) are preserved in both light and mocha palettes.

### Alternatives Considered
- **Activity Recreation (`recreate()`)**: Standard Android approach for XML themes. Rejected for Compose as it causes a noticeable screen flash and resets scroll positions.

---

## 3. Language Selection & Dynamic Localization (English & Portuguese)

### Decision
Support per-app language selection (`en` for English, `pt` for Brazilian Portuguese) stored in DataStore Preferences and integrated with Android per-app language APIs (`AppCompatDelegate.setApplicationLocales` / `LocaleManagerCompat`) or localized String resources.

### Technical Details
- Provide complete strings in `res/values/strings.xml` (default: English) and `res/values-pt-rBR/strings.xml` (Brazilian Portuguese).
- In `SettingsRepository`, store `"app_language"` (`EN`, `PT_BR`, `SYSTEM`).
- `SettingsViewModel` exposes the active language and triggers locale update on selection.
- Language selector UI provides clear options: `English` and `Português (Brasil)`.

### Rationale
- Complies with Android 13+ standard per-app language APIs while providing backwards compatibility for earlier versions.
- Clean separation of presentation texts, facilitating future translations.

### Alternatives Considered
- **In-code String Maps**: Key-value maps inside Kotlin files. Rejected because it bypasses Android linting, plurals, and standard resource qualifers.

---

## 4. Haptic Feedback Integration

### Decision
Create a reusable `HapticFeedbackHelper` (or wrap Compose `LocalHapticFeedback.current`) that checks the `isHapticEnabled` preference from DataStore before triggering tactile feedback.

### Technical Details
- Key in DataStore: `booleanPreferencesKey("haptic_feedback_enabled")` with default `true`.
- Toggling the switch in `SettingsScreen` instantly saves the preference.
- Interactive elements (buttons, segmented options, toggles) invoke haptic pulses only when enabled.

### Rationale
- Gives tactile satisfaction to Neobrutalism physical buttons while respecting user accessibility preferences.

---

## 5. Neobrutalism UI Hierarchy & Visual Layout

### Decision
Construct the Settings screen faithfully following the reference screenshot (`Screenshot_20260814_184938_Tuckii.jpg`):
- **Top App Bar**: Back navigation button (`<`) with border and shadow + "Settings" title.
- **Top Hero Card (Yellow `#FFE600`)**:
  - Top row: App icon badge + Title ("Tuckii Free" or "Bookmarks") + "FREE PLAN" badge.
  - Divider line.
  - Sub-section: "USAGE" with dual metric cards:
    - Card 1: `Links today` (e.g. `X/4` or `X added`) with progress/metric bar.
    - Card 2: `Collections` (e.g. `X/3` or `X total`) with progress/metric bar.
    - Bottom Action Button: `Upgrade for more →` (or `Storage details`).
- **PREFERENCES Section**:
  - Theme Selector Card / Radio Group.
  - Language Selector Card (`English` / `Português`).
  - Haptic Feedback Card with Switch.
- **YOUR DATA Section**:
  - Export Backup action card.
  - Restore from Backup action card.
- **IMPORT FROM OTHER APPS Section**:
  - Import Bookmarks action card.
- **ABOUT Section**:
  - App version and copyright info.
