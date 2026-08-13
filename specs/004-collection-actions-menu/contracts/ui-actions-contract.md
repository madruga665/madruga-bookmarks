# Contract: Collection Actions UI Component Callbacks & Contracts

## 1. `NeobrutalistFolderCard` Callback Interface

```kotlin
@Composable
fun NeobrutalistFolderCard(
    collection: CollectionEntity,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
)
```

- `onClick`: Triggered on short tap (<300ms) to navigate into Collection Bookmarks List view.
- `onLongClick`: Triggered on touch-and-hold (>500ms) to launch Collection Actions Menu overlay.

---

## 2. `CollectionActionsOverlay` Interface

```kotlin
@Composable
fun CollectionActionsOverlay(
    collection: CollectionEntity,
    onDismiss: () -> Unit,
    onEditClick: (CollectionEntity) -> Unit,
    onShareClick: (CollectionEntity) -> Unit,
    onDeleteClick: (CollectionEntity) -> Unit,
    modifier: Modifier = Modifier
)
```

### Action Behaviours

1. **`onDismiss`**: Triggered when user taps outside the floating card overlay or presses back key.
2. **`onEditClick`**: Triggered when user taps the yellow pencil floating icon button. Closes overlay and emits `onEditClick(collection)`.
3. **`onShareClick`**: Triggered when user taps the blue share floating icon button. Closes overlay and triggers Android OS share sheet.
4. **`onDeleteClick`**: Triggered when user taps the trash bin floating icon button. Closes overlay and emits `onDeleteClick(collection)`.

---

## 3. Native Share Payload Contract

- **Action**: `Intent.ACTION_SEND`
- **MIME Type**: `text/plain`
- **Extra Text Payload Format**:
  ```text
  Check out my collection "[Collection Name]" on Tuckii Bookmarks!
  https://tuckii.app/c/[Collection ID]
  ```
