package com.example.pagekeeper.pages.presentation.reader.models

import com.example.pagekeeper.pages.domain.reader.BodyType

data class SectionUi(
    val sectionId: Int,
    val bookId: Int,
    val body: BodyType,
    val section: Fb2SectionUi? = null,
    val title: Fb2TitleUi? = null
)

