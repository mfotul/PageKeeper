package com.example.pagekeeper.pages.presentation.reader

import com.example.pagekeeper.pages.presentation.models.ColorItem

data class ReaderState(
    val areBarsVisible: Boolean = false,
    val bookName: String,
    val isAutRotate: Boolean = true,
    val fontSize: Float,
    val fonSizeChangeCounter: Int = 0,
    val isFavorite: Boolean,
    val isFontSliderVisible: Boolean = false,
    val readingPositionIndex: Int,
    val readingPositionOffset: Int,
    val elementCount: Int,
    val bookmarks: Map<Int, ColorItem> = emptyMap()
)
