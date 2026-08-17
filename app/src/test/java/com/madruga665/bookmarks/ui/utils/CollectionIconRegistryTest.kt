package com.madruga665.bookmarks.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CollectionIconRegistryTest {

    @Test
    fun icons_contains43CuratedIcons() {
        assertEquals(43, CollectionIconRegistry.icons.size)
        val keys = CollectionIconRegistry.icons.map { it.key }
        assertEquals(43, keys.distinct().size)
    }

    @Test
    fun defaultIcon_isFolder() {
        assertEquals("folder", CollectionIconRegistry.defaultIcon.key)
        assertEquals(Icons.Outlined.Folder, CollectionIconRegistry.defaultIcon.icon)
    }

    @Test
    fun getIcon_withKnownKeys_returnsExpectedIcon() {
        assertEquals(Icons.Outlined.Folder, CollectionIconRegistry.getIcon("folder"))
        assertEquals(Icons.Outlined.StarOutline, CollectionIconRegistry.getIcon("star"))
        assertEquals(Icons.Outlined.FavoriteBorder, CollectionIconRegistry.getIcon("heart"))
        assertEquals(Icons.Outlined.Code, CollectionIconRegistry.getIcon("code"))
        assertEquals(Icons.Outlined.Home, CollectionIconRegistry.getIcon("home"))
        assertEquals(Icons.Outlined.WorkOutline, CollectionIconRegistry.getIcon("luggage"))
    }

    @Test
    fun getIcon_isCaseInsensitiveAndTrims() {
        assertEquals(Icons.Outlined.Code, CollectionIconRegistry.getIcon("CODE"))
        assertEquals(Icons.Outlined.StarOutline, CollectionIconRegistry.getIcon("  Star  "))
        assertEquals(Icons.Outlined.Folder, CollectionIconRegistry.getIcon("Folder"))
    }

    @Test
    fun getIcon_withAliases_returnsMappedIcon() {
        assertEquals(Icons.Outlined.Code, CollectionIconRegistry.getIcon("programacao"))
        assertEquals(Icons.Outlined.WorkOutline, CollectionIconRegistry.getIcon("vagas"))
        assertEquals(Icons.Outlined.WorkOutline, CollectionIconRegistry.getIcon("work"))
        assertEquals(Icons.Outlined.Flight, CollectionIconRegistry.getIcon("plane"))
        assertEquals(Icons.Outlined.Flight, CollectionIconRegistry.getIcon("airplane"))
        assertEquals(Icons.Outlined.ShoppingCart, CollectionIconRegistry.getIcon("cart"))
        assertEquals(Icons.Outlined.LocalOffer, CollectionIconRegistry.getIcon("label"))
        assertEquals(Icons.Outlined.ViewInAr, CollectionIconRegistry.getIcon("view_in_ar"))
    }

    @Test
    fun getIcon_withUnknownOrNullOrBlank_returnsDefaultFolder() {
        assertEquals(Icons.Outlined.Folder, CollectionIconRegistry.getIcon(null))
        assertEquals(Icons.Outlined.Folder, CollectionIconRegistry.getIcon(""))
        assertEquals(Icons.Outlined.Folder, CollectionIconRegistry.getIcon("   "))
        assertEquals(Icons.Outlined.Folder, CollectionIconRegistry.getIcon("non_existent_key_123"))
    }
}
