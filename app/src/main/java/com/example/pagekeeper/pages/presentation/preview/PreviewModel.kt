package com.example.pagekeeper.pages.presentation.preview

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import com.example.pagekeeper.pages.presentation.models.BookUi
import com.example.pagekeeper.pages.presentation.reader.models.ElementUi
import com.example.pagekeeper.pages.presentation.models.Fb2BlockElementUi
import com.example.pagekeeper.pages.presentation.navigation.models.ChapterUi
import com.example.pagekeeper.pages.presentation.navigation.models.ContentUi

data object PreviewModel {
    val books = listOf(
        BookUi(
            id = 1,
            bookCoverPath = null,
            bookTitle = "Alice's Adventures in Wonderland",
            authorName = "Lewis Carroll",
            isSelected = false,
            isFavorite = false,
            isFinished = false,
            readingProgress = 0.4f
        ),
        BookUi(
            id = 2,
            bookCoverPath = null,
            bookTitle = "The Free Range<",
            authorName = "Francis William Sullivan",
            isSelected = false,
            isFavorite = false,
            isFinished = false,
            readingProgress = 0.8f
        ),
        BookUi(
            id = 3,
            bookCoverPath = null,
            bookTitle = "The Billionaire’s Secret Heart: A Winters Saga Novella",
            authorName = "Ivy Layne",
            isSelected = false,
            isFavorite = false,
            isFinished = false,
            readingProgress = 0.5f
        ),
    )

    val element: List<ElementUi> = listOf(
        ElementUi(
            elementId = 1,
            bookId = 1,
            bodyId = 1,
            sectionId = 1,
            content = Fb2BlockElementUi.Paragraph(
                text = AnnotatedString(text = "Alice was beginning to get very tired of sitting by her sister on the bank, and of having nothing to do: once or twice she had peeped into the book her sister was reading, but it had no pictures or conversations in it, ")
            ),
        ),
        ElementUi(
            elementId = 2,
            bookId = 1,
            bodyId = 1,
            sectionId = 1,
            content =
                Fb2BlockElementUi.Paragraph(
                    text = AnnotatedString(text = "“and what is the use of a book,” thought Alice “without pictures or conversations?”"),
                ),
        ),
        ElementUi(
            elementId = 3,
            bookId = 1,
            bodyId = 1,
            sectionId = 1,
            content = Fb2BlockElementUi.Cite(
                lines = listOf(
                    AnnotatedString(
                        text = "First line",
                        spanStyle = SpanStyle(fontStyle = FontStyle.Italic)
                    ),
                    AnnotatedString(text = "Second line"),
                    AnnotatedString(text = "Third line")
                ),
                author = AnnotatedString(text = "Author")
            ),
        ),
        ElementUi(
            elementId = 4,
            bookId = 1,
            bodyId = 1,
            sectionId = 1,
            content = Fb2BlockElementUi.Paragraph(
                text = AnnotatedString(text = "“and what is the use of a book,” thought Alice “without pictures or conversations?”"),
            ),
        )
    )

    val contents = listOf(
        ContentUi(
            id = 0,
            title = listOf("Title 1", "Subtitle 1"),
            chapters = listOf(
                ChapterUi(
                    elementId = 0,
                    title = listOf("Chapter 1", "Subchapter 1")
                ),
                ChapterUi(
                    elementId = 1,
                    title = listOf("Chapter 2")
                ),
                ChapterUi(
                    elementId = 2,
                    title = listOf("Chapter 3")
                ),
                ChapterUi(
                    elementId = 3,
                    title = listOf("Chapter 4")
                ),
                ChapterUi(
                    elementId = 4,
                    title = listOf("Chapter 5")
                ),
            )
        ),
        ContentUi(
            id = 1,
            title = listOf("Title 2", "Subtitle 2"),
            chapters = listOf(
                ChapterUi(
                    elementId = 0,
                    title = listOf("Chapter 1")
                ),
                ChapterUi(
                    elementId = 1,
                    title = listOf("Chapter 2")
                )
            )
        )
    )
}
