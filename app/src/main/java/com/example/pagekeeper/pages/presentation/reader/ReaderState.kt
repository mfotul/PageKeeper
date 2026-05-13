package com.example.pagekeeper.pages.presentation.reader

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
)
