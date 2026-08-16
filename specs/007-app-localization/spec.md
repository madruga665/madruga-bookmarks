# Feature Specification: Full App Localization & Internationalization (i18n)

**Feature Branch**: `007-app-localization`

**Created**: 2026-08-15

**Status**: Draft

**Input**: User description: "internacionalizaçao completa, traduzir todos os texto da aplicaçao de acordo com o idioma que o usuario seleciona nas configuraçoes"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Comprehensive UI Localization in English & Portuguese (Priority: P1) 🎯 MVP

Every user-facing screen, modal, bottom sheet, action menu, empty state, and dialog throughout the entire application displays localized text according to the selected language (`English` or `Português (Brasil)`). No hardcoded text strings remain in the UI layer.

**Why this priority**: Core objective of internationalization—ensuring all app text is properly localized for both Portuguese and English speaking users.

**Independent Test**: Navigate through every screen in the app (Home, Save Modal, Collection Detail, Collection Actions, Bookmark Details, Settings) with the language set to English, and verify 100% of visible labels, buttons, and placeholders are in English; then switch to Portuguese and verify 100% are in Portuguese.

**Acceptance Scenarios**:

1. **Given** the app language is set to English, **When** viewing the Home screen, **Then** all headers, search placeholders, collection counters, and buttons display in English (e.g., "COLLECTIONS", "Quick Add", "Save").
2. **Given** the app language is set to Português (Brasil), **When** viewing the Home screen, **Then** all headers, search placeholders, collection counters, and buttons display in Portuguese (e.g., "COLEÇÕES", "Adicionar Rápido", "Salvar").
3. **Given** any screen (Bookmark Details, Save Modal, Collection Actions), **When** viewed in either language, **Then** all dialog titles, action buttons ("Save"/"Salvar", "Cancel"/"Cancelar", "Delete"/"Excluir", "Show more"/"Mostrar mais"), and section titles match the chosen locale.

---

### User Story 2 - Instant Dynamic Language Switching in Settings (Priority: P1) 🎯 MVP

When a user selects a language in the Settings screen (`Português (Brasil)`, `English`, or `System Default`), the entire app UI immediately updates its locale dynamically without requiring a manual app restart and without losing the user's current session or navigation state.

**Why this priority**: Frictionless user experience when altering language preferences.

**Independent Test**: Open Settings, tap Language preference, choose "Português (Brasil)"; verify that the Settings screen and all other navigation destinations immediately reflect Portuguese text.

**Acceptance Scenarios**:

1. **Given** the user is on the Settings screen, **When** they tap Language and select "Português (Brasil)", **Then** the Settings screen instantly updates to Portuguese ("Configurações", "Preferências", "Tema", "Idioma").
2. **Given** the user changed the language to English, **When** they navigate back to the Home or Collection view, **Then** those views immediately display English text.
3. **Given** a selected language, **When** the app is closed and reopened, **Then** the app starts with the previously chosen language loaded from persistent storage.

---

### User Story 3 - System Default Auto-Detection & Fallback (Priority: P2)

When the user selects "System Default" (`Padrão do Sistema`), the app automatically detects the device's operating system locale. If the device is set to Portuguese, the app displays in Portuguese; otherwise, it falls back cleanly to English.

**Why this priority**: Respects user's global OS settings by default upon initial app installation.

**Independent Test**: Set device OS language to Portuguese, select "System Default" in Settings, and verify the app displays Portuguese; change device OS to English/other, and verify app adapts accordingly.

**Acceptance Scenarios**:

1. **Given** the app language setting is "System Default", **When** the device OS locale is `pt-BR`, **Then** the app renders all UI elements in Brazilian Portuguese.
2. **Given** the app language setting is "System Default", **When** the device OS locale is `en-US` or another unsupported locale, **Then** the app renders all UI elements in English.

---

### User Story 4 - Formatted Counters, Plurals & Date Localization (Priority: P2)

Pluralized counters (e.g., `1 link` vs `2 links`, `0 subcollections` vs `1 subcollection`) and formatted dates/timestamps (e.g., `11 de ago. de 2026` vs `Aug 11, 2026`) format according to the grammatical and formatting rules of the active locale.

**Why this priority**: Polished linguistic accuracy across different language conventions.

**Independent Test**: View a collection with 1 link and 2 links in both languages, confirming proper singular/plural text and localized date formats.

**Acceptance Scenarios**:

1. **Given** a collection with 1 link in English, **When** displayed, **Then** the subtitle reads "1 link".
2. **Given** a collection with multiple links in Portuguese, **When** displayed, **Then** the subtitle reads "[N] links" or "[N] favoritos".
3. **Given** creation timestamps in Bookmark Details, **When** viewed in Portuguese, **Then** dates format using Brazilian locale conventions; when viewed in English, they format using English conventions.

---

### Edge Cases

- **Special Characters & Accents**: Portuguese strings with diacritics/accents (e.g., "Configurações", "Coleções", "Excluir", "Padrão") render without character corruption or encoding issues.
- **Dynamic Content vs Static Strings**: User-generated content (bookmark custom titles, user notes, tag names) remains in the user's original typed language, while UI labels around them localize.
- **Orientation & Configuration Changes**: Changing device orientation or dark/light theme does not reset or alter the selected language.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST provide comprehensive string resource files for English (`res/values/strings.xml`) and Brazilian Portuguese (`res/values-pt-rBR/strings.xml`).
- **FR-002**: All user-facing UI text across all screens (Home, Save Modal, Collection Detail, Collection Actions, Bookmark Details, Settings, Search) MUST be loaded from Android string resources (`R.string.*` / `stringResource(...)`).
- **FR-003**: No hardcoded string literals MUST remain in Composable UI components for user-visible labels, headers, buttons, dialog messages, tooltips, or input placeholders.
- **FR-004**: Selecting a language option in the Settings language dialog MUST persist the selection in DataStore preferences.
- **FR-005**: Changing language in Settings MUST immediately apply the new locale dynamically to the running application configuration without requiring a force-close or app restart.
- **FR-006**: The system MUST support three language options: "System Default" (`Padrão do Sistema`), "English" (`English`), and "Português (Brasil)" (`Português (Brasil)`).
- **FR-007**: Plural and quantity strings (e.g., link counts and subcollection counts) MUST use localized format strings.
- **FR-008**: System toasts and confirmation alert messages MUST be fully localized.

### Key Entities

- **AppLanguage**: Enum representing supported languages (`SYSTEM`, `EN`, `PT_BR`).
- **SettingsPreferences**: Preferences entity storing `app_language`, `theme_mode`, and `haptic_feedback`.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of user-facing UI text across all 7 app screens and modals is translated in both English and Portuguese.
- **SC-002**: Language switching in Settings takes effect across all screens in under 100ms.
- **SC-003**: 0 hardcoded user-visible text strings detected in the UI layer.
- **SC-004**: Language preference is retained across 100% of app restarts.

## Assumptions

- Standard Android `res/values/strings.xml` and `res/values-pt-rBR/strings.xml` resource folders are used.
- Android `AppCompatDelegate.setApplicationLocales` or Compose locale provider configuration is used for dynamic locale switching.
