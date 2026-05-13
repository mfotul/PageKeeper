package com.example.pagekeeper.pages.data.reader

import com.example.pagekeeper.pages.domain.reader.Fb2BlockElement
import com.example.pagekeeper.pages.domain.reader.StyledText

fun Fb2BlockElement.toFB2BlockElementDto(): Fb2BlockElementDto {
    return when (this) {
        is Fb2BlockElement.Cite -> Fb2BlockElementDto.Cite(
            lines = lines.map { styledTexts -> styledTexts.map { it.toStyledTextDto() } },
            author = author?.map { it.toStyledTextDto() }
        )

        Fb2BlockElement.EmptyLine -> Fb2BlockElementDto.EmptyLine
        is Fb2BlockElement.Paragraph -> Fb2BlockElementDto.Paragraph(
            text = text.map { it.toStyledTextDto() }
        )

        is Fb2BlockElement.Subtitle -> Fb2BlockElementDto.Subtitle(
            text = text.map { it.toStyledTextDto() }
        )

        is Fb2BlockElement.Title -> Fb2BlockElementDto.Title(
            lines = lines.map { lines ->
                lines.map {
                    it.toStyledTextDto()
                }
            }
        )
    }
}

fun StyledText.toStyledTextDto(): StyledTextDto {
    return StyledTextDto(
        text = text,
        isBold = isBold,
        isItalic = isItalic
    )
}

fun Fb2BlockElementDto.toFB2BlockElement(): Fb2BlockElement {
    return when (this) {
        is Fb2BlockElementDto.Cite -> Fb2BlockElement.Cite(
            lines = lines.map { styledTexts -> styledTexts.map { it.toStyledText() } },
            author = author?.map { it.toStyledText() }
        )

        Fb2BlockElementDto.EmptyLine -> Fb2BlockElement.EmptyLine
        is Fb2BlockElementDto.Paragraph -> Fb2BlockElement.Paragraph(
            text = text.map { it.toStyledText() }
        )

        is Fb2BlockElementDto.Subtitle -> Fb2BlockElement.Subtitle(
            text = text.map { it.toStyledText() }
        )

        is Fb2BlockElementDto.Title -> Fb2BlockElement.Title(
            lines = lines.map { lines ->
                lines.map {
                    it.toStyledText()
                }
            }
        )
    }
}

fun StyledTextDto.toStyledText(): StyledText {
    return StyledText(
        text = text,
        isBold = isBold,
        isItalic = isItalic
    )
}