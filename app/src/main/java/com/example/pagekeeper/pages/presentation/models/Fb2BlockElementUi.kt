package com.example.pagekeeper.pages.presentation.models

import androidx.compose.ui.text.AnnotatedString

sealed interface Fb2BlockElementUi {
    data class Paragraph(val text: AnnotatedString) : Fb2BlockElementUi

    data class Title(val lines: List<AnnotatedString>) : Fb2BlockElementUi

    data class Subtitle(val text: AnnotatedString) : Fb2BlockElementUi

    data class Cite(
        val lines: List<AnnotatedString>,
        val author: AnnotatedString?
    ) : Fb2BlockElementUi

    object EmptyLine : Fb2BlockElementUi
}