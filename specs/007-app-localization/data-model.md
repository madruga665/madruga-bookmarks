# Data Model: App Localization

**Feature**: `007-app-localization`
**Date**: 2026-08-15

## Enums & Data Structures

### `AppLanguage` Enum

Represents the available language preferences.

```kotlin
enum class AppLanguage(val code: String) {
    SYSTEM("system"),
    EN("en"),
    PT_BR("pt-BR")
}
```

| Value | Code | Display Name (EN) | Display Name (PT-BR) |
|---|---|---|---|
| `SYSTEM` | `"system"` | System Default | Padrão do Sistema |
| `EN` | `"en"` | English | Inglês |
| `PT_BR` | `"pt-BR"` | Portuguese (Brazil) | Português (Brasil) |

---

## DataStore Preference Keys

Stored in `settings_preferences.preferences_pb`:

- Key: `app_language` (String)
- Default: `AppLanguage.SYSTEM.name` ("SYSTEM")

---

## String Resource Key Inventory

| Scope | String Resource Key | Default (EN) | Português (PT-BR) |
|---|---|---|---|
| Common | `dialog_cancel` | Cancel | Cancelar |
| Common | `dialog_save` | Save | Salvar |
| Common | `dialog_delete` | Delete | Excluir |
| Common | `dialog_edit` | Edit | Editar |
| Common | `dialog_confirm` | Confirm | Confirmar |
| Home | `home_title` | Madruga Bookmarks | Madruga Bookmarks |
| Home | `home_collections_heading` | COLLECTIONS | COLEÇÕES |
| Home | `home_quick_add_title` | Quick Add | Adicionar Rápido |
| Home | `home_search_placeholder` | Search bookmarks, tags... | Buscar links, tags... |
| Collection | `collection_all_links` | ALL LINKS (%d) | TODOS OS LINKS (%d) |
| Collection | `collection_pinned` | PINNED (%d) | FIXADOS (%d) |
| Collection | `collection_empty_title` | No bookmarks yet | Nenhum link salvo |
| Collection | `collection_empty_add` | Add Link | Adicionar Link |
| Collection | `collection_subtitle_fmt` | %1$d links · %2$d subcollections | %1$d links · %2$d subcoleções |
| Bookmark | `bookmark_description` | DESCRIPTION | DESCRIÇÃO |
| Bookmark | `bookmark_notes` | NOTES | NOTAS |
| Bookmark | `bookmark_notes_placeholder` | Tap to add notes... | Toque para adicionar notas... |
| Bookmark | `bookmark_tags` | TAGS | TAGS |
| Bookmark | `bookmark_add_tag` | Add Tag | Adicionar Tag |
| Bookmark | `bookmark_show_more` | Show more | Mostrar mais |
| Bookmark | `bookmark_show_less` | Show less | Mostrar menos |
| Bookmark | `bookmark_delete_title` | Delete Bookmark? | Excluir Bookmark? |
| Bookmark | `bookmark_delete_msg` | Are you sure you want to delete this bookmark? | Tem certeza que deseja excluir este bookmark? |
| Bookmark | `bookmark_move_title` | Move to Collection | Mover para Pasta |
| Save | `save_modal_title` | Save Bookmark | Salvar Bookmark |
| Save | `save_new_folder` | Create new folder | Criar nova pasta |
| Save | `save_folder_name` | Folder Name | Nome da Pasta |
| Settings | `settings_title` | Settings | Configurações |
