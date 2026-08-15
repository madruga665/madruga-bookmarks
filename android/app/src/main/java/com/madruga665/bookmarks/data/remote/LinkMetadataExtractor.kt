package com.madruga665.bookmarks.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URI

data class LinkMetadata(
    val title: String?,
    val faviconUrl: String?,
    val thumbnailUrl: String?,
    val sourcePlatform: String?,
    val description: String? = null
)

object LinkMetadataExtractor {

    suspend fun extractMetadata(url: String): LinkMetadata = withContext(Dispatchers.IO) {
        val cleanUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else url

        val domain = try {
            val uri = URI(cleanUrl)
            uri.host?.removePrefix("www.") ?: ""
        } catch (e: Exception) {
            ""
        }

        val defaultFavicon = if (domain.isNotBlank()) {
            "https://www.google.com/s2/favicons?domain=$domain&sz=128"
        } else null

        val defaultPlatform = if (domain.isNotBlank()) {
            formatDomainToPlatformName(domain)
        } else null

        try {
            val doc = Jsoup.connect(cleanUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .timeout(10000)
                .get()

            val ogTitle = doc.select("meta[property=og:title]").attr("content").takeIf { it.isNotBlank() }
            val twitterTitle = doc.select("meta[property=og:title]").attr("content").takeIf { it.isNotBlank() }
            val pageTitle = doc.title().takeIf { it.isNotBlank() }
            val title = ogTitle ?: twitterTitle ?: pageTitle ?: defaultPlatform

            val ogDescription = doc.select("meta[property=og:description]").attr("content").takeIf { it.isNotBlank() }
            val twitterDescription = doc.select("meta[name=twitter:description]").attr("content").takeIf { it.isNotBlank() }
            val metaDescription = doc.select("meta[name=description]").attr("content").takeIf { it.isNotBlank() }
            val description = ogDescription ?: twitterDescription ?: metaDescription

            val ogImage = doc.select("meta[property=og:image]").attr("content").takeIf { it.isNotBlank() }
            val twitterImage = doc.select("meta[property=og:image]").attr("content").takeIf { it.isNotBlank() }
            val thumbnailUrl = ogImage ?: twitterImage

            val ogSiteName = doc.select("meta[property=og:site_name]").attr("content").takeIf { it.isNotBlank() }
            val sourcePlatform = if (!ogSiteName.isNullOrBlank()) "@$ogSiteName" else defaultPlatform

            val iconHref = doc.select("link[rel~=(?i)^(shortcut )?icon]").attr("abs:href").takeIf { it.isNotBlank() }
            val faviconUrl = iconHref ?: defaultFavicon

            LinkMetadata(
                title = title,
                faviconUrl = faviconUrl,
                thumbnailUrl = thumbnailUrl,
                sourcePlatform = sourcePlatform,
                description = description
            )
        } catch (e: Exception) {
            LinkMetadata(
                title = defaultPlatform,
                faviconUrl = defaultFavicon,
                thumbnailUrl = null,
                sourcePlatform = defaultPlatform,
                description = null
            )
        }
    }

    private fun formatDomainToPlatformName(domain: String): String {
        val parts = domain.split(".")
        val name = if (parts.size >= 2) parts[parts.size - 2] else domain
        return "@" + name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
