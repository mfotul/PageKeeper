package com.example.pagekeeper.pages.presentation.reader.models

data class Fb2SectionUi(
    val title: Fb2TitleUi? = null,
    val content: List<Fb2BlockElementUi>
)
