package com.madruga665.bookmarks.data.repository

import com.madruga665.bookmarks.data.local.CollectionDao
import com.madruga665.bookmarks.data.local.CollectionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CollectionRepository(
    private val collectionDao: CollectionDao
) {
    val collections: Flow<List<CollectionEntity>> = collectionDao.getAllCollections().map { list ->
        if (list.isEmpty()) {
            defaultCollections()
        } else {
            list
        }
    }

    private fun defaultCollections(): List<CollectionEntity> = listOf(
        CollectionEntity(
            id = "col_ia",
            name = "IA",
            linkCount = 2,
            iconKey = "code",
            colorAccent = "YELLOW",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ),
        CollectionEntity(
            id = "col_vagas",
            name = "Vagas",
            linkCount = 2,
            iconKey = "work",
            colorAccent = "PURPLE",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ),
        CollectionEntity(
            id = "col_programacao",
            name = "Programação",
            linkCount = 0,
            iconKey = "code",
            colorAccent = "ORANGE",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    )
}
