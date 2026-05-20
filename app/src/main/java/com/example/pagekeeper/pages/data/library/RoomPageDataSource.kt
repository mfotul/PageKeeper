package com.example.pagekeeper.pages.data.library

import com.example.pagekeeper.core.database.pages.library.PageDao
import com.example.pagekeeper.pages.data.navigation.toChapter
import com.example.pagekeeper.pages.data.navigation.toChapterEntity
import com.example.pagekeeper.pages.data.navigation.toContent
import com.example.pagekeeper.pages.data.navigation.toContentEntity
import com.example.pagekeeper.pages.data.reader.toElement
import com.example.pagekeeper.pages.data.reader.toElementEntity
import com.example.pagekeeper.pages.domain.library.Book
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.domain.navigation.Chapter
import com.example.pagekeeper.pages.domain.navigation.Content
import com.example.pagekeeper.pages.domain.reader.Element
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

    override fun observeBookById(bookId: Long): Flow<Book?> {
        return pageDao
            .observeBookById(bookId)
            .map { it?.toBook() }
    }

    override fun observeBooksByIds(ids: List<Long>): Flow<List<Book>> {
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

    override fun getBookTitleWithCount(id: Long): Flow<Book?> {
        return pageDao
            .getBookTitleWithCount(id)
            .map { book ->
                book?.toBook()
            }
    }

    override fun observerChaptersByBookId(id: Long): Flow<List<Element>> {
        return pageDao
            .observerChaptersByBookId(id)
            .map {
                it.map { element ->
                    element.toElement()
                }
            }
    }

    override fun observerChaptersByBookIdAndSectionId(id: Long): Flow<List<Element>> {
        return pageDao
            .observerChaptersByBookIdAndSectionId(id)
            .map {
                it.map { element ->
                    element.toElement()
                }
            }
    }

    override fun getIndexOfElementByBookId(
        bookId: Long,
        elementId: Long
    ): Flow<Int> = pageDao.getIndexOfElementByBookId(bookId, elementId)

    override fun observeContentsByBookId(bookId: Long): Flow<List<Content>> {
        return pageDao
            .observeContentsByBookId(bookId)
            .map { contents ->
                contents.map { content ->
                    content.toContent()
                }
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

    override suspend fun insertElement(section: Element) {
        pageDao.insertElement(section.toElementEntity())
    }

    override suspend fun deleteElementsByBookId(bookId: Long) {
        pageDao.deleteElementsByBookId(bookId)
    }

    override suspend fun insertContentWithChapters(content: Content) {
        pageDao.insertContentWithChapters(
            content.toContentEntity(),
            content.chapters.map { it.toChapterEntity() }
        )
    }

}