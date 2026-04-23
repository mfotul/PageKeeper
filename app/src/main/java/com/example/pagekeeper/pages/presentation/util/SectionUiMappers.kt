package com.example.pagekeeper.pages.presentation.util

import com.example.pagekeeper.pages.domain.reader.Fb2BlockElement
import com.example.pagekeeper.pages.domain.reader.Fb2Section
import com.example.pagekeeper.pages.domain.reader.Fb2Title
import com.example.pagekeeper.pages.domain.reader.Section
import com.example.pagekeeper.pages.presentation.reader.models.Fb2BlockElementUi
import com.example.pagekeeper.pages.presentation.reader.models.Fb2SectionUi
import com.example.pagekeeper.pages.presentation.reader.models.Fb2TitleUi
import com.example.pagekeeper.pages.presentation.reader.models.SectionUi

fun Section.toSectionUi(): SectionUi {
    return SectionUi(
        sectionId = sectionId ?: 0,
        bookId = bookId,
        body = body,
        section = section?.toFb2SectionUi(),
        title = title?.toFb2TitleUi(),
    )
}

fun Fb2Section.toFb2SectionUi(): Fb2SectionUi {
    return Fb2SectionUi(
        title = title?.toFb2TitleUi(),
        content = content.map { it.toFb2BlockElementUi() }
    )
}

fun Fb2Title.toFb2TitleUi(): Fb2TitleUi {
    return Fb2TitleUi(
        lines = lines.map { it.toAnnotatedString() }
    )
}

fun Fb2BlockElement.toFb2BlockElementUi() : Fb2BlockElementUi =
    when(this) {
        is Fb2BlockElement.Cite -> Fb2BlockElementUi.Cite(
            lines = lines.map { it.toAnnotatedString() },
            author = author?.toAnnotatedString()
        )
        Fb2BlockElement.EmptyLine -> Fb2BlockElementUi.EmptyLine
        is Fb2BlockElement.Paragraph -> Fb2BlockElementUi.Paragraph(text.toAnnotatedString())
        is Fb2BlockElement.Subtitle -> Fb2BlockElementUi.Subtitle(text.toAnnotatedString())
        is Fb2BlockElement.Title -> Fb2BlockElementUi.Title(lines.toAnnotatedString())
    }