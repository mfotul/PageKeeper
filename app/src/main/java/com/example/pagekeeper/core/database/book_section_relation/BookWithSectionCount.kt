package com.example.pagekeeper.core.database.book_section_relation

import androidx.room.Embedded
import com.example.pagekeeper.core.database.pages.library.BookEntity

data class BookWithSectionCountRoom(
    @Embedded val book: BookEntity,
    val sectionCount: Int
)
