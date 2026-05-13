package com.example.pagekeeper.pages.presentation.navigation.models

data class ContentUi(
    val title: List<String>,
    val chapters: List<ChapterUi>,
    val collapsed: Boolean = true
)
