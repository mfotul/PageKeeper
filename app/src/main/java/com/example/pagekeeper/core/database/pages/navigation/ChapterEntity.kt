package com.example.pagekeeper.core.database.pages.navigation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChapterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val elementId: Long,
    val title: String,
)
