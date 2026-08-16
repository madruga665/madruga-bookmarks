# Technical Research & Architectural Decisions: App Localization (i18n)

**Feature**: `007-app-localization`
**Date**: 2026-08-15

## 1. Dynamic Runtime Locale Switching in Jetpack Compose

- **Decision**: Use a combination of AndroidX `AppCompatDelegate.setApplicationLocales` and Compose `CompositionLocalProvider(LocalConfiguration provides updatedConfig, LocalContext provides localizedContext)`.
- **Rationale**:
  - `AppCompatDelegate.setApplicationLocales` (App-Specific Language API) manages system-level and activity-level locale configuration seamlessly across Android versions (API 26+ up to Android 14+ / 34).
  - In Jetpack Compose, updating the localized `Context` and `Configuration` in `CompositionLocalProvider` ensures instant dynamic recomposition of all `stringResource(id)` calls without requiring an activity recreation or losing navigation backstack state.
- **Alternatives Considered**:
  - `Activity.recreate()`: Causes screen flicker, navigation state loss, and potential modal dismissals. Rejected in favor of smooth reactive Compose recomposition.

## 2. Resource Strategy: Default (en) and Regional (pt-rBR)

- **Decision**:
  - `res/values/strings.xml`: English (default fallback for all international locales).
  - `res/values-pt-rBR/strings.xml`: Brazilian Portuguese.
- **Rationale**: Follows standard Android localization practices. All string keys are identical between resource files.

## 3. String Keys Taxonomy & Standardization

- **Decision**: Organize string resources with standard prefixes:
  - `app_*`: Global app-level strings (`app_name`, `app_hero_title`)
  - `nav_*` / `common_*`: Navigation and generic actions (`back`, `cancel`, `save`, `edit`, `delete`, `apply`, `ok`)
  - `home_*`: Home screen labels (`home_collections_title`, `home_quick_add_title`, `home_search_placeholder`)
  - `collection_*`: Collection detail and actions (`collection_all_links`, `collection_pinned`, `collection_links_count`, `collection_subcollections_count`, `collection_empty_title`, `collection_empty_add`)
  - `bookmark_*`: Bookmark detail and operations (`bookmark_description`, `bookmark_notes`, `bookmark_notes_placeholder`, `bookmark_tags`, `bookmark_add_tag`, `bookmark_move_title`, `bookmark_delete_title`, `bookmark_delete_confirm_msg`, `bookmark_show_more`, `bookmark_show_less`)
  - `save_*`: Save modal strings (`save_bookmark_title`, `save_new_folder`, `save_folder_name_hint`)
  - `settings_*` / `pref_*`: Settings and preferences strings

## 4. Date & Plural Formatting

- **Decision**: Format dates using `SimpleDateFormat` or `DateTimeFormatter` initialized with the active `Locale` (e.g. `Locale("pt", "BR")` vs `Locale.ENGLISH`). Use formatted string placeholders for counts (e.g. `"%d links · %d subcollections"`).
- **Rationale**: Guarantees consistent typography and formatting respecting the active app locale.
