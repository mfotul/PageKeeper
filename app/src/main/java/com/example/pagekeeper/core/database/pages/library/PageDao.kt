package com.example.pagekeeper.core.database.pages.library

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.pagekeeper.core.database.book_element_relation.BookWithBookmarksCount
import com.example.pagekeeper.core.database.book_element_relation.BookWithElementCount
import com.example.pagekeeper.core.database.content_chapter_relation.ContentWithChapters
import com.example.pagekeeper.core.database.pages.bookmarks.BookmarkEntity
import com.example.pagekeeper.core.database.pages.navigation.ChapterEntity
import com.example.pagekeeper.core.database.pages.navigation.ContentEntity
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

    @Transaction
    @Query("""
        SELECT *, 
        (SELECT COUNT(*) FROM elemententity WHERE bookId = :id) AS elementCount 
        FROM bookentity 
        WHERE bookId = :id
    """)
    fun getBookTitleWithCount(id: Long): Flow<BookWithElementCount?>

    @Transaction
    @Query("""
        SELECT b.*, COUNT(be.bookId) AS bookmarkCount
        FROM bookentity b
        JOIN bookmarkentity be ON b.bookId = be.bookId
        GROUP BY b.bookId
        HAVING COUNT(be.bookId) > 0
        ORDER BY MAX(be.creationTime) DESC
    """)
    fun observeBooksWithBookmarksCount(): Flow<List<BookWithBookmarksCount>>

    @Query("""
        SELECT * 
        FROM elemententity 
        WHERE bookId = :id AND 
        (content LIKE "%Fb2BlockElementDto.Title%" OR lower(content) LIKE "%chapter%") 
        ORDER BY elementId
    """)
    fun observerChaptersByBookId(id: Long): Flow<List<ElementEntity>>

    @Query("""
        SELECT *
    FROM (
    SELECT *, 
           ROW_NUMBER() OVER (
               PARTITION BY sectionId 
               ORDER BY elementId
           ) as row_num
    FROM elemententity
    WHERE bookid = :bookId
) WHERE row_num = 1    
    """)
    fun observerChaptersWithSectionByBookId(bookId: Long): Flow<List<ElementEntity>>

    @RewriteQueriesToDropUnusedColumns
    @Query("""
        SELECT COUNT(*)
        FROM elemententity 
        WHERE bookId = :bookId AND elementId <= :elementId
    """)
    fun getPositionInBook(bookId: Long, elementId: Long): Flow<Int>

    @Transaction
    @Query("SELECT * FROM contententity WHERE bookId = :bookId ORDER BY id")
    fun observeContentsByBookId(bookId: Long): Flow<List<ContentWithChapters>>

    @Query("SELECT * FROM bookmarkentity WHERE bookId = :bookId ORDER BY creationTime DESC")
    fun observeBookmarksByBookId(bookId: Long): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarkentity WHERE id = :id")
    fun observeBookmarkById(id: Int): Flow<BookmarkEntity?>

    @Query("""
        SELECT * 
        FROM elemententity 
        WHERE bookId = :id
        ORDER by elementId
        LIMIT 1 OFFSET :position - 1
    """)
    fun observeElementByBookIdAndPosition(id: Long, position: Int): Flow<ElementEntity?>

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

    @Insert
    suspend fun insertContent(content: ContentEntity): Long

    @Insert
    suspend fun insertChapter(chapters: List<ChapterEntity>)

    @Transaction
    suspend fun insertContentWithChapters(content: ContentEntity, chapters: List<ChapterEntity>) {
        val contentId = insertContent(content)
        insertChapter(chapters.map { it.copy(contentId = contentId.toInt()) })
    }

    @Delete
    suspend fun deleteContent(content: ContentEntity)

    @Delete
    suspend fun deleteChapters(chapters: List<ChapterEntity>)

    @Transaction
    suspend fun deleteContentWithChapters(content: ContentEntity, chapters: List<ChapterEntity>) {
        deleteContent(content)
        deleteChapters(chapters)
    }

    @Upsert
    suspend fun upsertBookmark(bookmark: BookmarkEntity)

    @Delete
    suspend fun deleteBookmark(bookmark: BookmarkEntity)

    @Delete
    suspend fun deleteBookmarks(bookmarks: List<BookmarkEntity>)
}