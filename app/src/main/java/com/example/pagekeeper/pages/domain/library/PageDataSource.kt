package com.example.pagekeeper.pages.domain.library

import com.example.pagekeeper.core.database.pages.reader.ElementEntity
import com.example.pagekeeper.pages.domain.reader.Element
import kotlinx.coroutines.flow.Flow

interface PageDataSource {
    suspend fun checkPoint(): Int
    fun observeLibrary(): Flow<List<Book>>
    fun observeFavorites(): Flow<List<Book>>
    fun observeFinished(): Flow<List<Book>>
    fun observeBookById(bookId: Long): Flow<Book?>
    fun observeBooksByIds(ids: List<Long>): Flow<List<Book>>
    fun observeDocumentsId(): Flow<List<String>>
    fun searchBooksByTitle(search: String): Flow<List<Book>>
    fun getBookTitleWithCount(id: Long): Flow<Book?>
    fun observerChaptersByBookId(id: Long): Flow<List<Element>>
    fun observerChaptersByBookIdAndSectionId(id: Long): Flow<List<Element>>
    fun getIndexOfElementByBookId(bookId: Long, elementId: Long): Flow<Int>
    suspend fun removeSelected()
    suspend fun upsertBook(book: Book)
    suspend fun deleteBook(book: List<Book>)
    suspend fun insertElement(section: Element)
    suspend fun deleteElementsByBookId(bookId: Long)
}