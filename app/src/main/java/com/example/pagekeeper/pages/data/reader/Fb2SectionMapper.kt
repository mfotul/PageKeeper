package com.example.pagekeeper.pages.data.reader

import com.example.pagekeeper.pages.domain.reader.Fb2BlockElement
import com.example.pagekeeper.pages.domain.reader.Fb2Section
import com.example.pagekeeper.pages.domain.reader.Fb2Title
import com.example.pagekeeper.pages.domain.reader.StyledText

fun Fb2Section.toFb2SectionDto(): Fb2SectionDto {
    return Fb2SectionDto(
        title = title.toFb2TitleDto(),
        content = content.map { it.toFB2BlockElementDto() }
    )
}

fun Fb2Title?.toFb2TitleDto(): FB2TitleDto? {
    return this?.let {
        FB2TitleDto(
            lines = lines.map { styledTexts -> styledTexts.map { it.toStyledTextDto() } }
        )
    }
}

fun Fb2BlockElement.toFB2BlockElementDto(): FB2BlockElementDto {
    return when (this) {
        is Fb2BlockElement.Cite -> FB2BlockElementDto.Cite(
            lines = lines.map { styledTexts -> styledTexts.map { it.toStyledTextDto() } },
            author = author?.map { it.toStyledTextDto() }
        )

        Fb2BlockElement.EmptyLine -> FB2BlockElementDto.EmptyLine
        is Fb2BlockElement.Paragraph -> FB2BlockElementDto.Paragraph(
            text = text.map { it.toStyledTextDto() }
        )

        is Fb2BlockElement.Subtitle -> FB2BlockElementDto.Subtitle(
            text = text.map { it.toStyledTextDto() }
        )

        is Fb2BlockElement.Title -> FB2BlockElementDto.Title(
            lines = lines.map { it.toStyledTextDto() }
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

fun Fb2SectionDto.toFb2Section(): Fb2Section {
    return Fb2Section(
        title = title.toFb2Title(),
        content = content.map {
            it.toFB2BlockElement()
        }
    )
}

fun FB2TitleDto?.toFb2Title(): Fb2Title? {
    return this?.let {
        Fb2Title(
            lines = lines.map { styledTexts -> styledTexts.map { it.toStyledText() } }
        )
    }
}

fun FB2BlockElementDto.toFB2BlockElement(): Fb2BlockElement {
    return when (this) {
        is FB2BlockElementDto.Cite -> Fb2BlockElement.Cite(
            lines = lines.map { styledTexts -> styledTexts.map { it.toStyledText() } },
            author = author?.map { it.toStyledText() }
        )

        FB2BlockElementDto.EmptyLine -> Fb2BlockElement.EmptyLine
        is FB2BlockElementDto.Paragraph -> Fb2BlockElement.Paragraph(
            text = text.map { it.toStyledText() }
        )

        is FB2BlockElementDto.Subtitle -> Fb2BlockElement.Subtitle(
            text = text.map { it.toStyledText() }
        )

        is FB2BlockElementDto.Title -> Fb2BlockElement.Title(
            lines = lines.map { it.toStyledText() }
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