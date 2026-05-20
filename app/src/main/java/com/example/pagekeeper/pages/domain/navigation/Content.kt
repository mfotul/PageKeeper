package com.example.pagekeeper.pages.domain.navigation

import com.example.pagekeeper.pages.domain.reader.Fb2BlockElement

data class Content(
    val id: Int? = null,
    val bookId: Long,
    val title: Fb2BlockElement,
    val chapters: List<Chapter>
)
