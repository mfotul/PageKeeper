package com.example.pagekeeper.pages.domain.library

import com.example.pagekeeper.pages.domain.bookmarks.Bookmark
import com.example.pagekeeper.pages.domain.navigation.Content
import com.example.pagekeeper.pages.domain.reader.Element
import kotlinx.coroutines.flow.Flow

interface PageDataSource {
    suspend fun checkPoint(): Int
    fun observeLibrary(): Flow<List<Book>>
    fun observeFavorites(): Flow<List<Book>>
    fun observeFinished(): Flow<List<Book>>
    fun observeBookById(id: Long): Flow<Book?>
    fun observeBooksByIds(ids: List<Long>): Flow<List<Book>>
    fun observeDocumentsId(): Flow<List<String>>
    fun searchBooksByTitle(search: String): Flow<List<Book>>
    fun getBookTitleWithCount(id: Long): Flow<Book?>
    fun observerChaptersByBookId(id: Long): Flow<List<Element>>
    fun observerChaptersWithSectionByBookId(id: Long): Flow<List<Element>>
    fun getPositionInBook(bookId: Long, elementId: Long): Flow<Int>
    fun observeContentsByBookId(bookId: Long): Flow<List<Content>>
    fun observeBookmarksByBookId(bookId: Long): Flow<List<Bookmark>>
    fun observeBookmarkById(id: Int): Flow<Bookmark?>
    fun observeElementByBookIdAndPosition(bookId: Long, position: Int): Flow<Element?>
    suspend fun removeSelected()
    suspend fun upsertBook(book: Book)
    suspend fun deleteBook(book: List<Book>)
    suspend fun insertElement(section: Element)
    suspend fun deleteElementsByBookId(bookId: Long)
    suspend fun insertContentWithChapters(content: Content)
    suspend fun deleteContentsWithChapters(contents: List<Content>)
    suspend fun upsertBookmark(bookmark: Bookmark)
    suspend fun deleteBookmark(bookmark: Bookmark)
    suspend fun deleteBookmarks(bookmarks: List<Bookmark>)
}