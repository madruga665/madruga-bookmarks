package com.madruga665.bookmarks.ui.utils

import androidx.compose.ui.graphics.Color
import com.madruga665.bookmarks.data.local.BookmarkEntity
import kotlin.math.abs

data class TagItem(
    val name: String,
    val color: Color,
    val count: Int = 0
) {
    val displayName: String
        get() = if (name.startsWith("#")) name else "#$name"
}

object TagPalette {
    val colors = listOf(
        Color(0xFFFFE600), // Yellow
        Color(0xFFFF4B8B), // Pink
        Color(0xFF9B51E0), // Purple
        Color(0xFF2F80ED), // Blue
        Color(0xFF00C49F), // Mint
        Color(0xFFA0E040), // Lime
        Color(0xFFFF7700), // Orange
        Color(0xFFBA68C8), // Mauve
        Color(0xFF6C88A8), // Slate Blue
        Color(0xFFFF6B6B)  // Coral
    )

    fun getTagColor(tagName: String): Color {
        val clean = tagName.trim().removePrefix("#").lowercase()
        if (clean.isBlank()) return colors.first()
        val index = abs(clean.hashCode()) % colors.size
        return colors[index]
    }
}

val BookmarkEntity.tagList: List<String>
    get() = tags.split(",")
        .map { it.trim().removePrefix("#").lowercase().replace(" ", "-") }
        .filter { it.isNotBlank() }

fun List<String>.toTagString(): String =
    this.map { it.trim().removePrefix("#").lowercase().replace(" ", "-") }
        .filter { it.isNotBlank() }
        .distinct()
        .take(10)
        .joinToString(",")
