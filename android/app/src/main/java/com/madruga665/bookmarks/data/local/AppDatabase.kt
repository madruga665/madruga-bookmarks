package com.madruga665.bookmarks.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collections_table ORDER BY name ASC")
    fun getAllCollections(): Flow<List<CollectionEntity>>

    @Query("SELECT * FROM collections_table WHERE id = :collectionId")
    fun getCollectionById(collectionId: String): Flow<CollectionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollections(collections: List<CollectionEntity>)

    @Query("UPDATE collections_table SET link_count = link_count + 1 WHERE id = :collectionId")
    suspend fun incrementLinkCount(collectionId: String)

    @Query("UPDATE collections_table SET name = :name, color_accent = :colorAccent, icon_key = :iconKey, updated_at = :updatedAt WHERE id = :id")
    suspend fun updateCollection(id: String, name: String, colorAccent: String, iconKey: String, updatedAt: Long)

    @Query("DELETE FROM collections_table WHERE id = :collectionId")
    suspend fun deleteCollectionById(collectionId: String)
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks_table ORDER BY created_at DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks_table WHERE collection_id = :collectionId ORDER BY is_pinned DESC, created_at DESC")
    fun getBookmarksByCollection(collectionId: String): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks_table WHERE collection_id = :collectionId ORDER BY is_pinned DESC, created_at DESC")
    suspend fun getBookmarksByCollectionList(collectionId: String): List<BookmarkEntity>

    @Query("SELECT * FROM bookmarks_table WHERE id = :bookmarkId")
    fun getBookmarkById(bookmarkId: String): Flow<BookmarkEntity?>

    @Query("SELECT * FROM bookmarks_table WHERE id = :bookmarkId")
    suspend fun getBookmarkByIdDirect(bookmarkId: String): BookmarkEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Update
    suspend fun updateBookmark(bookmark: BookmarkEntity)

    @Query("UPDATE bookmarks_table SET title = :title, updated_at = :updatedAt, sync_status = 'PENDING_SYNC' WHERE id = :id")
    suspend fun updateBookmarkTitle(id: String, title: String?, updatedAt: Long)

    @Query("UPDATE bookmarks_table SET notes = :notes, updated_at = :updatedAt, sync_status = 'PENDING_SYNC' WHERE id = :id")
    suspend fun updateBookmarkNotes(id: String, notes: String?, updatedAt: Long)

    @Query("UPDATE bookmarks_table SET tags = :tags, updated_at = :updatedAt, sync_status = 'PENDING_SYNC' WHERE id = :id")
    suspend fun updateBookmarkTags(id: String, tags: String, updatedAt: Long)

    @Query("UPDATE bookmarks_table SET is_pinned = :isPinned, updated_at = :updatedAt, sync_status = 'PENDING_SYNC' WHERE id = :id")
    suspend fun updateBookmarkPinned(id: String, isPinned: Boolean, updatedAt: Long)

    @Query("UPDATE bookmarks_table SET collection_id = :collectionId, updated_at = :updatedAt, sync_status = 'PENDING_SYNC' WHERE id = :id")
    suspend fun updateBookmarkCollection(id: String, collectionId: String, updatedAt: Long)

    @Query("UPDATE bookmarks_table SET collection_id = 'col_unsorted' WHERE collection_id = :collectionId")
    suspend fun resetBookmarkCollectionId(collectionId: String)

    @Query("DELETE FROM bookmarks_table WHERE collection_id = :collectionId")
    suspend fun deleteBookmarksByCollectionId(collectionId: String)

    @Query("DELETE FROM bookmarks_table WHERE id = :bookmarkId")
    suspend fun deleteBookmarkById(bookmarkId: String)
}

@Database(entities = [CollectionEntity::class, BookmarkEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collectionDao(): CollectionDao
    abstract fun bookmarkDao(): BookmarkDao
}
