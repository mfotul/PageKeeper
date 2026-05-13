package com.example.pagekeeper.pages.domain.reader

data class Element(
    val elementId: Long? = null,
    val bookId: Long,
    val bodyId: Int,
    val sectionId: Int,
    val content: Fb2BlockElement
)
