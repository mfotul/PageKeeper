package com.example.pagekeeper.pages.presentation.navigation.models

import com.example.pagekeeper.pages.presentation.models.ChapterUi

data class ContentUi(
    val id: Int,
    val title: List<String>,
    val chapters: List<ChapterUi>
)
