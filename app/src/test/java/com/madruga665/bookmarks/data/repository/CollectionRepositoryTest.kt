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

    @org.junit.Before
    fun setUp() {
        every { collectionDao.getAllCollections() } returns flowOf(emptyList())
        every { bookmarkDao.getAllBookmarks() } returns flowOf(emptyList())
    }

    @Test
    fun collections_whenEmpty_returnsDefaultSampleCollections() = runTest {
        io.mockk.coEvery { collectionDao.insertCollections(any()) } returns Unit
        val repository = CollectionRepository(collectionDao, bookmarkDao)

        val result = repository.collections.first()
        assertEquals(1, result.size)
        assertEquals("Unsorted", result[0].name)
    }

    @Test
    fun createCollection_withValidInput_insertsAndReturnsEntity() = runTest {
        io.mockk.coEvery { collectionDao.insertCollection(any()) } returns Unit
        val repository = CollectionRepository(collectionDao, bookmarkDao)

        val entity = repository.createCollection("Design Systems", "#9B51E0", "palette")
        org.junit.Assert.assertNotNull(entity)
        assertEquals("Design Systems", entity?.name)
        assertEquals("#9B51E0", entity?.colorAccent)
        assertEquals("palette", entity?.iconKey)
    }

    @Test
    fun createCollection_withBlankIconKey_defaultsToFolder() = runTest {
        io.mockk.coEvery { collectionDao.insertCollection(any()) } returns Unit
        val repository = CollectionRepository(collectionDao, bookmarkDao)

        val entity = repository.createCollection("Docs", "#FFE600", "  ")
        org.junit.Assert.assertNotNull(entity)
        assertEquals("Docs", entity?.name)
        assertEquals("folder", entity?.iconKey)
    }

    @Test
    fun createCollection_withBlankName_returnsNull() = runTest {
        val repository = CollectionRepository(collectionDao, bookmarkDao)
        val entity = repository.createCollection("   ", "#FFE600", "folder")
        org.junit.Assert.assertNull(entity)
    }

    @Test
    fun createCollection_withNameExceeding40Chars_returnsNull() = runTest {
        val repository = CollectionRepository(collectionDao, bookmarkDao)
        val longName = "A".repeat(41)
        val entity = repository.createCollection(longName, "#FFE600", "folder")
        org.junit.Assert.assertNull(entity)
    }
}
