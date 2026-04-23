package com.example.pagekeeper.pages.domain.reader

data class Fb2Section(
    val title: Fb2Title? = null,
    val content: List<Fb2BlockElement>
)