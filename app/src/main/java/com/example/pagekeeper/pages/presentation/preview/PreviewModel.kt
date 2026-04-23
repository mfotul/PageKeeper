package com.example.pagekeeper.pages.presentation.preview

import androidx.compose.ui.text.AnnotatedString
import com.example.pagekeeper.pages.domain.reader.BodyType
import com.example.pagekeeper.pages.presentation.models.BookUi
import com.example.pagekeeper.pages.presentation.reader.models.Fb2BlockElementUi
import com.example.pagekeeper.pages.presentation.reader.models.Fb2SectionUi
import com.example.pagekeeper.pages.presentation.reader.models.Fb2TitleUi
import com.example.pagekeeper.pages.presentation.reader.models.SectionUi

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

    val section: List<SectionUi> = listOf(
        SectionUi(
            sectionId = 1,
            bookId = 1,
            body = BodyType.SECTION,
            section = Fb2SectionUi(
                title = Fb2TitleUi(
                    lines = listOf(AnnotatedString(text = "Chapter I"))
                ),
                content = listOf(
                    Fb2BlockElementUi.Paragraph(
                        text = AnnotatedString(text = "Alice was beginning to get very tired of sitting by her sister on the bank, and of having nothing to do: once or twice she had peeped into the book her sister was reading, but it had no pictures or conversations in it, ")
                    ),
                    Fb2BlockElementUi.Paragraph(
                        text = AnnotatedString(text = "“and what is the use of a book,” thought Alice “without pictures or conversations?”"),
                    ),
                    Fb2BlockElementUi.Cite(
                        lines = listOf(
                            AnnotatedString(text = "First line"),
                            AnnotatedString(text = "Second line"),
                            AnnotatedString(text = "Third line")
                        ),
                        author = AnnotatedString(text = "Author")
                    ),
                    Fb2BlockElementUi.Paragraph(
                        text = AnnotatedString(text = "“and what is the use of a book,” thought Alice “without pictures or conversations?”"),
                    ),
                )
            )
        )
    )
}
