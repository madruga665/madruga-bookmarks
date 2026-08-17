# Feature Specification: Bookmark Tagging System

**Feature Branch**: `010-bookmark-tags`

**Created**: 2026-08-16

**Status**: Draft

**Input**: User description: "Sistema de Tags / Etiquetas nos Bookmarks - Adicionar tags personalizadas ao salvar ou editar links, filtrar bookmarks por tags na busca e exibir chips de tags com visual Neobrutalista."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Add and Remove Tags during Bookmark Capture & Editing (Priority: P1) 🎯 MVP

When saving a new bookmark (via the in-app quick save bar, native share target, or bookmark save modal) or editing an existing bookmark, users can assign one or more descriptive tags (e.g., `design`, `kotlin`, `ai`, `career`) to categorize their links across multiple dimensions beyond single folders.

**Why this priority**: Core classification mechanism fulfilling Constitution Principle III (Flexible Organization) and enabling multi-dimensional bookmark organization.

**Independent Test**: Open the bookmark save modal or details screen, type one or more tags (e.g., `compose`, `android`), submit, and verify that the tags are saved with the bookmark and visible on its card.

**Acceptance Scenarios**:

1. **Given** the user is saving a bookmark in `SaveBookmarkBottomSheet` or editing in `BookmarkDetailScreen`, **When** typing in the tag input field and pressing Enter, comma, or tapping Add, **Then** a new Neobrutalist tag chip is created and displayed in the active tags list.
2. **Given** a bookmark with existing tags, **When** tapping the remove ('X') icon on a tag chip, **Then** the tag is immediately removed from the active list.
3. **Given** the user attempts to add an empty tag or a tag exceeding 25 characters, **Then** the input rejects the empty string and caps the tag at 25 characters.
4. **Given** the user attempts to add a duplicate tag (case-insensitive, e.g., `Design` when `design` already exists), **Then** the duplicate is ignored and not added twice.

---

### User Story 2 - Filter Bookmarks by Tags in Dedicated Search (Priority: P1) 🎯 MVP

Users can filter bookmarks by tapping tag chips on the Search Screen. Tapping a tag immediately filters all bookmarks containing that tag, with seamless combination with text query search.

**Why this priority**: Fulfills Constitution Principle IV (Dedicated Search & Instant Discovery) allowing lightning-fast bookmark retrieval by topic.

**Independent Test**: Navigate to the Search Screen, tap a tag chip (e.g., `#compose`), and verify that only bookmarks tagged with `#compose` are displayed in the results list in under 100ms.

**Acceptance Scenarios**:

1. **Given** the Search Screen, **When** the screen is loaded, **Then** a horizontal scrollable row or section of active tags is displayed below the search bar.
2. **Given** active tags on the Search Screen, **When** tapping a tag chip, **Then** the tag toggles to a selected/highlighted state and the search results list updates instantly to show only bookmarks matching that tag.
3. **Given** an active tag filter and a typed text query, **When** searching, **Then** the results match both the text query (title/URL) and the selected tag.
4. **Given** a selected tag filter, **When** tapping the selected tag again or clearing filters, **Then** the tag filter is deactivated and all bookmarks are displayed.

---

### User Story 3 - Visual Tag Chips on Cards & Details Screen (Priority: P2)

Bookmark cards in collection lists and search results display compact Neobrutalist tag pills. The Bookmark Detail screen presents the full list of tags with options to add more or remove existing ones.

**Why this priority**: Enhances visual recognition and enables managing tags post-capture.

**Independent Test**: View a collection containing tagged bookmarks; verify that tag badges appear on each card with proper typography, contrasting backgrounds, and borders.

**Acceptance Scenarios**:

1. **Given** a bookmark with tags, **When** rendered in `NeobrutalistBookmarkCard`, **Then** compact tag pills (e.g., `#design`, `#android`) render below the title/description with Neobrutalism border and pill shape.
2. **Given** a bookmark with more than 3 tags, **When** rendered on a compact card, **Then** the first 2-3 tags render alongside a `+N` indicator to prevent layout clipping.
3. **Given** the `BookmarkDetailScreen`, **When** viewing the bookmark, **Then** all assigned tags are displayed in a flow row with interactive remove buttons and an "Add Tag" input.

