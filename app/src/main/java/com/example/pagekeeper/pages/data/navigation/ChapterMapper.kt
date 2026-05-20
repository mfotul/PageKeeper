package com.example.pagekeeper.pages.data.navigation

import com.example.pagekeeper.core.database.pages.navigation.ChapterEntity
import com.example.pagekeeper.pages.domain.navigation.Chapter

fun Chapter.toChapterEntity(): ChapterEntity {
    return ChapterEntity(
        id = id ?: 0,
        elementId = elementId,
        title = title.joinToString(separator = "|")
    )
}

fun ChapterEntity.toChapter(): Chapter {
    return Chapter(
        id = id,
        elementId = elementId,
        title = title.split("|")
    )
}