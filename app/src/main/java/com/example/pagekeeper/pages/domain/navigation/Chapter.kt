package com.example.pagekeeper.pages.domain.navigation

data class Chapter(
    val id: Int? = null,
    val contentId: Int,
    val elementId: Long,
    val title: List<String>,
)
