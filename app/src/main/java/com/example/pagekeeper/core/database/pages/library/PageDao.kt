package com.example.pagekeeper.core.database.pages.library

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.pagekeeper.core.database.book_section_relation.BookWithSection
import com.example.pagekeeper.core.database.book_section_relation.BookWithSectionCountRoom
import com.example.pagekeeper.core.database.pages.reader.SectionEntity
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

    @Transaction
    @Query("SELECT * FROM bookentity WHERE bookId=:id")
    fun observeBookById(id: Int): Flow<BookWithSection?>


    @Query("SELECT * FROM sectionentity WHERE bookId=:id")
    fun observeSectionsByBookIdPaginated(id: Int): PagingSource<Int, SectionEntity>

    @Transaction
    @Query("SELECT * FROM bookentity WHERE bookId IN (:ids)")
    fun observeBooksByIds(ids: List<Int>): Flow<List<BookWithSection>>

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
        (SELECT COUNT(*) FROM sectionentity WHERE bookId = :id) AS sectionCount 
        FROM bookentity 
        WHERE bookId = :id
    """)
    fun getBookTitleWithCount(id: Int): Flow<BookWithSectionCountRoom?>

    @Query("UPDATE bookentity SET isSelected=0")
    suspend fun removeSelected()

    @Upsert
    suspend fun upsertBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(books: List<BookEntity>)

    @Insert
    suspend fun insertSection(section: SectionEntity)

    @Delete
    suspend fun deleteSections(sections: List<SectionEntity>)
}