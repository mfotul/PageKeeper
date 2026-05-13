package com.example.pagekeeper.core.database.pages.reader

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pagekeeper.pages.data.reader.Fb2BlockElementDto

@Entity
data class ElementEntity(
    @PrimaryKey(autoGenerate = true)
    val elementId: Long = 0,
    val bookId: Long,
    val bodyId: Int,
    val sectionId: Int,
    val content: Fb2BlockElementDto
)
