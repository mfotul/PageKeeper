package com.example.pagekeeper.core.database.pages.navigation

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pagekeeper.pages.data.reader.Fb2BlockElementDto

@Entity
data class ContentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val bookId: Long,
    val title: Fb2BlockElementDto,
)
