package com.madruga665.bookmarks.ui.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class UrlUtilsTest {

    @Test
    fun normalizeUrl_withEmptyOrNull_returnsEmpty() {
        assertEquals("", UrlUtils.normalizeUrl(null))
        assertEquals("", UrlUtils.normalizeUrl(""))
        assertEquals("", UrlUtils.normalizeUrl("   "))
    }

    @Test
    fun normalizeUrl_withAlreadyValidHttps_returnsTrimmedUrl() {
        assertEquals("https://github.com/madruga665", UrlUtils.normalizeUrl("https://github.com/madruga665"))
        assertEquals("https://github.com/madruga665", UrlUtils.normalizeUrl("  https://github.com/madruga665  "))
    }

    @Test
    fun normalizeUrl_withAlreadyValidHttp_returnsTrimmedUrl() {
        assertEquals("http://example.com", UrlUtils.normalizeUrl("http://example.com"))
        assertEquals("http://example.com/page", UrlUtils.normalizeUrl("  http://example.com/page  "))
    }

    @Test
    fun normalizeUrl_withoutScheme_prependsHttps() {
        assertEquals("https://github.com/torvalds/linux", UrlUtils.normalizeUrl("github.com/torvalds/linux"))
        assertEquals("https://www.google.com", UrlUtils.normalizeUrl("www.google.com"))
        assertEquals("https://t.co/abc123xyz", UrlUtils.normalizeUrl("   t.co/abc123xyz  "))
    }
}
