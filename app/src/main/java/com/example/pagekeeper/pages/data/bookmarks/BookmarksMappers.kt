package com.example.pagekeeper.pages.data.bookmarks

import com.example.pagekeeper.core.database.pages.bookmarks.BookmarkEntity
import com.example.pagekeeper.pages.domain.bookmarks.Bookmark
import java.time.Instant

fun Bookmark.toBookmarkEntity(): BookmarkEntity {
    return BookmarkEntity(
        id = id ?: 0,
        bookId = bookId,
        colorItem = colorItem,
        title = title,
        chapter = chapter,
        creationTime = creationTime.toEpochMilli(),
        readingPositionIndex = readingPositionIndex,
        readingPositionOffset = readingPositionOffset
    )
}

fun BookmarkEntity.toBookmark(): Bookmark {
    return Bookmark(
        id = id,
        bookId = bookId,
        colorItem = colorItem,
        title = title,
        chapter = chapter,
        creationTime = Instant.ofEpochMilli(creationTime),
        readingPositionIndex = readingPositionIndex,
        readingPositionOffset = readingPositionOffset
    )
}