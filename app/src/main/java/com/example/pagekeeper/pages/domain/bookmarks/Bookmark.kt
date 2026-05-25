package com.example.pagekeeper.pages.domain.bookmarks

import java.time.Instant

data class Bookmark(
    val id: Int? = null,
    val bookId: Long,
    val colorItem: String,
    val title: String,
    val chapter: String,
    val creationTime: Instant,
    val readingPositionIndex: Int,
    val readingPositionOffset: Int,
)
