package com.example.pagekeeper.pages.domain.reader

data class Section(
    val sectionId: Int? = null,
    val bookId: Int,
    val body: BodyType,
    val section: Fb2Section? = null,
    val title: Fb2Title? = null
)
