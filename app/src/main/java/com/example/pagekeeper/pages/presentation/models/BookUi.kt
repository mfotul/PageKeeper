package com.example.pagekeeper.pages.presentation.models

data class BookUi(
    val id: Int,
    val bookCoverPath: String?,
    val bookTitle: String,
    val authorName: String,
    val isSelected: Boolean,
    val isFavorite: Boolean,
    val isFinished: Boolean
)
