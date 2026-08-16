package com.madruga665.bookmarks.data.repository

import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeRepositoryTest {

    @Test
    fun themeModeEnum_valuesAreCorrect() {
        assertEquals("LIGHT", AppThemeMode.LIGHT.name)
        assertEquals("CATPPUCCIN_MOCHA", AppThemeMode.CATPPUCCIN_MOCHA.name)
        assertEquals("SYSTEM", AppThemeMode.SYSTEM.name)
    }
}
