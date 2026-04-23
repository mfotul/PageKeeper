package com.example.pagekeeper.pages.data.reader

import kotlinx.serialization.Serializable

@Serializable
data class Fb2SectionDto(
    val title: FB2TitleDto? = null,
    val content: List<FB2BlockElementDto>
)

@Serializable
sealed interface FB2BlockElementDto {
    @Serializable
    data class Paragraph(val text: List<StyledTextDto>) : FB2BlockElementDto

    @Serializable
    data class Title(val lines: List<StyledTextDto>) : FB2BlockElementDto

    @Serializable
    data class Subtitle(val text: List<StyledTextDto>) : FB2BlockElementDto

    @Serializable
    data class Cite(
        val lines: List<List<StyledTextDto>>,
        val author: List<StyledTextDto>?
    ) : FB2BlockElementDto

    @Serializable
    object EmptyLine : FB2BlockElementDto
}

@Serializable
data class FB2TitleDto(val lines: List<List<StyledTextDto>>)

@Serializable
data class StyledTextDto(
    val text: String,
    val isBold: Boolean = false,
    val isItalic: Boolean = false
)
