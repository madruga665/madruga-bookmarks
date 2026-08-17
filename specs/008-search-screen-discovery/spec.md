# Feature Specification: Search Screen with Library Statistics & Discovery

**Feature Branch**: `008-search-screen-discovery`

**Created**: 2026-08-16

**Status**: Aligned

**Input**: User description: "tela de busca vai listar todos os links um card com informações da biblioteca de links \n referencia: /home/madruga665/Downloads/Screenshot_20260816_140257_Tuckii.jpg"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Dedicated Search Screen & Library Statistics Dashboard (Priority: P1)

Users tapping the search icon on the Home screen navigate to a dedicated Search screen. At the top, a prominent Neobrutalist search bar is displayed with placeholder text and an immediate "Cancel" button to dismiss search and return to Home. In the default discovery state (when no search query is entered), the screen prominently displays a "YOUR LIBRARY" summary card featuring live counters for Collections, Links, Pinned bookmarks, and Tags.

**Why this priority**: Delivers the primary entry point and high-level library overview requested in the design reference (Constitution Principle IV: Dedicated Search & Instant Discovery).

**Independent Test**: Can be tested by opening the Search screen from the Home screen, confirming the search bar, Cancel action, and "YOUR LIBRARY" card render with accurate live counts for all 4 metric categories.

**Acceptance Scenarios**:

1. **Given** the user is on the Home screen, **When** they tap the Search magnifying glass icon in the top app bar, **Then** the app navigates to the Search screen showing the search input and a "Cancel" button.
2. **Given** existing bookmarks and collections in the database, **When** the Search screen opens without a query, **Then** the "YOUR LIBRARY" card displays live counts for Collections, Links, Pinned items, and unique Tags across 4 distinct columns separated by visual dividers.
3. **Given** the Search screen, **When** the user taps "Cancel", **Then** the application navigates back to the Home screen.

---

### User Story 2 - Recently Saved Bookmarks Discovery Carousel (Priority: P1)

When viewing the Search screen in its initial discovery state, below the library stats card, the user sees a "RECENTLY SAVED" section (identified by a clock icon). This section displays a horizontally scrollable carousel of recent bookmark cards. Each card displays the rich thumbnail preview, a collection badge/pill (e.g., "Unsorted", "IA"), the bookmark title, and the source platform indicator (e.g., Instagram, X/Twitter, GitHub, Web). Tapping any card navigates to that bookmark's detail view (`bookmark_detail/{bookmarkId}`).

**Why this priority**: Provides instant, frictionless access to recently captured links without requiring typing, fulfilling Constitution Principle IV.

**Independent Test**: Can be tested by adding bookmarks to the app and opening the Search screen, confirming recent bookmarks appear horizontally in reverse-chronological order, and tapping any card opens the Bookmark Detail screen.

**Acceptance Scenarios**:

1. **Given** saved bookmarks in the database, **When** the Search screen is displayed with an empty query, **Then** the "RECENTLY SAVED" section displays cards for the most recent bookmarks ordered by creation/update timestamp descending.
2. **Given** a bookmark card in the carousel, **When** the user taps the card, **Then** the app navigates to that bookmark's detail screen (`bookmark_detail/{bookmarkId}`).
3. **Given** no bookmarks exist in the database, **When** the Search screen opens, **Then** the recently saved section displays an empty hint or is gracefully hidden.

---

### User Story 3 - Real-Time Instant Search Filtering (Priority: P2)

When the user enters text into the search bar, the Discovery elements (Library stats card, Recently Saved carousel, and idle prompt) are seamlessly hidden to give full screen focus to the real-time search results list. The search filters across bookmark titles, URLs, collection names, and tags with debounced real-time responsiveness (<200ms). Tapping any search result card navigates to that bookmark's detail view. A clear button (`X`) resets the search input and restores the Discovery view.

**Why this priority**: Core functionality for discovering and finding links rapidly (<200ms latency target from Constitution Principle IV).

**Independent Test**: Can be tested by typing queries matching various attributes (e.g. domain name, tag name, title keyword) and verifying that the Discovery sections hide, search results update in real-time with matching items, tapping an item opens detail view, and clear button restores the discovery view.

**Acceptance Scenarios**:

1. **Given** the Search screen with bookmarks in the database, **When** the user types a query (e.g., "Instagram", "IA", or a URL fragment), **Then** the "YOUR LIBRARY" card and "RECENTLY SAVED" carousel hide, and the screen displays a list of matching bookmarks in real time.
2. **Given** an active search query, **When** the user taps the clear (`X`) button inside the search field, **Then** the query is cleared and the screen immediately restores the Discovery state (Library card + Recently Saved carousel + "Type something to search..." prompt).
3. **Given** a search query that does not match any bookmark in the database, **When** the query is evaluated, **Then** the screen displays a "No results found for '<query>'" empty state with an appropriate illustration/icon.
4. **Given** a bookmark card in the search results list, **When** the user taps the card, **Then** the app navigates to that bookmark's detail screen (`bookmark_detail/{bookmarkId}`).

---

### User Story 4 - Neobrutalism Dual Theme & Localization (Priority: P3)

The Search screen respects the application-wide Neobrutalism design system in both Light and Dark modes. In Light mode, it features high-contrast black borders, hard offset shadows, and vibrant yellow accents. In Dark mode, it applies Catppuccin Mocha tokens (Base `#1e1e2e`, Surface `#313244`, Text `#cdd6f4`, accents). All text and metrics are fully localized in English and Portuguese (`pt-BR`).

