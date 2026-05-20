package com.example.pagekeeper.core.database.content_chapter_relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.pagekeeper.core.database.pages.navigation.ChapterEntity
import com.example.pagekeeper.core.database.pages.navigation.ContentEntity

data class ContentWithChapters(
    @Embedded val content: ContentEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "contentId"
    )
    val chapters: List<ChapterEntity>
)
