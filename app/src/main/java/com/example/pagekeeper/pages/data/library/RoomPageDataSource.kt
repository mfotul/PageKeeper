package com.example.pagekeeper.pages.data.library

import com.example.pagekeeper.core.database.pages.library.PageDao
import com.example.pagekeeper.pages.data.bookmarks.toBookmark
import com.example.pagekeeper.pages.data.bookmarks.toBookmarkEntity
import com.example.pagekeeper.pages.data.navigation.toChapterEntity
import com.example.pagekeeper.pages.data.navigation.toContent
import com.example.pagekeeper.pages.data.navigation.toContentEntity
import com.example.pagekeeper.pages.data.reader.toElement
import com.example.pagekeeper.pages.data.reader.toElementEntity
import com.example.pagekeeper.pages.domain.bookmarks.Bookmark
import com.example.pagekeeper.pages.domain.library.Book
import com.example.pagekeeper.pages.domain.library.PageDataSource
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

    override fun observeBookById(id: Long): Flow<Book?> {
        return pageDao
            .observeBookById(id)
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

    override fun observeBooksWithBookmarksCount(): Flow<List<Book>> {
        return pageDao
            .observeBooksWithBookmarksCount()
            .map { books ->
                books.map {
                    it.toBook()
                }
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

    override fun observerChaptersWithSectionByBookId(id: Long): Flow<List<Element>> {
        return pageDao
            .observerChaptersWithSectionByBookId(id)
            .map {
                it.map { element ->
                    element.toElement()
                }
            }
    }

    override fun getPositionInBook(
        bookId: Long,
        elementId: Long
    ): Flow<Int> = pageDao.getPositionInBook(bookId, elementId)

    override fun observeContentsByBookId(bookId: Long): Flow<List<Content>> {
        return pageDao
            .observeContentsByBookId(bookId)
            .map { contents ->
                contents.map { content ->
                    content.toContent()
                }
            }
    }

    override fun observeBookmarksByBookId(bookId: Long): Flow<List<Bookmark>> {
        return pageDao
            .observeBookmarksByBookId(bookId)
            .map { bookmarks ->
                bookmarks.map { bookmark ->
                    bookmark.toBookmark()
                }
            }
    }

    override fun observeBookmarkById(id: Int): Flow<Bookmark?> {
        return pageDao
            .observeBookmarkById(id)
            .map {
                it?.toBookmark()
            }
    }

    override fun observeElementByBookIdAndPosition(bookId: Long, position: Int): Flow<Element?> {
        return pageDao
            .observeElementByBookIdAndPosition(bookId, position)
            .map { it?.toElement() }
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

    override suspend fun deleteContentsWithChapters(contents: List<Content>) {
        contents.forEach { content ->
            pageDao.deleteContentWithChapters(
                content = content.toContentEntity(),
                chapters = content.chapters.map { it.toChapterEntity() }
            )
        }
    }

    override suspend fun upsertBookmark(bookmark: Bookmark) {
        pageDao.upsertBookmark(bookmark.toBookmarkEntity())
    }

    override suspend fun deleteBookmark(bookmark: Bookmark) {
        pageDao.deleteBookmark(bookmark.toBookmarkEntity())
    }

    override suspend fun deleteBookmarks(bookmarks: List<Bookmark>) {
        pageDao.deleteBookmarks(
            bookmarks.map { it.toBookmarkEntity() }
        )
    }
}