package com.example.pagekeeper.pages.presentation.navigation.models

data class ChapterUi(
    val elementId: Long,
    val title: List<String>,
    val isSelected: Boolean = false,
)
