package com.example.pagekeeper.pages.presentation.reader

import com.example.pagekeeper.pages.presentation.reader.models.ElementUi

sealed interface ReaderAction {
    data object OnLockScreenClick : ReaderAction
    data class OnBackClick(
        val readingPositionIndex: Int,
        val readingPositionOffset: Int,
        val readingProgress: Float
    ) : ReaderAction

    data object OnFavoritesClick : ReaderAction
    data object OnScreenClick : ReaderAction
    data object OnFontSizeClick : ReaderAction
    data class OnFontSizeChange(val fontSize: Float) : ReaderAction
    data class OnSliderPositionChange(val fontSize: Float) : ReaderAction
    data class OnChapterClick(val currentElementOnTop: ElementUi?) : ReaderAction
    data class OnChapterSelected(val index: Int) : ReaderAction
    data class OnBookmarksClick(
        val readingPositionIndex: Int,
        val readingPositionOffset: Int,
        val readingProgress: Float
    ) : ReaderAction
}