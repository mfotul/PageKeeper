package com.example.pagekeeper.core.database.pages.library

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val bookId: Long = 0,
    val title: String,
    val author: String,
    val bookPath: String,
    val coverPath: String?,
    val documentId: String,
    val isFavorite: Boolean,
    val isFinished: Boolean,
    val isSelected: Boolean,
    val readingPositionIndex: Int,
    val readingPositionOffset: Int,
    val readingProgress: Float,
    val currentElementId: Long?,
    val addedAt: Long,
)