---

### User Story 4 - Tag Autocomplete & Suggestions (Priority: P3)

When typing in the tag input field, the system suggests existing tags from the user's library matching the prefix, speeding up input and preventing accidental near-duplicate tags (e.g., `design` vs `designs`).

**Why this priority**: Minimizes typing friction during capture and maintains clean tag taxonomy.

**Independent Test**: Type `de` in the tag field; verify a suggestion dropdown/row displays existing tag `design` and tapping it adds the tag.

**Acceptance Scenarios**:

1. **Given** existing tags in the database, **When** the user types 1 or more characters in the tag input, **Then** matching existing tags appear as clickable suggestion pills.
2. **Given** a suggestion pill, **When** the user taps it, **Then** the suggested tag is added to the active tags list and the input text is cleared.

---

### Edge Cases

- **Special Characters in Tags**: Tags allow letters, numbers, hyphens, and underscores; spaces are converted to hyphens or treated as tag delimiters.
- **Case Sensitivity**: Tags are case-insensitive and normalized to lowercase (e.g., `Android` and `android` resolve to the same tag).
- **Maximum Tag Limit**: A maximum of 10 tags per bookmark to preserve UI layout integrity and storage efficiency.
- **Bookmarks with Zero Tags**: Bookmarks without tags function seamlessly without empty placeholders or broken spacing.
- **Deleting the Last Tag**: Removing all tags from a bookmark cleanly updates the entity to an empty tag list without errors.
- **Theme Adaptability**: Tag chips and inputs render with high contrast and distinct Neobrutalism borders in both Light and Catppuccin Mocha Dark themes.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST support associating an ordered list of unique string tags (`List<String>`) with each bookmark entity.
- **FR-002**: The system MUST normalize tag names by trimming leading/trailing whitespace, converting to lowercase, and disallowing empty tags.
- **FR-003**: The system MUST enforce a maximum length of 25 characters per tag and a maximum of 10 tags per bookmark.
- **FR-004**: The system MUST provide an interactive Neobrutalist tag input field allowing users to type and add tags via Enter key, comma delimiter, or Add button.
- **FR-005**: The system MUST allow removing any tag from a bookmark by tapping a clear/remove ('X') button on the tag chip.
- **FR-006**: The bookmark save modal (`SaveBookmarkBottomSheet`) MUST include the tag input and display active tag chips during initial capture.
- **FR-007**: The bookmark details screen (`BookmarkDetailScreen`) MUST display all assigned tags and support adding and removing tags dynamically.
- **FR-008**: The bookmark card component (`NeobrutalistBookmarkCard`) MUST display compact tag badges for tagged bookmarks.
- **FR-009**: The search screen (`SearchScreen`) MUST provide interactive tag filter chips to filter search results by selected tag(s).
- **FR-010**: The search engine (`BookmarkRepository.searchBookmarks` or DAO) MUST evaluate tags in addition to title, URL, and folder name (Constitution Principle IV).
- **FR-011**: All tag-related strings and labels MUST be localized in English (`values/strings.xml`) and Portuguese (`values-pt-rBR/strings.xml`).
- **FR-012**: All tag chips, inputs, and suggestion surfaces MUST adhere to Neobrutalism design tokens (2.5dp/2.0dp black borders, crisp offset shadows, rounded corners, high contrast).

### Key Entities

- **Bookmark**: Entity updated with `tags: List<String>` (stored as JSON/comma-separated string in Room or relational mapping).
- **TagSummary**: Aggregate representation containing `name: String`, `bookmarkCount: Int`, and optional `colorAccent: String`.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users can add a tag to a bookmark in under 3 seconds during capture or edit.
- **SC-002**: Tag filtering on the Search Screen filters and renders results in under 100ms.
- **SC-003**: 100% of tag chips and components adhere to Neobrutalism design tokens in both Light and Dark themes.
- **SC-004**: Search queries matching tags return 100% accurate results without regressions to title/URL search.

## Assumptions

- Tags are stored locally in Room database within `BookmarkEntity` (using a Room TypeConverter for `List<String>`) or a dedicated Tag table.
- Tags are synchronized with future backend API sync according to Constitution Principle I.
- Localization strings are provided for `en` and `pt-BR`.
