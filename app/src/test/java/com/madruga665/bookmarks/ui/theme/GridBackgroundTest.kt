package com.madruga665.bookmarks.ui.theme

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class GridBackgroundTest {

    @Test
    fun lightNeobrutalismColors_gridLine_matchesExpectedColor() {
        assertEquals(Color(0xFFE2E2E2), LightNeobrutalismColors.gridLine)
    }

    @Test
    fun mochaDarkNeobrutalismColors_gridLine_matchesExpectedColor() {
        assertEquals(Color(0xFF28283D), MochaDarkNeobrutalismColors.gridLine)
    }

    @Test
    fun gridLine_isDistinctFromBackground_inLightAndDarkThemes() {
        assertNotEquals(
            "Light gridLine must be distinct from Light background",
            LightNeobrutalismColors.background,
            LightNeobrutalismColors.gridLine
        )
        assertNotEquals(
            "Dark gridLine must be distinct from Dark background",
            MochaDarkNeobrutalismColors.background,
            MochaDarkNeobrutalismColors.gridLine
        )
    }

    @Test
    fun gridLine_isDistinctFromSurfaceAndBorder_inBothThemes() {
        assertNotEquals(LightNeobrutalismColors.surface, LightNeobrutalismColors.gridLine)
        assertNotEquals(LightNeobrutalismColors.border, LightNeobrutalismColors.gridLine)
        assertNotEquals(MochaDarkNeobrutalismColors.surface, MochaDarkNeobrutalismColors.gridLine)
        assertNotEquals(MochaDarkNeobrutalismColors.border, MochaDarkNeobrutalismColors.gridLine)
    }
}
