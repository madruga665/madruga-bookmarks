package com.madruga665.bookmarks.data.repository

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

    @Test
    fun collections_whenEmpty_returnsDefaultSampleCollections() = runTest {
        every { collectionDao.getAllCollections() } returns flowOf(emptyList())
        val repository = CollectionRepository(collectionDao)

        val result = repository.collections.first()
        assertEquals(4, result.size)
        assertEquals("Unsorted", result[0].name)
        assertEquals("IA", result[1].name)
        assertEquals("Vagas", result[2].name)
        assertEquals("Programação", result[3].name)
    }
}
