package com.example.pagekeeper.pages.domain.library

import java.time.Instant

data class Book(
    val title: String,
    val author: String,
    val bookPath: String?,
    val coverPath: String?,
    val documentId: String,
    val isFavorite: Boolean = false,
    val isFinished: Boolean = false,
    val isSelected: Boolean = false,
    val readingPositionIndex: Int,
    val readingPositionOffset: Int,
    val readingProgress: Float,
    val currentElementId: Long?,
    val addedAt: Instant,
    val elementCount: Int? = null,
    val bookmarkCount: Int? = null,
    val bookId: Long? = null,
)
