package com.example.pagekeeper.pages.presentation.reader

sealed interface ReaderAction {
    data object OnLockScreenClick : ReaderAction
    data object OnBackClick : ReaderAction
    data object OnFavoritesClick : ReaderAction
    data object OnScreenClick: ReaderAction
    data object OnFontSizeClick: ReaderAction
}