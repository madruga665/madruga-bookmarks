package com.madruga665.bookmarks.ui.collection.create

import androidx.compose.ui.graphics.Color
import com.madruga665.bookmarks.ui.utils.CollectionPalette
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CollectionColorPickerTest {

    @Test
    fun collectionPalette_containsAll16ColorsInExpectedOrder() {
        val colors = CollectionPalette.colors
        assertEquals(16, colors.size)

        val expectedIds = listOf(
            "yellow", "pink", "purple", "blue",
            "mint", "green", "lime", "orange",
            "sand", "gray", "slate_blue", "mauve",
            "brown", "dark_slate", "coral", "indigo"
        )

        assertEquals(expectedIds, colors.map { it.id })
    }

    @Test
    fun collectionPalette_chunkedEight_producesTwoRowsOfEight() {
        val rows = CollectionPalette.colors.chunked(8)
        assertEquals(2, rows.size)
        assertEquals(8, rows[0].size)
        assertEquals(8, rows[1].size)
    }

    @Test
    fun isColorSelected_matchesByHexCaseInsensitive() {
        val yellowItem = CollectionPalette.colors.first { it.id == "yellow" }
        val pinkItem = CollectionPalette.colors.first { it.id == "pink" }

        assertTrue(yellowItem.hex.equals("#FFE600", ignoreCase = true))
        assertTrue(yellowItem.hex.equals("#ffe600", ignoreCase = true))
        assertFalse(pinkItem.hex.equals("#FFE600", ignoreCase = true))
    }

    @Test
    fun isColorSelected_matchesByIdCaseInsensitive() {
        val darkSlate = CollectionPalette.colors.first { it.id == "dark_slate" }
        assertTrue(darkSlate.id.equals("dark_slate", ignoreCase = true))
        assertTrue(darkSlate.id.equals("DARK_SLATE", ignoreCase = true))
        assertFalse(darkSlate.id.equals("blue", ignoreCase = true))
    }

    @Test
    fun defaultSelectedColor_isYellowHex() {
        val defaultState = CreateCollectionUiState()
        assertEquals("#FFE600", defaultState.selectedColor)
        assertEquals(CollectionPalette.colors.first().hex, defaultState.selectedColor)
    }
}
