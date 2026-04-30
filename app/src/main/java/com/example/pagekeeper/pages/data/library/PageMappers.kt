package com.example.pagekeeper.pages.data.library

import com.example.pagekeeper.core.database.book_section_relation.BookWithSection
import com.example.pagekeeper.core.database.book_section_relation.BookWithSectionCountRoom
import com.example.pagekeeper.core.database.pages.library.BookEntity
import com.example.pagekeeper.pages.data.reader.toSection
import com.example.pagekeeper.pages.domain.library.Book
import com.example.pagekeeper.pages.domain.reader.BookWithSectionCount
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
        addedAt = Instant.ofEpochMilli(addedAt),
        sections = emptyList()
    )
}

fun BookWithSection.toBook(): Book {
    return Book(
        bookId = book.bookId,
        title = book.title,
        author = book.author,
        bookPath = book.bookPath,
        coverPath = book.coverPath,
        documentId = book.documentId,
        isFavorite = book.isFavorite,
        isFinished = book.isFinished,
        isSelected = book.isSelected,
        addedAt = Instant.ofEpochMilli(book.addedAt),
        sections = sections.map { it.toSection() }
    )
}

fun BookWithSectionCountRoom.toBookWithSectionCount(): BookWithSectionCount {
    return BookWithSectionCount(
        title = book.title,
        author = book.author,
        bookPath = book.bookPath,
        coverPath = book.coverPath,
        documentId = book.documentId,
        isFavorite = book.isFavorite,
        isFinished = book.isFinished,
        isSelected = book.isSelected,
        addedAt = Instant.ofEpochMilli(book.addedAt),
        bookId = book.bookId,
        sectionCount = sectionCount
    )
}