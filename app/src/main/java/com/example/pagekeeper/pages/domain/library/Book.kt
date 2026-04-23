package com.example.pagekeeper.pages.domain.library

import com.example.pagekeeper.pages.domain.reader.Section
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
    val addedAt: Instant,
    val bookId: Int? = null,
    val sections: List<Section>
)
