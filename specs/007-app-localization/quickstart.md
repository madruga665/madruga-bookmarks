# Quickstart & Verification Guide: Full App Localization

**Feature**: `007-app-localization`
**Date**: 2026-08-15

## 1. Automated Unit Test Verification

Run all unit tests in the repository:

```bash
./gradlew testDebugUnitTest
```

### Specific Localization Tests

```bash
./gradlew testDebugUnitTest --tests "com.madruga665.bookmarks.ui.settings.*"
./gradlew testDebugUnitTest --tests "com.madruga665.bookmarks.data.repository.SettingsRepositoryTest"
```

---

## 2. Manual Verification Scenarios

1. **Verify Default / English Display**:
   - Open app with device in English.
   - Verify Home ("COLLECTIONS", "Quick Add", "Total Links"), Collection Detail ("ALL LINKS", "PINNED"), and Bookmark Details ("DESCRIPTION", "NOTES", "TAGS") all display in English.
2. **Switch to Portuguese in Settings**:
   - Open Settings (`/settings`), tap Language ("Idioma"), select "Português (Brasil)".
   - Verify Settings screen immediately updates labels to Portuguese ("Configurações", "Preferências", "Tema", "Idioma").
3. **Verify All Other Screens in Portuguese**:
   - Navigate back to Home: Verify "COLEÇÕES", "Adicionar Rápido", etc.
   - Open a Collection: Verify "TODOS OS LINKS", "FIXADOS", "%d links · %d subcoleções".
   - Open a Bookmark: Verify "DESCRIÇÃO", "NOTAS", "TAGS", "Mover para Pasta", "Excluir Bookmark?".
   - Open Save Modal: Verify "Salvar Bookmark", "Criar nova pasta", etc.
4. **App Restart Persistence**:
   - Force close the app and reopen.
   - Verify the app launches directly in Portuguese.
5. **Switch Back to English & System Default**:
   - Open Settings, choose "English" -> instant update to English.
   - Choose "System Default" -> adopts device OS language.
