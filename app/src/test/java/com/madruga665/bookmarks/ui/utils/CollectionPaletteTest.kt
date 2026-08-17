package com.madruga665.bookmarks.ui.utils

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CollectionPaletteTest {

    @Test
    fun colors_containsSixteenCuratedColors() {
        assertEquals(16, CollectionPalette.colors.size)
        assertEquals("yellow", CollectionPalette.colors[0].id)
        assertEquals("#FFE600", CollectionPalette.colors[0].hex)
        assertEquals(Color(0xFFFFE600), CollectionPalette.colors[0].color)

        assertEquals("indigo", CollectionPalette.colors[15].id)
        assertEquals("#5352ED", CollectionPalette.colors[15].hex)
        assertEquals(Color(0xFF5352ED), CollectionPalette.colors[15].color)
    }

    @Test
    fun defaultColor_isFirstColorYellow() {
        assertEquals(CollectionPalette.colors.first(), CollectionPalette.defaultColor)
        assertEquals("yellow", CollectionPalette.defaultColor.id)
        assertEquals("#FFE600", CollectionPalette.defaultColor.hex)
        assertEquals(Color(0xFFFFE600), CollectionPalette.defaultColor.color)
    }

    @Test
    fun getColor_resolvesByIdCaseInsensitively() {
        assertEquals(Color(0xFFFFE600), CollectionPalette.getColor("yellow"))
        assertEquals(Color(0xFFFFE600), CollectionPalette.getColor("YELLOW"))
        assertEquals(Color(0xFFFF4B8B), CollectionPalette.getColor("pink"))
        assertEquals(Color(0xFF9B51E0), CollectionPalette.getColor("Purple"))
        assertEquals(Color(0xFF1E1E1E), CollectionPalette.getColor("dark_slate"))
        assertEquals(Color(0xFF6C88A8), CollectionPalette.getColor("slate_blue"))
    }

    @Test
    fun getColor_resolvesByHexWithOrWithoutHash() {
        assertEquals(Color(0xFFFFE600), CollectionPalette.getColor("#FFE600"))
        assertEquals(Color(0xFFFFE600), CollectionPalette.getColor("FFE600"))
        assertEquals(Color(0xFFFF4B8B), CollectionPalette.getColor("#ff4b8b"))
        assertEquals(Color(0xFF5352ED), CollectionPalette.getColor("5352ed"))
    }

    @Test
    fun getColor_resolvesCustomHex() {
        assertEquals(Color(0xFF123456), CollectionPalette.getColor("#123456"))
        assertEquals(Color(0xFFAABBCC), CollectionPalette.getColor("AABBCC"))
    }

    @Test
    fun getColor_withNullOrBlankOrUnknown_returnsDefaultColor() {
        assertEquals(CollectionPalette.defaultColor.color, CollectionPalette.getColor(null))
        assertEquals(CollectionPalette.defaultColor.color, CollectionPalette.getColor(""))
        assertEquals(CollectionPalette.defaultColor.color, CollectionPalette.getColor("   "))
        assertEquals(CollectionPalette.defaultColor.color, CollectionPalette.getColor("non_existent_color_name"))
        assertEquals(CollectionPalette.defaultColor.color, CollectionPalette.getColor("invalid_hex_xyz"))
    }

    @Test
    fun getHex_returnsPredefinedHexForPaletteColors() {
        assertEquals("#FFE600", CollectionPalette.getHex(Color(0xFFFFE600)))
        assertEquals("#FF4B8B", CollectionPalette.getHex(Color(0xFFFF4B8B)))
        assertEquals("#5352ED", CollectionPalette.getHex(Color(0xFF5352ED)))
    }

    @Test
    fun getHex_formatsArbitraryColorAsHex() {
        assertEquals("#112233", CollectionPalette.getHex(Color(0xFF112233)))
    }
}
