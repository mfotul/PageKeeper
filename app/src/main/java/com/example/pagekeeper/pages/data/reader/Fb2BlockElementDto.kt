package com.example.pagekeeper.pages.data.reader

import kotlinx.serialization.Serializable

@Serializable
sealed interface Fb2BlockElementDto {
    @Serializable
    data class Paragraph(val text: List<StyledTextDto>) : Fb2BlockElementDto

    @Serializable
    data class Title(val lines: List<List<StyledTextDto>>) : Fb2BlockElementDto

    @Serializable
    data class Subtitle(val text: List<StyledTextDto>) : Fb2BlockElementDto

    @Serializable
    data class Cite(
        val lines: List<List<StyledTextDto>>,
        val author: List<StyledTextDto>?
    ) : Fb2BlockElementDto

    @Serializable
    object EmptyLine : Fb2BlockElementDto
}

@Serializable
data class StyledTextDto(
    val text: String,
    val isBold: Boolean = false,
    val isItalic: Boolean = false
)
