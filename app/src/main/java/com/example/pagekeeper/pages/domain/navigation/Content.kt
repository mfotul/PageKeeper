package com.example.pagekeeper.pages.domain.navigation

import com.example.pagekeeper.pages.presentation.navigation.models.ChapterUi

data class Content(
    val id: Int,
    val bookId: Long,
    val title: List<String>,
    val chapters: List<ChapterUi>
)
