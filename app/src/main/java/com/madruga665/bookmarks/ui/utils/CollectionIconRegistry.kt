package com.madruga665.bookmarks.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents an icon selectable for a collection / folder.
 */
data class CollectionIconItem(
    val key: String,
    val icon: ImageVector
)

/**
 * Centralized registry mapping 43 curated category icons for collections.
 */
object CollectionIconRegistry {

    val icons: List<CollectionIconItem> = listOf(
        CollectionIconItem("folder", Icons.Outlined.Folder),
        CollectionIconItem("star", Icons.Outlined.StarOutline),
        CollectionIconItem("heart", Icons.Outlined.FavoriteBorder),
        CollectionIconItem("book", Icons.Outlined.MenuBook),
        CollectionIconItem("music", Icons.Outlined.MusicNote),
        CollectionIconItem("camera", Icons.Outlined.PhotoCamera),
        CollectionIconItem("flag", Icons.Outlined.Flag),
        CollectionIconItem("moon", Icons.Outlined.DarkMode),
        CollectionIconItem("sun", Icons.Outlined.WbSunny),
        CollectionIconItem("cloud", Icons.Outlined.Cloud),
        CollectionIconItem("pin", Icons.Outlined.LocationOn),
        CollectionIconItem("calendar", Icons.Outlined.CalendarToday),
        CollectionIconItem("globe", Icons.Outlined.Language),
        CollectionIconItem("gift", Icons.Outlined.CardGiftcard),
        CollectionIconItem("leaf", Icons.Outlined.Eco),
        CollectionIconItem("luggage", Icons.Outlined.WorkOutline),
        CollectionIconItem("shopping_cart", Icons.Outlined.ShoppingCart),
        CollectionIconItem("flight", Icons.Outlined.Flight),
        CollectionIconItem("car", Icons.Outlined.DirectionsCar),
        CollectionIconItem("coffee", Icons.Outlined.LocalCafe),
        CollectionIconItem("film", Icons.Outlined.Movie),
        CollectionIconItem("headphones", Icons.Outlined.Headphones),
        CollectionIconItem("palette", Icons.Outlined.Palette),
        CollectionIconItem("gamepad", Icons.Outlined.SportsEsports),
        CollectionIconItem("dumbbell", Icons.Outlined.FitnessCenter),
        CollectionIconItem("dollar", Icons.Outlined.AttachMoney),
        CollectionIconItem("phone", Icons.Outlined.Call),
        CollectionIconItem("computer", Icons.Outlined.DesktopWindows),
        CollectionIconItem("clock", Icons.Outlined.AccessTime),
        CollectionIconItem("lightbulb", Icons.Outlined.Lightbulb),
        CollectionIconItem("school", Icons.Outlined.School),
        CollectionIconItem("shield", Icons.Outlined.Shield),
        CollectionIconItem("restaurant", Icons.Outlined.Restaurant),
        CollectionIconItem("tv", Icons.Outlined.Tv),
        CollectionIconItem("bell", Icons.Outlined.Notifications),
        CollectionIconItem("key", Icons.Outlined.VpnKey),
        CollectionIconItem("cube", Icons.Outlined.ViewInAr),
        CollectionIconItem("layers", Icons.Outlined.Layers),
        CollectionIconItem("code", Icons.Outlined.Code),
        CollectionIconItem("bolt", Icons.Outlined.Bolt),
        CollectionIconItem("bookmark", Icons.Outlined.BookmarkBorder),
        CollectionIconItem("tag", Icons.Outlined.LocalOffer),
        CollectionIconItem("home", Icons.Outlined.Home)
    )

    val defaultIcon: CollectionIconItem = icons.first()

    private val iconMap: Map<String, ImageVector> = icons.associate { it.key to it.icon }

    private val aliases: Map<String, String> = mapOf(
        "programacao" to "code",
        "vagas" to "luggage",
        "work" to "luggage",
        "plane" to "flight",
        "airplane" to "flight",
        "label" to "tag",
        "view_in_ar" to "cube",
        "cart" to "shopping_cart",
        "favorite" to "heart",
        "money" to "dollar",
        "time" to "clock",
        "pushpin" to "pin"
    )

    /**
     * Resolves an ImageVector by its string key, case-insensitively,
     * supporting aliases and defaulting to Icons.Outlined.Folder for unknown or null keys.
     */
    fun getIcon(key: String?): ImageVector {
        if (key.isNullOrBlank()) return defaultIcon.icon
        val normalizedKey = key.trim().lowercase()
        val mappedKey = aliases[normalizedKey] ?: normalizedKey
        return iconMap[mappedKey] ?: defaultIcon.icon
    }
}
