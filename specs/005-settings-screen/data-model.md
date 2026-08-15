# Data Model: Settings, Preferences & Usage Statistics

## 1. User Preferences Entity / DataStore Schema

Stored in Jetpack DataStore Preferences (`"settings_prefs"` or `"theme_prefs"`).

### Keys & Types

| Key | Data Type | Default Value | Description |
|-----|-----------|---------------|-------------|
| `app_theme_mode` | `String` | `"SYSTEM"` | Theme choice: `"LIGHT"`, `"CATPPUCCIN_MOCHA"`, `"SYSTEM"` |
| `app_language` | `String` | `"SYSTEM"` | Language choice: `"EN"`, `"PT_BR"`, `"SYSTEM"` |
| `haptic_feedback_enabled` | `Boolean` | `true` | Enables or disables tactile vibration on button taps |

### Enums

```kotlin
enum class AppThemeMode {
    LIGHT,             // High-contrast Neobrutalism Light
    CATPPUCCIN_MOCHA,  // Catppuccin Mocha Dark
    SYSTEM             // Follows Android OS Dark Theme setting
}

enum class AppLanguage(val code: String, val displayName: String) {
    SYSTEM("system", "System Default"),
    EN("en", "English"),
    PT_BR("pt-BR", "Português (Brasil)")
}
```

---

## 2. Usage Statistics Model

Aggregated data model used by the UI layer to render the top yellow Hero Card.

```kotlin
data class UsageStatistics(
    val totalBookmarks: Int = 0,
    val bookmarksToday: Int = 0,
    val totalCollections: Int = 0,
    val dailyLimit: Int = 4,       // Reference limit for visual indicator if applicable
    val collectionLimit: Int = 3    // Reference limit for visual indicator if applicable
)
```

### Validation & Computed Properties
- `totalBookmarks`: Must be `>= 0`.
- `bookmarksToday`: Calculated dynamically from local database bookmarks where `createdAt >= midnightEpochMillis`. Must be `<= totalBookmarks`.
- `totalCollections`: Count of existing collections in `collections_table`. Must be `>= 0`.

---

## 3. UI State Models

### `SettingsUiState`

```kotlin
data class SettingsUiState(
    val isLoading: Boolean = false,
    val usageStatistics: UsageStatistics = UsageStatistics(),
    val currentTheme: AppThemeMode = AppThemeMode.SYSTEM,
    val currentLanguage: AppLanguage = AppLanguage.SYSTEM,
    val isHapticFeedbackEnabled: Boolean = true,
    val appVersion: String = "1.0.0",
    val errorMessage: String? = null
)
```

---

## 4. UI Events / User Actions

```kotlin
sealed interface SettingsEvent {
    data class SetTheme(val themeMode: AppThemeMode) : SettingsEvent
    data class SetLanguage(val language: AppLanguage) : SettingsEvent
    data class ToggleHapticFeedback(val enabled: Boolean) : SettingsEvent
    object ExportBackup : SettingsEvent
    object RestoreBackup : SettingsEvent
    object ImportBookmarks : SettingsEvent
}
```
