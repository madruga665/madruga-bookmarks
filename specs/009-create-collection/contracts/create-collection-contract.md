# Component & ViewModel Contracts: Create New Collection Modal

## 1. ViewModel Contract (`CreateCollectionViewModel`)

```kotlin
interface CreateCollectionViewModelContract {
    val uiState: StateFlow<CreateCollectionUiState>

    fun onNameChange(name: String)
    fun onColorSelect(hexColor: String)
    fun onIconSelect(iconKey: String)
    fun createCollection(onSuccess: (CollectionEntity) -> Unit = {})
    fun resetState()
}
```

---

## 2. Composable Contracts

### `CreateCollectionBottomSheet`

```kotlin
@Composable
fun CreateCollectionBottomSheet(
    onDismiss: () -> Unit,
    onCollectionCreated: (CollectionEntity) -> Unit = {},
    viewModel: CreateCollectionViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
)
```

### `CollectionColorPicker`

```kotlin
@Composable
fun CollectionColorPicker(
    colors: List<CollectionColorItem>,
    selectedColorHex: String,
    onColorSelect: (String) -> Unit,
    modifier: Modifier = Modifier
)
```

### `CollectionIconPicker`

```kotlin
@Composable
fun CollectionIconPicker(
    icons: List<CollectionIconItem>,
    selectedIconKey: String,
    selectedColor: Color,
    onIconSelect: (String) -> Unit,
    modifier: Modifier = Modifier
)
```

---

## 3. Repository Extension Contract (`CollectionRepository`)

```kotlin
suspend fun createCollection(
    name: String,
    colorAccent: String,
    iconKey: String = "folder"
): CollectionEntity?
```

