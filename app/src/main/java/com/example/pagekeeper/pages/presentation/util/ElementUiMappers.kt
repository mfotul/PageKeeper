package com.example.pagekeeper.pages.presentation.util

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.pagekeeper.pages.domain.reader.Element
import com.example.pagekeeper.pages.domain.reader.Fb2BlockElement
import com.example.pagekeeper.pages.presentation.models.Fb2BlockElementUi
import com.example.pagekeeper.pages.presentation.reader.models.ElementUi

fun Element.toElementUi(fontSize: TextUnit): ElementUi {
    return ElementUi(
        elementId = elementId!!,
        bookId = bookId,
        bodyId = bodyId,
        sectionId = sectionId,
        content = content.toFb2BlockElementUi(fontSize)
    )
}

fun Fb2BlockElement.toFb2BlockElementUi(fontSize: TextUnit = 18.sp): Fb2BlockElementUi =
    when (this) {
        is Fb2BlockElement.Cite -> Fb2BlockElementUi.Cite(
            lines = lines.map {
                it.toAnnotatedString(
                    fontStyle = FontStyle.Italic,
                    fontSize = fontSize
                )
            },
            author = author?.toAnnotatedString(fontSize = fontSize)
        )

        Fb2BlockElement.EmptyLine -> Fb2BlockElementUi.EmptyLine
        is Fb2BlockElement.Paragraph -> Fb2BlockElementUi.Paragraph(text.toAnnotatedString(fontSize = fontSize))
        is Fb2BlockElement.Subtitle -> Fb2BlockElementUi.Subtitle(text.toAnnotatedString(fontSize = fontSize))
        is Fb2BlockElement.Title -> Fb2BlockElementUi.Title(lines.map {
            it.toAnnotatedString(
                fontWeight = FontWeight.SemiBold,
                fontSize = fontSize * 1.4,
                lineHeightMultiplier = 1.2f
            )
        })
    }

fun Fb2BlockElement.toTitleString(): List<String> {
    return when (this) {
        is Fb2BlockElement.Paragraph -> listOf(text.toTitleString())
        is Fb2BlockElement.Title -> lines.map { it.toTitleString() }
        else -> emptyList()
    }
}

fun Fb2BlockElementUi.toTitleString(): String {
    return when (this) {
        is Fb2BlockElementUi.Cite -> lines[0].text
        Fb2BlockElementUi.EmptyLine -> ""
        is Fb2BlockElementUi.Paragraph -> text.text
        is Fb2BlockElementUi.Subtitle -> text.text
        is Fb2BlockElementUi.Title -> lines[0].text
    }
}