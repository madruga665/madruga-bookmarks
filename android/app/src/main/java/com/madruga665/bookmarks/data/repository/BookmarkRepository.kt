package com.madruga665.bookmarks.data.repository

import com.madruga665.bookmarks.data.local.BookmarkDao
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.remote.LinkMetadataExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class BookmarkRepository(
    private val bookmarkDao: BookmarkDao
) {
    private var isSeeded = false

    val allBookmarks: Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks().map { list ->
        if (list.isEmpty() && !isSeeded) {
            isSeeded = true
            seedDefaultBookmarks()
            defaultSampleBookmarks()
        } else {
            isSeeded = true
            list
        }
    }

    fun getBookmarksByCollection(collectionId: String): Flow<List<BookmarkEntity>> =
        bookmarkDao.getBookmarksByCollection(collectionId).map { list ->
            if (list.isEmpty() && collectionId == "col_ia" && !isSeeded) {
                isSeeded = true
                seedDefaultBookmarks()
                defaultSampleBookmarks().filter { it.collectionId == collectionId }
            } else {
                list
            }
        }

    suspend fun getBookmarksForCollection(collectionId: String): List<BookmarkEntity> = withContext(Dispatchers.IO) {
        val list = bookmarkDao.getBookmarksByCollectionList(collectionId)
        if (list.isEmpty() && collectionId == "col_ia") {
            seedDefaultBookmarks()
            defaultSampleBookmarks().filter { it.collectionId == collectionId }
        } else {
            list
        }
    }

    suspend fun quickSaveBookmark(
        url: String,
        collectionId: String = "col_unsorted",
        isPinned: Boolean = false
    ): Boolean = withContext(Dispatchers.IO) {
        val trimmedUrl = url.trim()
        if (trimmedUrl.isBlank() || (!trimmedUrl.startsWith("http://") && !trimmedUrl.startsWith("https://"))) {
            return@withContext false
        }

        val metadata = LinkMetadataExtractor.extractMetadata(trimmedUrl)

        val entity = BookmarkEntity(
            id = UUID.randomUUID().toString(),
            url = trimmedUrl,
            title = metadata.title ?: parseTitleFromUrlFallback(trimmedUrl),
            faviconUrl = metadata.faviconUrl,
            thumbnailUrl = metadata.thumbnailUrl ?: generateFallbackThumbnailUrl(trimmedUrl),
            sourcePlatform = metadata.sourcePlatform ?: "Web",
            collectionId = if (collectionId.isBlank()) "col_unsorted" else collectionId,
            isPinned = isPinned,
            createdAt = System.currentTimeMillis(),
            syncStatus = "PENDING_SYNC"
        )
        bookmarkDao.insertBookmark(entity)
        return@withContext true
    }

    private suspend fun seedDefaultBookmarks() {
        withContext(Dispatchers.IO) {
            defaultSampleBookmarks().forEach { bookmarkDao.insertBookmark(it) }
        }
    }

    private fun defaultSampleBookmarks(): List<BookmarkEntity> = listOf(
        BookmarkEntity(
            id = "bm_ia_1",
            url = "https://www.instagram.com/devemdobro",
            title = "Dev em dobro | Programação on Instagram",
            faviconUrl = "https://www.google.com/s2/favicons?domain=instagram.com&sz=128",
            thumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80",
            sourcePlatform = "Instagram",
            collectionId = "col_ia",
            isPinned = false,
            createdAt = System.currentTimeMillis(),
            syncStatus = "SYNCED"
        ),
        BookmarkEntity(
            id = "bm_ia_2",
            url = "https://www.instagram.com/devemdobro/p/sample",
            title = "Dev em dobro | Programação on Instagram",
            faviconUrl = "https://www.google.com/s2/favicons?domain=instagram.com&sz=128",
            thumbnailUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=600&auto=format&fit=crop&q=80",
            sourcePlatform = "Instagram",
            collectionId = "col_ia",
            isPinned = false,
            createdAt = System.currentTimeMillis() - 1000,
            syncStatus = "SYNCED"
        )
    )

    private fun parseTitleFromUrlFallback(url: String): String {
        return try {
            val uri = java.net.URI(url)
            val host = uri.host?.removePrefix("www.") ?: ""
            val pathParts = uri.path?.split("/")?.filter { it.isNotBlank() } ?: emptyList()

            when {
                host.contains("instagram.com", ignoreCase = true) -> {
                    val username = pathParts.firstOrNull { it != "p" && it != "reel" } ?: "devemdobro"
                    "${username.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }} | Programação on Instagram"
                }
                host.contains("github.com", ignoreCase = true) -> {
                    val repo = pathParts.take(2).joinToString("/")
                    if (repo.isNotBlank()) "$repo: GitHub Repository" else "GitHub"
                }
                else -> {
                    val cleanDomain = host.split(".").firstOrNull()?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } ?: "Web"
                    val firstPath = pathParts.firstOrNull()?.replace("-", " ")?.replace("_", " ")
                    if (firstPath != null) "$firstPath on $cleanDomain" else cleanDomain
                }
            }
        } catch (e: Exception) {
            url
        }
    }

    private fun generateFallbackThumbnailUrl(url: String): String {
        val cleanHost = try {
            java.net.URI(url).host?.removePrefix("www.") ?: "default"
        } catch (e: Exception) {
            "default"
        }

        return when {
            cleanHost.contains("instagram.com", ignoreCase = true) ->
                "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80"
            cleanHost.contains("github.com", ignoreCase = true) ->
                "https://images.unsplash.com/photo-1618401471353-b98afee0b2eb?w=600&auto=format&fit=crop&q=80"
            cleanHost.contains("youtube.com", ignoreCase = true) || cleanHost.contains("youtu.be", ignoreCase = true) ->
                "https://images.unsplash.com/photo-1611162617213-7d7a39e9b1d7?w=600&auto=format&fit=crop&q=80"
            else ->
                "https://picsum.photos/seed/${cleanHost.hashCode()}/600/400"
        }
    }
}
