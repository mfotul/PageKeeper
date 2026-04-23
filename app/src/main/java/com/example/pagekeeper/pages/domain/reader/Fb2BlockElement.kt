package com.example.pagekeeper.pages.domain.reader

sealed interface Fb2BlockElement {
    data class Paragraph(val text: List<StyledText>) : Fb2BlockElement

    data class Title(val lines: List<StyledText>) : Fb2BlockElement

    data class Subtitle(val text: List<StyledText>) : Fb2BlockElement

    data class Cite(
        val lines: List<List<StyledText>>,
        val author: List<StyledText>?
    ) : Fb2BlockElement
    object EmptyLine : Fb2BlockElement
}