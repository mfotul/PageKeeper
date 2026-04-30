package com.example.pagekeeper.pages.domain.reader

import java.time.Instant

data class BookWithSectionCount (
    val title: String,
    val author: String,
    val bookPath: String?,
    val coverPath: String?,
    val documentId: String,
    val isFavorite: Boolean = false,
    val isFinished: Boolean = false,
    val isSelected: Boolean = false,
    val addedAt: Instant,
    val bookId: Int? = null,
    val sectionCount: Int,
)