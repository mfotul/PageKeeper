package com.example.pagekeeper.pages.presentation.preview

import com.example.pagekeeper.pages.presentation.models.BookUi

data object PreviewModel {
    val books = listOf(
        BookUi(
            id = 1,
            bookCoverPath = null,
            bookTitle = "Alice's Adventures in Wonderland",
            authorName = "Lewis Carroll",
            isSelected = false,
            isFavorite = false,
            isFinished = false
        ),
        BookUi(
            id = 2,
            bookCoverPath = null,
            bookTitle = "The Free Range<",
            authorName = "Francis William Sullivan",
            isSelected = false,
            isFavorite = false,
            isFinished = false
        ),
        BookUi(
            id = 3,
            bookCoverPath = null,
            bookTitle = "The Billionaire’s Secret Heart: A Winters Saga Novella",
            authorName = "Ivy Layne",
            isSelected = false,
            isFavorite = false,
            isFinished = false
        ),
    )
}