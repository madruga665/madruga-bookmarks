package com.madruga665.bookmarks.data.remote

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LinkMetadataExtractorTest {

    @Test
    fun extractMetadata_handlesInvalidUrl_returnsFallback() = runTest {
        val result = LinkMetadataExtractor.extractMetadata("not-a-valid-url")
        assertNotNull(result)
        assertNotNull(result.title)
    }

    @Test
    fun extractMetadata_handlesTwitterDomainFallback() = runTest {
        val result = LinkMetadataExtractor.extractMetadata("https://x.com/devemdobro")
        assertNotNull(result)
        assertEquals("@X", result.sourcePlatform)
        assertTrue(result.title?.contains("devemdobro") == true || result.title?.contains("X") == true)
    }

    @Test
    fun extractMetadata_handlesInstagramFallback() = runTest {
        val result = LinkMetadataExtractor.extractMetadata("https://www.instagram.com/devemdobro")
        assertNotNull(result)
        assertEquals("@Instagram", result.sourcePlatform)
        assertNotNull(result.thumbnailUrl)
    }

    @Test
    fun extractMetadata_handlesLinkedInFallback() = runTest {
        val result = LinkMetadataExtractor.extractMetadata("https://www.linkedin.com/in/john-doe")
        assertNotNull(result)
        assertEquals("@LinkedIn", result.sourcePlatform)
        assertTrue(result.title?.contains("LinkedIn") == true || result.title?.contains("john-doe") == true)
    }

    @Test
    fun extractMetadata_handlesGitHubFallback() = runTest {
        val result = LinkMetadataExtractor.extractMetadata("https://github.com/google/dagger")
        assertNotNull(result)
        assertEquals("@GitHub", result.sourcePlatform)
        assertTrue(result.title?.contains("dagger") == true)
    }

    @Test
    fun formatDomainToPlatformName_formatsCorrectly() {
        assertEquals("@X", LinkMetadataExtractor.formatDomainToPlatformName("x.com"))
        assertEquals("@X", LinkMetadataExtractor.formatDomainToPlatformName("twitter.com"))
        assertEquals("@Instagram", LinkMetadataExtractor.formatDomainToPlatformName("instagram.com"))
        assertEquals("@LinkedIn", LinkMetadataExtractor.formatDomainToPlatformName("linkedin.com"))
        assertEquals("@Facebook", LinkMetadataExtractor.formatDomainToPlatformName("facebook.com"))
        assertEquals("@YouTube", LinkMetadataExtractor.formatDomainToPlatformName("youtube.com"))
        assertEquals("@GitHub", LinkMetadataExtractor.formatDomainToPlatformName("github.com"))
        assertEquals("@Medium", LinkMetadataExtractor.formatDomainToPlatformName("medium.com"))
        assertEquals("@Reddit", LinkMetadataExtractor.formatDomainToPlatformName("reddit.com"))
    }
}
