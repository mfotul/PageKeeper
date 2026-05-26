package com.example.pagekeeper.pages.data.library

import com.example.pagekeeper.core.database.book_element_relation.BookWithBookmarksCount
import com.example.pagekeeper.core.database.book_element_relation.BookWithElementCount
import com.example.pagekeeper.core.database.pages.library.BookEntity
import com.example.pagekeeper.pages.domain.library.Book
import java.time.Instant


fun Book.toBookEntity(): BookEntity {
    return BookEntity(
        bookId = bookId ?: 0,
        title = title,
        author = author,
        bookPath = bookPath ?: "",
        coverPath = coverPath,
        documentId = documentId,
        isFavorite = isFavorite,
        isFinished = isFinished,
        isSelected = isSelected,
        readingPositionIndex = readingPositionIndex,
        readingPositionOffset = readingPositionOffset,
        readingProgress = readingProgress,
        addedAt = addedAt.toEpochMilli()
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        bookId = bookId,
        title = title,
        author = author,
        bookPath = bookPath,
        coverPath = coverPath,
        documentId = documentId,
        isFavorite = isFavorite,
        isFinished = isFinished,
        isSelected = isSelected,
        readingPositionIndex = readingPositionIndex,
        readingPositionOffset = readingPositionOffset,
        readingProgress = readingProgress,
        addedAt = Instant.ofEpochMilli(addedAt),
    )
}

fun BookWithElementCount.toBook(): Book {
 return Book(
     title = book.title,
     author = book.author,
     bookPath = book.bookPath,
     coverPath = book.coverPath,
     documentId = book.documentId,
     isFavorite = book.isFavorite,
     isFinished = book.isFinished,
     isSelected = book.isSelected,
     readingPositionIndex = book.readingPositionIndex,
     readingPositionOffset = book.readingPositionOffset,
     readingProgress = book.readingProgress,
     addedAt = Instant.ofEpochMilli(book.addedAt),
     elementCount = elementCount,
     bookId = book.bookId
 )
}

fun BookWithBookmarksCount.toBook(): Book {
    return Book(
        title = book.title,
        author = book.author,
        bookPath = book.bookPath,
        coverPath = book.coverPath,
        documentId = book.documentId,
        isFavorite = book.isFavorite,
        isFinished = book.isFinished,
        isSelected = book.isSelected,
        readingPositionIndex = book.readingPositionIndex,
        readingPositionOffset = book.readingPositionOffset,
        readingProgress = book.readingProgress,
        addedAt = Instant.ofEpochMilli(book.addedAt),
        bookmarkCount = bookmarkCount,
        bookId = book.bookId
    )
}