package com.example.pagekeeper.pages.domain.library

import kotlinx.coroutines.flow.Flow

interface PageDataSource {
    suspend fun checkPoint(): Int
    fun observeLibrary(): Flow<List<Book>>
    fun observeFavorites(): Flow<List<Book>>
    fun observeFinished(): Flow<List<Book>>
    fun observeBookById(id: Int): Flow<Book>
    fun observeBooksByIds(ids: List<Int>): Flow<List<Book>>
    fun observeDocumentsId(): Flow<List<String>>
    fun searchBooksByTitle(search: String): Flow<List<Book>>
    suspend fun removeSelected()
    suspend fun upsertBook(book: Book)
    suspend fun deleteBook(book: List<Book>)
}