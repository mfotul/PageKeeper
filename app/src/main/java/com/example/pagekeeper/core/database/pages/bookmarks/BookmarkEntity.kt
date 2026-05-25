package com.example.pagekeeper.core.database.pages.bookmarks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val bookId: Long,
    val colorItem: String,
    val title: String,
    val chapter: String,
    val creationTime: Long,
    val readingPositionIndex: Int,
    val readingPositionOffset: Int,
)
