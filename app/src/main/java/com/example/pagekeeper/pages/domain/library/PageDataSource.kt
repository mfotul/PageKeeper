package com.example.pagekeeper.pages.domain.library

import com.example.pagekeeper.pages.domain.reader.BookWithSectionCount
import com.example.pagekeeper.pages.domain.reader.Section
import kotlinx.coroutines.flow.Flow

interface PageDataSource {
    suspend fun checkPoint(): Int
    fun observeLibrary(): Flow<List<Book>>
    fun observeFavorites(): Flow<List<Book>>
    fun observeFinished(): Flow<List<Book>>
    fun observeBookById(id: Int): Flow<Book?>
    fun observeBooksByIds(ids: List<Int>): Flow<List<Book>>
    fun observeDocumentsId(): Flow<List<String>>
    fun searchBooksByTitle(search: String): Flow<List<Book>>
    fun getBookTitleWithCount(id: Int): Flow<BookWithSectionCount?>
    suspend fun removeSelected()
    suspend fun upsertBook(book: Book)
    suspend fun deleteBook(book: List<Book>)
    suspend fun insertSection(section: Section)
    suspend fun deleteSections(sections: List<Section>)
}