package com.example.pagekeeper.pages.domain.reader

data class StyledText(
    val text: String,
    val isBold: Boolean = false,
    val isItalic: Boolean = false
)