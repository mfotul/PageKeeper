package com.example.pagekeeper.pages.data.navigation

import com.example.pagekeeper.core.database.pages.navigation.ChapterEntity
import com.example.pagekeeper.pages.data.reader.toFB2BlockElement
import com.example.pagekeeper.pages.data.reader.toFB2BlockElementDto
import com.example.pagekeeper.pages.domain.navigation.Chapter

fun Chapter.toChapterEntity(): ChapterEntity {
    return ChapterEntity(
        id = id ?: 0,
        contentId = contentId ?: 0,
        elementId = elementId,
        title = title.toFB2BlockElementDto()
    )
}

fun ChapterEntity.toChapter(): Chapter {
    return Chapter(
        id = id,
        contentId = contentId,
        elementId = elementId,
        title = title.toFB2BlockElement()
    )
}