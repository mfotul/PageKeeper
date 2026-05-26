package com.example.pagekeeper.pages.presentation.util

import com.example.pagekeeper.pages.domain.library.Book
import com.example.pagekeeper.pages.presentation.models.BookUi

fun Book.toBookUi(): BookUi {
    return BookUi(
        id = bookId!!,
        bookCoverPath = coverPath,
        bookTitle = title,
        authorName = author,
        isSelected = isSelected,
        isFavorite = isFavorite,
        isFinished = isFinished,
        readingProgress = readingProgress,
        bookmarksCount = bookmarkCount
    )
}