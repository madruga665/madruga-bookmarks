package com.madruga665.bookmarks.ui.utils

import androidx.compose.ui.graphics.Color
import java.util.Locale

/**
 * Data class representing a curated color item in the Neobrutalism collection palette.
 */
data class CollectionColorItem(
    val id: String,
    val hex: String,
    val color: Color
)

/**
 * Centralized palette registry defining the 16 curated Neobrutalism colors for collection folders.
 */
object CollectionPalette {

    val colors: List<CollectionColorItem> = listOf(
        CollectionColorItem("yellow", "#FFE600", Color(0xFFFFE600)),
        CollectionColorItem("pink", "#FF4B8B", Color(0xFFFF4B8B)),
        CollectionColorItem("purple", "#9B51E0", Color(0xFF9B51E0)),
        CollectionColorItem("blue", "#2F80ED", Color(0xFF2F80ED)),
        CollectionColorItem("mint", "#00C49F", Color(0xFF00C49F)),
        CollectionColorItem("green", "#48BB78", Color(0xFF48BB78)),
        CollectionColorItem("lime", "#A0E040", Color(0xFFA0E040)),
        CollectionColorItem("orange", "#FF7700", Color(0xFFFF7700)),
        CollectionColorItem("sand", "#FDE5A9", Color(0xFFFDE5A9)),
        CollectionColorItem("gray", "#A0AEC0", Color(0xFFA0AEC0)),
        CollectionColorItem("slate_blue", "#6C88A8", Color(0xFF6C88A8)),
        CollectionColorItem("mauve", "#BA68C8", Color(0xFFBA68C8)),
        CollectionColorItem("brown", "#9C6644", Color(0xFF9C6644)),
        CollectionColorItem("dark_slate", "#1E1E1E", Color(0xFF1E1E1E)),
        CollectionColorItem("coral", "#FF6B6B", Color(0xFFFF6B6B)),
        CollectionColorItem("indigo", "#5352ED", Color(0xFF5352ED))
    )

    val defaultColor: CollectionColorItem = colors.first()

    /**
     * Resolves a color by hex or id/name case-insensitively, handling '#' prefixes,
     * falling back to [defaultColor.color] if not found or invalid.
     */
    fun getColor(hexOrName: String?): Color {
        if (hexOrName.isNullOrBlank()) return defaultColor.color

        val clean = hexOrName.trim()

        // 1. Match by id case-insensitively (e.g. "yellow", "dark_slate", "slate_blue")
        colors.firstOrNull { it.id.equals(clean, ignoreCase = true) }?.let {
            return it.color
        }

        // 2. Match by hex (with or without '#')
        val cleanHex = if (clean.startsWith("#")) clean else "#$clean"
        colors.firstOrNull { it.hex.equals(cleanHex, ignoreCase = true) }?.let {
            return it.color
        }

        // 3. Fallback: Parse custom hex if valid 6 or 8 character hex string
        return parseHexColor(clean) ?: defaultColor.color
    }

    /**
     * Returns the hex string representation of the given Compose [Color].
     * If the color matches a palette item, returns its predefined hex; otherwise formats as #RRGGBB.
     */
    fun getHex(color: Color): String {
        colors.firstOrNull { it.color == color }?.let {
            return it.hex
        }
        val red = (color.red * 255).toInt().coerceIn(0, 255)
        val green = (color.green * 255).toInt().coerceIn(0, 255)
        val blue = (color.blue * 255).toInt().coerceIn(0, 255)
        return String.format(Locale.ROOT, "#%02X%02X%02X", red, green, blue)
    }

    private fun parseHexColor(hexString: String): Color? {
        val raw = hexString.removePrefix("#").trim()
        return when (raw.length) {
            6 -> {
                val colorInt = raw.toLongOrNull(16) ?: return null
                Color(0xFF000000 or colorInt)
            }
            8 -> {
                val colorInt = raw.toLongOrNull(16) ?: return null
                Color(colorInt)
            }
            else -> null
        }
    }
}
