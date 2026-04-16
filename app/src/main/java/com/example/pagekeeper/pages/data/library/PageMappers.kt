package com.example.pagekeeper.pages.data.library

import com.example.pagekeeper.core.database.pages.BookEntity
import com.example.pagekeeper.pages.domain.library.Book
import java.time.Instant


fun Book.toBookEntity(): BookEntity {
    return BookEntity(
        id = id ?: 0,
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
        id = id,
        title = title,
        author = author,
        bookPath = bookPath,
        coverPath = coverPath,
        documentId = documentId,
        isFavorite = isFavorite,
        isFinished = isFinished,
        isSelected = isSelected,
        addedAt = Instant.ofEpochMilli(addedAt)
    )
}