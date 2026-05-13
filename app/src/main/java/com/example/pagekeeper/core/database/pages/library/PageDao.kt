package com.example.pagekeeper.core.database.pages.library

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.pagekeeper.core.database.book_element_relation.BookWithElementCount
import com.example.pagekeeper.core.database.pages.reader.ElementEntity
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

    @Query("SELECT * FROM bookentity WHERE bookId=:id")
    fun observeBookById(id: Long): Flow<BookEntity?>


    @Query("SELECT * FROM elemententity WHERE bookId=:id ORDER BY elementId")
    fun observeElementsByBookId(id: Long): PagingSource<Int, ElementEntity>

    @Query("SELECT * FROM elemententity WHERE bookId=:id ORDER BY elementId")
    fun observeElementsByBookIdTest(id: Long): Flow<List<ElementEntity>>

    @Query("SELECT * FROM bookentity WHERE bookId IN (:ids)")
    fun observeBooksByIds(ids: List<Long>): Flow<List<BookEntity>>

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

    @Query("""
        SELECT *, 
        (SELECT COUNT(*) FROM elemententity WHERE bookId = :id) AS elementCount 
        FROM bookentity 
        WHERE bookId = :id
    """)
    fun getBookTitleWithCount(id: Long): Flow<BookWithElementCount?>

    @Query("""
        SELECT * 
        FROM elemententity 
        WHERE bookId = :id AND 
        (content LIKE "%Fb2BlockElementDto.Title%" OR lower(content) LIKE "%chapter%") 
        ORDER BY elementId
    """)
    fun observerChaptersByBookId(id: Long): Flow<List<ElementEntity>>

    @Query("""
        SELECT COUNT(*) AS row_index
        FROM elemententity 
        WHERE bookId = :bookId AND elementId <= :elementId
        ORDER BY elementId
    """)
    fun getIndexOfElementByBookId(bookId: Long, elementId: Long): Flow<Int>

    @Query("UPDATE bookentity SET isSelected=0")
    suspend fun removeSelected()

    @Upsert
    suspend fun upsertBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(books: List<BookEntity>)

    @Insert
    suspend fun insertElement(section: ElementEntity)

    @Query("DELETE FROM elemententity WHERE bookId = :bookId")
    suspend fun deleteElementsByBookId(bookId: Long)
}