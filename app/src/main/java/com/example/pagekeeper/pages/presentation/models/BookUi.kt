package com.example.pagekeeper.pages.presentation.models

data class BookUi(
    val id: Long,
    val bookCoverPath: String?,
    val bookTitle: String,
    val authorName: String,
    val isSelected: Boolean,
    val isFavorite: Boolean,
    val isFinished: Boolean,
    val readingProgress: Float,
    val bookmarksCount: Int?,
)
