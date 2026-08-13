package com.madruga665.bookmarks.data.repository

import com.madruga665.bookmarks.data.local.BookmarkDao
import com.madruga665.bookmarks.data.local.CollectionDao
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CollectionRepositoryTest {

    private val collectionDao: CollectionDao = mockk()
    private val bookmarkDao: BookmarkDao = mockk()

    @Test
    fun collections_whenEmpty_returnsDefaultSampleCollections() = runTest {
        io.mockk.coEvery { collectionDao.insertCollections(any()) } returns Unit
        every { collectionDao.getAllCollections() } returns flowOf(emptyList())
        every { bookmarkDao.getAllBookmarks() } returns flowOf(emptyList())
        val repository = CollectionRepository(collectionDao, bookmarkDao)

        val result = repository.collections.first()
        assertEquals(1, result.size)
        assertEquals("Unsorted", result[0].name)
    }
}
