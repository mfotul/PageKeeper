package com.example.pagekeeper.pages.data.navigation

import com.example.pagekeeper.core.database.content_chapter_relation.ContentWithChapters
import com.example.pagekeeper.core.database.pages.navigation.ContentEntity
import com.example.pagekeeper.pages.data.reader.toFB2BlockElement
import com.example.pagekeeper.pages.data.reader.toFB2BlockElementDto
import com.example.pagekeeper.pages.domain.library.Book
import com.example.pagekeeper.pages.domain.navigation.Chapter
import com.example.pagekeeper.pages.domain.navigation.Content
import com.example.pagekeeper.pages.domain.reader.Fb2BlockElement
import com.example.pagekeeper.pages.domain.reader.StyledText

fun ContentWithChapters.toContent(): Content {
    return Content(
        id = content.id,
        bookId = content.bookId,
        title = content.title.toFB2BlockElement(),
        chapters = chapters.map { it.toChapter() }
    )
}

fun Content.toContentEntity(): ContentEntity {
    return ContentEntity(
        id = id ?: 0,
        bookId = bookId,
        title = title.toFB2BlockElementDto()
    )
}

fun Map<Int, Map<Int, List<Chapter>>>.toContents(book: Book): List<Content> {
    return this.values.map { sectionsMap ->
        val titleElement = sectionsMap[0]?.firstOrNull()?.title

        Content(
            title = titleElement ?: Fb2BlockElement.Title(listOf(listOf(StyledText(text = book.title)))),
            bookId = book.bookId!!,
            chapters = sectionsMap.filterKeys { it != 0 }.values.flatten()
        )
    }
}
