package com.example.pagekeeper.pages.presentation.util

import com.example.pagekeeper.pages.presentation.models.ChapterUi

fun findChapterJustLower(chapters: List<ChapterUi>, targetId: Long): ChapterUi? {
    val index = chapters.binarySearchBy(targetId) { it.elementId }

    val lowerIndex = if (index >= 0) {
        index - 1
    } else {
        val insertionPoint = -(index + 1)
        insertionPoint - 1
    }
    return chapters.getOrNull(lowerIndex)
}