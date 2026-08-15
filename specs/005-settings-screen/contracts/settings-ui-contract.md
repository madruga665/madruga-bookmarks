# Settings UI Contract & Architecture

## 1. Composable Contract

```kotlin
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onThemeSelect: (AppThemeMode) -> Unit,
    onLanguageSelect: (AppLanguage) -> Unit,
    onToggleHapticFeedback: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onExportBackupClick: () -> Unit = {},
    onRestoreBackupClick: () -> Unit = {},
    onImportBookmarksClick: () -> Unit = {}
)
```

## 2. Component Structure

- `SettingsTopBar`:
  - Back icon button with 2.5dp black border and offset shadow
  - Centered/aligned "Settings" title with bold typography
- `UsageHeroCard`:
  - Container with `NeobrutalismYellow` background, 2.5dp solid black border, 4dp shadow offset
  - Top header: App icon container (white circle with black border), App title ("Madruga Bookmarks")
  - Horizontal divider
  - 3 metric boxes:
    - `MetricBox("Total Links", "$totalBookmarks")`
    - `MetricBox("Links Today", "$bookmarksToday")`
    - `MetricBox("Collections", "$totalCollections")`
- `PreferencesSection`:
  - Header: "PREFERENCES"
  - `ThemeSelectorCard`: Displays current theme with interactive selection dialog/chips (Light, Dark, System)
  - `LanguageSelectorCard`: Displays current language with interactive selection dialog/chips (English, Português)
  - `HapticFeedbackCard`: Row with lightning icon, "Haptic Feedback" title, subtitle "Vibrate for actions and selections", and toggle switch
- `DataSection`:
  - Header: "YOUR DATA"
  - `ActionItemCard`: "Export Backup" with share icon and chevron
  - `ActionItemCard`: "Restore from Backup" with cloud icon and chevron
- `ImportSection`:
  - Header: "IMPORT FROM OTHER APPS"
  - `ActionItemCard`: "Import Bookmarks" with tray icon and chevron
