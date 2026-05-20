package com.example.pagekeeper.pages.presentation.util

import androidx.compose.ui.graphics.Color
import com.example.pagekeeper.pages.domain.bookmarks.Bookmark
import com.example.pagekeeper.pages.presentation.bookmarks.models.BookmarkUi

fun Bookmark.toBookmarkUi(): BookmarkUi {
    return BookmarkUi(
        id = id!!,
        bookId = bookId,
        colorIndicator = Color(colorIndicator),
        text = title,
        chapter = chapter,
        creationTime = creationTime
    )
}