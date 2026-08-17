package com.madruga665.bookmarks.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.components.NeobrutalistButton
import com.madruga665.bookmarks.ui.search.components.RecentlySavedSection
import com.madruga665.bookmarks.ui.search.components.SearchEmptyState
import com.madruga665.bookmarks.ui.search.components.SearchIdlePrompt
import com.madruga665.bookmarks.ui.search.components.SearchResultsList
import com.madruga665.bookmarks.ui.search.components.YourLibraryCard
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

/**
 * Top-level Search screen with search bar input, Cancel back button,
 * dynamic discovery dashboard, and real-time instant search filtering.
 */
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onCancelClick: () -> Unit,
    onBookmarkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NeobrutalismTheme.colors.background)
            .statusBarsPadding()
            .testTag("tag_search_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // Top Bar: Search Input Field and Cancel Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .testTag("tag_search_top_bar"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Search Input Field
                val inputShape = RoundedCornerShape(12.dp)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .neobrutalistShadow(
                            shadowColor = NeobrutalismTheme.colors.shadow,
                            borderColor = NeobrutalismTheme.colors.border,
                            borderWidth = 2.dp,
                            shadowOffset = 3.dp,
                            shape = inputShape
                        )
                        .background(NeobrutalismTheme.colors.surface, inputShape)
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                            tint = NeobrutalismTheme.colors.onSurface,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (uiState.searchQuery.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.search_placeholder),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = NeobrutalismTheme.colors.subtext
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            val focusManager = LocalFocusManager.current
                            BasicTextField(
                                value = uiState.searchQuery,
                                onValueChange = onQueryChange,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                                textStyle = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = NeobrutalismTheme.colors.onSurface
                                ),
                                cursorBrush = SolidColor(NeobrutalismTheme.colors.onSurface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("tag_search_input")
                            )
                        }

                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = onClearQuery,
                                modifier = Modifier
                                    .size(24.dp)
                                    .testTag("tag_search_clear_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.search_clear_query),
                                    tint = NeobrutalismTheme.colors.onSurface,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Cancel Button
                NeobrutalistButton(
                    onClick = onCancelClick,
                    shape = RoundedCornerShape(12.dp),
                    borderWidth = 2.dp,
                    shadowOffset = 3.dp,
                    containerColor = NeobrutalismTheme.colors.surface,
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("tag_search_cancel_button")
                ) {
                    Text(
                        text = stringResource(R.string.search_cancel),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeobrutalismTheme.colors.onSurface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Body: Discovery Mode vs Active Search Mode
            if (!uiState.isSearching) {
                // Discovery Mode
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    YourLibraryCard(
                        stats = uiState.libraryStats,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (uiState.recentlySavedBookmarks.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(20.dp))

                        RecentlySavedSection(
                            bookmarks = uiState.recentlySavedBookmarks,
                            collectionsMap = uiState.collectionsMap,
                            onBookmarkClick = onBookmarkClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    SearchIdlePrompt(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = if (uiState.recentlySavedBookmarks.isEmpty()) 48.dp else 24.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                // Active Search Mode
                if (uiState.searchResults.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        SearchEmptyState(
                            searchQuery = uiState.searchQuery,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    SearchResultsList(
                        searchResults = uiState.searchResults,
                        collectionsMap = uiState.collectionsMap,
                        onBookmarkClick = onBookmarkClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
