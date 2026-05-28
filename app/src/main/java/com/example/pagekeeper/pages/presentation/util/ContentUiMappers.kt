package com.example.pagekeeper.pages.presentation.util

import com.example.pagekeeper.pages.domain.navigation.Chapter
import com.example.pagekeeper.pages.domain.navigation.Content
import com.example.pagekeeper.pages.presentation.models.ChapterUi
import com.example.pagekeeper.pages.presentation.navigation.models.ContentUi

fun Content.toContentUi(): ContentUi {
    return ContentUi(
        id = id ?: 0,
        title = title.toTitleString(),
        chapters = chapters.map { it.toChapterUi() }
    )
}

fun Chapter.toChapterUi(): ChapterUi {
    return ChapterUi(
        elementId = elementId,
        title = title.toTitleString(),
        isSelected = false,
    )
}