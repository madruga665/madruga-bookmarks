package com.madruga665.bookmarks.ui.search

import com.madruga665.bookmarks.ui.theme.LightAccentYellow
import com.madruga665.bookmarks.ui.theme.LightBackground
import com.madruga665.bookmarks.ui.theme.LightBorder
import com.madruga665.bookmarks.ui.theme.LightNeobrutalismColors
import com.madruga665.bookmarks.ui.theme.LightOnSurface
import com.madruga665.bookmarks.ui.theme.LightShadow
import com.madruga665.bookmarks.ui.theme.LightSubtext
import com.madruga665.bookmarks.ui.theme.LightSurface
import com.madruga665.bookmarks.ui.theme.MochaBase
import com.madruga665.bookmarks.ui.theme.MochaCrust
import com.madruga665.bookmarks.ui.theme.MochaDarkNeobrutalismColors
import com.madruga665.bookmarks.ui.theme.MochaSubtext0
import com.madruga665.bookmarks.ui.theme.MochaSurface0
import com.madruga665.bookmarks.ui.theme.MochaText
import com.madruga665.bookmarks.ui.theme.MochaYellow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class SearchThemeAndLocalizationTest {

    @Test
    fun lightThemeColors_properlyConfigured() {
        val colors = LightNeobrutalismColors

        assertEquals(LightBackground, colors.background)
        assertEquals(LightSurface, colors.surface)
        assertEquals(LightOnSurface, colors.onSurface)
        assertEquals(LightSubtext, colors.subtext)
        assertEquals(LightBorder, colors.border)
        assertEquals(LightShadow, colors.shadow)
        assertEquals(LightAccentYellow, colors.accentYellow)
        assertEquals(com.madruga665.bookmarks.ui.theme.LightGridLine, colors.gridLine)
    }

    @Test
    fun mochaDarkThemeColors_properlyConfigured() {
        val colors = MochaDarkNeobrutalismColors

        assertEquals(MochaBase, colors.background)
        assertEquals(MochaSurface0, colors.surface)
        assertEquals(MochaText, colors.onSurface)
        assertEquals(MochaSubtext0, colors.subtext)
        assertEquals(MochaCrust, colors.border)
        assertEquals(MochaCrust, colors.shadow)
        assertEquals(MochaYellow, colors.accentYellow)
        assertEquals(com.madruga665.bookmarks.ui.theme.MochaGridLine, colors.gridLine)
    }

    @Test
    fun searchStringResources_existInBothEnglishAndPortuguese() {
        val enFile = File("src/main/res/values/strings.xml")
            .takeIf { it.exists() } ?: File("app/src/main/res/values/strings.xml")
        val ptFile = File("src/main/res/values-pt-rBR/strings.xml")
            .takeIf { it.exists() } ?: File("app/src/main/res/values-pt-rBR/strings.xml")

        assertTrue("English strings file should exist", enFile.exists())
        assertTrue("Portuguese strings file should exist", ptFile.exists())

        val enContent = enFile.readText()
        val ptContent = ptFile.readText()

        val expectedSearchKeys = listOf(
            "search_placeholder",
            "search_cancel",
            "search_your_library",
            "search_collections",
            "search_links",
            "search_pinned",
            "search_tags",
            "search_recently_saved",
            "search_idle_prompt",
            "search_no_results",
            "search_no_results_fmt",
            "search_results_count_fmt",
            "search_clear_query"
        )

        for (key in expectedSearchKeys) {
            val enRegex = Regex("""<string name="$key">(.*?)</string>""")
            val ptRegex = Regex("""<string name="$key">(.*?)</string>""")

            val enMatch = enRegex.find(enContent)
            val ptMatch = ptRegex.find(ptContent)

            assertNotNull("Key '$key' must exist in English strings.xml", enMatch)
            assertNotNull("Key '$key' must exist in Portuguese strings.xml", ptMatch)

            val enValue = enMatch!!.groupValues[1]
            val ptValue = ptMatch!!.groupValues[1]

            assertTrue("English value for '$key' cannot be blank", enValue.isNotBlank())
            assertTrue("Portuguese value for '$key' cannot be blank", ptValue.isNotBlank())

            // Format parity check
            if (key.endsWith("_fmt")) {
                val enSpecifiers = Regex("""%[0-9]*\$?[a-zA-Z]""").findAll(enValue).map { it.value }.toList()
                val ptSpecifiers = Regex("""%[0-9]*\$?[a-zA-Z]""").findAll(ptValue).map { it.value }.toList()
                assertEquals(
                    "Format specifier count and type for key '$key' must match between EN and PT",
                    enSpecifiers,
                    ptSpecifiers
                )
            }
        }
    }
}
