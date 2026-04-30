package com.example.pagekeeper.pages.presentation.reader

data class ReaderState(
    val areBarsVisible: Boolean = false,
    val bookName: String = "",
    val isAutRotate: Boolean = true,
    val fontSize: Float? = null,
    val fonSizeChangeCounter: Int = 0,
    val isFontSliderVisible: Boolean = false,
)
