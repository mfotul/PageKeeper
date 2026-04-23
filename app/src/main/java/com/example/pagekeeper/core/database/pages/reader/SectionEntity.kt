package com.example.pagekeeper.core.database.pages.reader

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pagekeeper.pages.domain.reader.BodyType

@Entity
data class SectionEntity(
    @PrimaryKey(autoGenerate = true)
    val sectionId: Int = 0,
    val bookId: Int,
    val body: BodyType,
    val content: String
)
