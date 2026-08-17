# Research: Create New Collection Modal

## 1. Icon Registry & Vector Mappings

### Decision
Implement a centralized `CollectionIconRegistry` containing all ~43 category icons shown in the reference design, mapped by stable string keys (e.g. `"folder"`, `"star"`, `"heart"`, `"book"`, `"music"`, `"camera"`, `"flag"`, `"moon"`, `"sun"`, `"cloud"`, `"pin"`, `"calendar"`, `"globe"`, `"gift"`, `"leaf"`, `"luggage"`, `"shopping_cart"`, `"plane"`, `"car"`, `"coffee"`, `"film"`, `"headphones"`, `"palette"`, `"gamepad"`, `"dumbbell"`, `"dollar"`, `"phone"`, `"computer"`, `"clock"`, `"lightbulb"`, `"school"`, `"shield"`, `"restaurant"`, `"tv"`, `"bell"`, `"key"`, `"view_in_ar"`, `"layers"`, `"code"`, `"bolt"`, `"bookmark"`, `"label"`, `"home"`).

### Rationale
- Using Material Icons (`Icons.Outlined.*` and `Icons.Filled.*` or custom standard vectors) ensures lightweight, crisp rendering with zero external bitmap overhead.
- String keys allow seamless serialization into Room database (`CollectionEntity.iconKey`) and future API synchronization (Constitution Principle I).
- Centralizing in `CollectionIconRegistry` enables sharing across `CreateCollectionBottomSheet`, `NeobrutalistFolderCard`, `CollectionHeader`, and `SaveBookmarkBottomSheet`.

### Alternatives Considered
- *Dynamic Resource Identifiers*: Not type-safe and fragile across builds.
- *Bundled SVGs*: Material Icons already provide identical vectors in Compose Material Icons Extended.

---

## 2. Color Palette Representation (16 Swatches)

### Decision
Define a 16-color curated Neobrutalism palette in `CollectionPalette` with hex strings and named keys:
1. **Yellow**: `#FFE600` (Default / Brand Yellow)
2. **Pink / Magenta**: `#FF4B8B`
3. **Purple / Violet**: `#9B51E0`
4. **Blue**: `#2F80ED`
5. **Mint / Teal**: `#00C49F`
6. **Green**: `#48BB78`
7. **Lime / Chartreuse**: `#A0E040`
8. **Orange**: `#FF7700`
9. **Sand / Cream**: `#FDE5A9`
10. **Gray**: `#A0AEC0`
11. **Slate Blue**: `#6C88A8`
12. **Mauve / Lilac**: `#BA68C8`
13. **Brown**: `#9C6644`
14. **Dark Slate / Black**: `#1E1E1E`
15. **Coral / Salmon**: `#FF6B6B`
16. **Indigo**: `#5352ED`

### Rationale
- Matches the reference design color grid 1:1.
- Provides high contrast and visual vibrancy in both Light and Catppuccin Mocha Dark themes.
- Hex codes are stored as strings in `CollectionEntity.colorAccent`, backward compatible with existing repository methods.

---

## 3. Modal Architecture & State Management

### Decision
Use a Jetpack Compose `ModalBottomSheet` (or custom Neobrutalist bottom sheet surface) controlled via a dedicated `CreateCollectionViewModel` or state-hoisted composable.

### Rationale
- Clean Architecture separation: `CreateCollectionViewModel` handles input mutation, validation rules (max 40 chars, non-blank), and repository calls.
- Unidirectional Data Flow (UDF) with immutable `CreateCollectionUiState` exposed via `StateFlow`.
- Testable with standard coroutine test dispatchers and MockK.

---

## 4. Entry Point & Integration

### Decision
1. **Home Screen Top Bar**: Tapping the folder button (`Icons.Outlined.FolderSpecial`) in `HomeScreenTopBar` triggers `CreateCollectionBottomSheet`.
2. **Bookmark Save Flow**: Tapping "Create New Folder" in `SaveBookmarkBottomSheet` can seamlessly open the full `CreateCollectionBottomSheet`, auto-selecting the newly created collection on completion.
3. **Repository Extension**: Update `CollectionRepository.createCollection` to accept both `colorAccent` and `iconKey` (defaulting to `"folder"`).

