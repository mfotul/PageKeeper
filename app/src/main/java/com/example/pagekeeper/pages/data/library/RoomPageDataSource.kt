package com.example.pagekeeper.pages.data.library

import com.example.pagekeeper.core.database.pages.library.PageDao
import com.example.pagekeeper.pages.data.reader.toSectionEntity
import com.example.pagekeeper.pages.domain.library.Book
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.domain.reader.Section
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomPageDataSource(
    private val pageDao: PageDao
) : PageDataSource {
    override suspend fun checkPoint(): Int {
        return pageDao
            .checkPoint()
    }

    override fun observeLibrary(): Flow<List<Book>> {
        return pageDao
            .observeLibraryBooks()
            .map { books ->
                books.map { it.toBook() }
            }
    }

    override fun observeFavorites(): Flow<List<Book>> {
        return pageDao
            .observeFavoriteBooks()
            .map { books ->
                books.map { it.toBook() }
            }
    }

    override fun observeFinished(): Flow<List<Book>> {
        return pageDao
            .observeFinishedBooks()
            .map { books ->
                books.map { it.toBook() }
            }
    }

    override fun observeBookById(id: Int): Flow<Book?> {
        return pageDao
            .observeBookById(id)
            .map { it?.toBook() }
    }

    override fun observeBooksByIds(ids: List<Int>): Flow<List<Book>> {
        return pageDao
            .observeBooksByIds(ids)
            .map { books ->
                books.map { it.toBook() }
            }
    }

    override fun observeDocumentsId(): Flow<List<String>> {
        return pageDao.observeDocumentsId()
    }

    override fun searchBooksByTitle(search: String): Flow<List<Book>> {
        return pageDao
            .searchBooksByTitle(search)
            .map { books ->
                books.map { it.toBook() }
            }
    }

    override suspend fun removeSelected() {
        pageDao.removeSelected()
    }

    override suspend fun upsertBook(book: Book) {
        pageDao.upsertBook(book.toBookEntity())
    }

    override suspend fun deleteBook(book: List<Book>) {
        pageDao.deleteBook(
            book.map { it.toBookEntity() }
        )
    }

    override suspend fun insertSection(section: Section) {
        pageDao.insertSection(section.toSectionEntity())
    }

    override suspend fun deleteSections(sections: List<Section>) {
        pageDao.deleteSections(sections.map { it.toSectionEntity() })
    }
}