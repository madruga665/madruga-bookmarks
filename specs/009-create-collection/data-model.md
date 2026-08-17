# Data Model: Create New Collection Modal

## 1. Domain & Persistence Entities

### `CollectionEntity` (Room Table: `collections`)

| Field | Type | Constraints | Description |
|---|---|---|---|
| `id` | `String` | Primary Key, Non-null | Unique UUID or stable identifier (e.g. `col_design_uuid`) |
| `name` | `String` | Non-null, Max 40 chars | User-provided collection title |
| `colorAccent` | `String` | Non-null | Hex code (e.g., `"#FFE600"`) or color token name |
| `iconKey` | `String` | Non-null | Key mapped in `CollectionIconRegistry` (e.g., `"folder"`, `"star"`) |
| `linkCount` | `Int` | Default 0 | Computed or cached count of bookmarks in collection |
| `createdAt` | `Long` | Non-null | Timestamp in milliseconds |
| `updatedAt` | `Long` | Non-null | Timestamp in milliseconds |

---

## 2. Presentation UI State Models

### `CreateCollectionUiState`

```kotlin
data class CreateCollectionUiState(
    val nameInput: String = "",
    val selectedColor: String = "#FFE600", // Default Yellow
    val selectedIconKey: String = "folder", // Default Folder
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) {
    val characterCount: Int
        get() = nameInput.length

    val isSubmitEnabled: Boolean
        get() = nameInput.isNotBlank() && nameInput.length <= 40 && !isSubmitting
}
```

---

## 3. UI Helper Models & Registry

### `CollectionColorItem`

```kotlin
data class CollectionColorItem(
    val id: String,
    val hexColor: String,
    val composeColor: Color
)
```

### `CollectionIconItem`

```kotlin
data class CollectionIconItem(
    val key: String,
    val imageVector: ImageVector,
    val contentDescriptionRes: Int
)
```

---

## 4. Validation Rules & Invariants

1. **Collection Name**:
   - Must be non-blank (`name.trim().isNotEmpty()`).
   - Maximum length: 40 characters (`name.length <= 40`).
   - Characters beyond 40 are rejected at input time.
2. **Color Selection**:
   - Must match one of the 16 predefined palette colors or a valid `#RRGGBB` hex string.
3. **Icon Selection**:
   - Must match a valid registered key in `CollectionIconRegistry`. If unknown, fall back safely to `"folder"`.
4. **Id Generation**:
   - Format: `"col_" + UUID.randomUUID().toString().take(8)`.

