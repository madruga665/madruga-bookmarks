package com.madruga665.bookmarks.data.repository

import com.madruga665.bookmarks.data.local.BookmarkDao
import com.madruga665.bookmarks.data.local.BookmarkEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class BookmarkRepository(
    private val bookmarkDao: BookmarkDao
) {
    val allBookmarks: Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()

    suspend fun quickSaveBookmark(
        url: String,
        collectionId: String = "col_unsorted",
        isPinned: Boolean = false
    ): Boolean {
        if (url.isBlank() || (!url.startsWith("http://") && !url.startsWith("https://"))) {
            return false
        }
        val entity = BookmarkEntity(
            id = UUID.randomUUID().toString(),
            url = url.trim(),
            title = null,
            faviconUrl = null,
            collectionId = if (collectionId.isBlank()) "col_unsorted" else collectionId,
            isPinned = isPinned,
            createdAt = System.currentTimeMillis(),
            syncStatus = "PENDING_SYNC"
        )
        bookmarkDao.insertBookmark(entity)
        return true
    }
}
