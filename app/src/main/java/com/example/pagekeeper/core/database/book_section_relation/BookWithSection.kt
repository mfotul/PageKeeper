package com.example.pagekeeper.core.database.book_section_relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.pagekeeper.core.database.pages.library.BookEntity
import com.example.pagekeeper.core.database.pages.reader.SectionEntity

data class BookWithSection(
    @Embedded val book: BookEntity,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    val sections: List<SectionEntity>
)