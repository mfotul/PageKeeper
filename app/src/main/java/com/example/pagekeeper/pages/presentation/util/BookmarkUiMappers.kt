package com.example.pagekeeper.pages.presentation.util

import com.example.pagekeeper.pages.domain.bookmarks.Bookmark
import com.example.pagekeeper.pages.presentation.bookmarks.models.BookmarkUi
import com.example.pagekeeper.pages.presentation.models.ColorItem

fun Bookmark.toBookmarkUi(): BookmarkUi {
    return BookmarkUi(
        id = id!!,
        bookId = bookId,
        colorItem = ColorItem.valueOf(colorItem),
        text = title,
        chapter = chapter,
        creationTime = creationTime
    )
}