**Why this priority**: Ensures visual consistency and multi-language accessibility across the entire application (Constitution Principle V).

**Independent Test**: Can be tested by switching between Light and Dark themes in Settings, and toggling device/app language between English and Portuguese (`pt-BR`), verifying that all borders, colors, and string resources render properly.

**Acceptance Scenarios**:

1. **Given** Dark Mode is enabled, **When** the user views the Search screen, **Then** all surfaces, cards, and text render using Catppuccin Mocha tokens with high contrast borders and dark offset shadows.
2. **Given** the app language is set to Portuguese (`pt-BR`), **When** viewing the Search screen, **Then** all labels ("SUA BIBLIOTECA", "Coleções", "Links", "Fixados", "Tags", "SALVOS RECENTEMENTE", "Buscar coleções ou links...", "Cancelar", "Digite algo para buscar...", "Nenhum resultado encontrado") appear correctly localized.

---

### Edge Cases

- **Empty Database (Zero bookmarks/collections)**:
  - "YOUR LIBRARY" displays `0` for Collections, `0` for Links, `0` for Pinned, `0` for Tags.
  - "RECENTLY SAVED" section shows an empty prompt or is omitted.
  - Search returns empty state for any search term.
- **Bookmarks with Missing Metadata (no thumbnail, title, or platform)**:
  - Card displays domain fallback thumbnail and extracts readable title from URL domain/path without crashing.
- **Special Characters and Whitespace in Search**:
  - Search queries containing leading/trailing whitespace, punctuation, accents, or special symbols are sanitized and matched case-insensitively.
- **Rapid Typing / Query Debouncing**:
  - Queries are debounced (~150-250ms) to ensure smooth performance without dropping user input or jittering.
- **Large Dataset Performance**:
  - The search list uses lazy rendering (`LazyColumn` / `LazyRow`) to ensure fluid 60fps scrolling even with hundreds of bookmarks.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST provide a dedicated Search screen registered in the app navigation graph (`NavRoutes.SEARCH`).
- **FR-002**: The Search screen top bar MUST include a text input field with placeholder text and an explicit "Cancel" action that navigates back to the previous screen.
- **FR-003**: When the search query is empty, the screen MUST display a "YOUR LIBRARY" summary card with live counts for:
  1. Total Collections
  2. Total Links (Bookmarks)
  3. Total Pinned Bookmarks
  4. Total Unique Tags
- **FR-004**: When the search query is empty, the screen MUST display a "RECENTLY SAVED" section containing a horizontal carousel (`LazyRow`) of recent bookmarks ordered by creation/update timestamp descending.
- **FR-005**: Each bookmark card in the recently saved carousel MUST display:
  1. Thumbnail preview image (with fallback domain/platform image)
  2. Collection pill badge overlaid or positioned prominently
  3. Bookmark title (max 2 lines with ellipsis)
  4. Platform icon and platform name / domain
- **FR-006**: In the default empty search state, the area below the carousel MUST display an idle prompt ("Type something to search..." with a search icon).
- **FR-007**: When the user enters text in the search input, the system MUST filter bookmarks matching against title, URL, collection name, or tags in real time (<200ms response).
- **FR-008**: In active search mode, the "YOUR LIBRARY" card and "RECENTLY SAVED" carousel MUST be hidden, giving full view to the search results list.
- **FR-009**: The search input MUST display a clear (`X`) icon when non-empty, which resets the query and restores the discovery view.
- **FR-010**: When a search query produces zero matches, the system MUST display an empty state indicating no results were found.
- **FR-011**: Tapping any bookmark card (from recently saved carousel or search results) MUST navigate to that bookmark's detail screen (`NavRoutes.bookmarkDetail(id)`).
- **FR-012**: The Search screen MUST adhere to Neobrutalism design tokens (bold borders, offset shadows, vibrant yellow accents) and support both Light Mode and Catppuccin Mocha Dark Mode.
- **FR-013**: All UI strings and labels MUST be localized in both English (`values/strings.xml`) and Portuguese (`values-pt-rBR/strings.xml`).

### Key Entities

- **SearchUiState**: UI state holding the current search query, search results list, recent bookmarks list, library statistics (`collectionsCount`, `linksCount`, `pinnedCount`, `tagsCount`), and loading/empty status.
- **LibraryStats**: Value object encapsulating total collections count, total links count, pinned bookmarks count, and unique tags count.
- **BookmarkEntity**: Core bookmark representation containing id, url, title, description, faviconUrl, thumbnailUrl, sourcePlatform, collectionId, tags, isPinned, createdAt, updatedAt.
- **CollectionEntity**: Collection representation containing id, name, linkCount, iconKey, colorAccent.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Search query evaluation and UI update occur in under 50ms for local queries.
- **SC-002**: 100% of bookmark titles, URLs, collection names, and tags are indexed and searchable.
- **SC-003**: Library metrics (collections, links, pinned, tags) accurately match live Room database records with 100% precision.
- **SC-004**: Users can navigate from Home to Search, locate a bookmark, and open its details in under 3 taps.
- **SC-005**: All UI elements maintain high-contrast Neobrutalist design compliance across both Light and Dark themes with zero visual regressions.

## Assumptions

- Search operates primarily on the local Room database cache to ensure instantaneous offline search (<50ms).
- Unique tags are derived dynamically from the comma-separated `tags` column in existing `BookmarkEntity` records.
- Tapping "Cancel" dismisses the search screen and returns to the previous destination in the navigation stack.
- The Recently Saved carousel displays up to 10 of the most recently created/updated bookmarks.
