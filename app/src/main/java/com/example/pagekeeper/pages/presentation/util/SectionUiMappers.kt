package com.example.pagekeeper.pages.presentation.util

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.example.pagekeeper.pages.domain.reader.Fb2BlockElement
import com.example.pagekeeper.pages.domain.reader.Fb2Section
import com.example.pagekeeper.pages.domain.reader.Fb2Title
import com.example.pagekeeper.pages.domain.reader.Section
import com.example.pagekeeper.pages.presentation.reader.models.Fb2BlockElementUi
import com.example.pagekeeper.pages.presentation.reader.models.Fb2SectionUi
import com.example.pagekeeper.pages.presentation.reader.models.Fb2TitleUi
import com.example.pagekeeper.pages.presentation.reader.models.SectionUi

fun Section.toSectionUi(fontSize: TextUnit): SectionUi {
    return SectionUi(
        sectionId = sectionId ?: 0,
        bookId = bookId,
        body = body,
        section = section?.toFb2SectionUi(fontSize = fontSize),
        title = title?.toFb2TitleUi(fontSize = fontSize),
    )
}

fun Fb2Section.toFb2SectionUi(fontSize: TextUnit): Fb2SectionUi {
    return Fb2SectionUi(
        title = title?.toFb2TitleUi(fontSize = fontSize),
        content = content.map { it.toFb2BlockElementUi(fontSize = fontSize) }
    )
}

fun Fb2Title.toFb2TitleUi(fontSize: TextUnit): Fb2TitleUi {
    return Fb2TitleUi(
        lines = lines.map { it.toAnnotatedString(
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSize * 1.4,
            lineHeightMultiplier = 1.2f
        ) }
    )
}

fun Fb2BlockElement.toFb2BlockElementUi(fontSize: TextUnit) : Fb2BlockElementUi =
    when(this) {
        is Fb2BlockElement.Cite -> Fb2BlockElementUi.Cite(
            lines = lines.map { it.toAnnotatedString(fontStyle = FontStyle.Italic, fontSize = fontSize) },
            author = author?.toAnnotatedString(fontSize = fontSize)
        )
        Fb2BlockElement.EmptyLine -> Fb2BlockElementUi.EmptyLine
        is Fb2BlockElement.Paragraph -> Fb2BlockElementUi.Paragraph(text.toAnnotatedString(fontSize = fontSize))
        is Fb2BlockElement.Subtitle -> Fb2BlockElementUi.Subtitle(text.toAnnotatedString(fontSize = fontSize))
        is Fb2BlockElement.Title -> Fb2BlockElementUi.Title(lines.toAnnotatedString(fontWeight = FontWeight.SemiBold, fontSize = fontSize))
    }