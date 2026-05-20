package com.example.pagekeeper.pages.domain.navigation

import com.example.pagekeeper.pages.domain.reader.Fb2BlockElement

data class Chapter(
    val id: Int? = null,
    val contentId: Int? = null,
    val elementId: Long,
    val title: Fb2BlockElement,
)
