package com.example.pagekeeper.core.database.pages

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PageDao {

    @Query("SELECT 1")
    suspend fun checkPoint(): Int

    @Query("SELECT * FROM bookentity ORDER BY addedAt DESC")
    fun observeLibraryBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM bookentity WHERE isFavorite=1 ORDER BY addedAt DESC")
    fun observeFavoriteBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM bookentity WHERE isFinished=1 ORDER BY addedAt DESC")
    fun observeFinishedBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM bookentity WHERE id=:id")
    fun observeBookById(id: Int): Flow<BookEntity>

    @Query("SELECT * FROM bookentity WHERE id IN (:ids)")
    fun observeBooksByIds(ids: List<Int>): Flow<List<BookEntity>>

    @Query("SELECT documentId FROM bookentity")
    fun observeDocumentsId(): Flow<List<String>>

    @Query("""
        SELECT * 
        FROM bookentity 
        WHERE title LIKE "%" || :search || "%" OR
        author LIKE "%" || :search || "%"
        ORDER BY addedAt DESC
    """)
    fun searchBooksByTitle(search: String): Flow<List<BookEntity>>

    @Query("UPDATE bookentity SET isSelected=0")
    suspend fun removeSelected()

    @Upsert
    suspend fun upsertBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: List<BookEntity>)

}