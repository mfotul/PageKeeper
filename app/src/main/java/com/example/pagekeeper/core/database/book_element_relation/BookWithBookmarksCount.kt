package com.example.pagekeeper.core.database.book_element_relation

import androidx.room.Embedded
import com.example.pagekeeper.core.database.pages.library.BookEntity

data class BookWithBookmarksCount(
    @Embedded val book: BookEntity,
    val bookmarkCount: Int
)
