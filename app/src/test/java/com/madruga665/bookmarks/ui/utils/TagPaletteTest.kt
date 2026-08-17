package com.madruga665.bookmarks.ui.utils

import androidx.compose.ui.graphics.Color
import com.madruga665.bookmarks.data.local.BookmarkEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TagPaletteTest {

    @Test
    fun getTagColor_returnsDeterministicColor_forSameTagName() {
        val color1 = TagPalette.getTagColor("kotlin")
        val color2 = TagPalette.getTagColor("kotlin")
        assertEquals(color1, color2)
        assertTrue(TagPalette.colors.contains(color1))
    }

    @Test
    fun getTagColor_handlesEmptyAndBlankStrings() {
        val defaultColor = TagPalette.colors.first()
        assertEquals(defaultColor, TagPalette.getTagColor(""))
        assertEquals(defaultColor, TagPalette.getTagColor("   "))
        assertEquals(defaultColor, TagPalette.getTagColor("#"))
        assertEquals(defaultColor, TagPalette.getTagColor("  #  "))
    }

    @Test
    fun getTagColor_handlesHashPrefixAndCaseInsensitivity() {
        val colorRaw = TagPalette.getTagColor("android")
        val colorWithHash = TagPalette.getTagColor("#android")
        val colorUpperCase = TagPalette.getTagColor("ANDROID")
        val colorMixedWithHashAndSpaces = TagPalette.getTagColor("  #AnDrOiD  ")

        assertEquals(colorRaw, colorWithHash)
        assertEquals(colorRaw, colorUpperCase)
        assertEquals(colorRaw, colorMixedWithHashAndSpaces)
    }

    @Test
    fun tagItem_displayName_addsHashPrefixWhenMissing() {
        val tagWithoutHash = TagItem(name = "compose", color = Color.Red, count = 5)
        assertEquals("#compose", tagWithoutHash.displayName)

        val tagWithHash = TagItem(name = "#compose", color = Color.Red, count = 5)
        assertEquals("#compose", tagWithHash.displayName)
    }

    @Test
    fun bookmarkEntity_tagList_splitsAndNormalizes() {
        val bookmark = BookmarkEntity(
            id = "b1",
            url = "https://example.com",
            title = "Test",
            faviconUrl = null,
            tags = " Kotlin, #Android , JETPACK, , #compose,  ",
            createdAt = System.currentTimeMillis()
        )

        val tags = bookmark.tagList
        assertEquals(listOf("kotlin", "android", "jetpack", "compose"), tags)
    }

    @Test
    fun bookmarkEntity_tagList_withEmptyString_returnsEmptyList() {
        val bookmark = BookmarkEntity(
            id = "b1",
            url = "https://example.com",
            title = "Test",
            faviconUrl = null,
            tags = "",
            createdAt = System.currentTimeMillis()
        )

        assertTrue(bookmark.tagList.isEmpty())
    }

    @Test
    fun toTagString_normalizesDeduplicatesCapsAt10AndJoins() {
        val inputList = listOf(
            " Kotlin ",
            "#kotlin",
            "ANDROID",
            "#android",
            "compose",
            "jetpack",
            "room",
            "hilt",
            "coroutines",
            "flow",
            "navigation",
            "material3",
            "design",
            "",
            "   ",
            "#"
        )

        val result = inputList.toTagString()

        // 10 distinct normalized tags capped
        val expectedTags = listOf(
            "kotlin",
            "android",
            "compose",
            "jetpack",
            "room",
            "hilt",
            "coroutines",
            "flow",
            "navigation",
            "material3"
        )
        val expectedString = expectedTags.joinToString(",")

        assertEquals(expectedString, result)
        assertEquals(10, result.split(",").size)
    }
}
