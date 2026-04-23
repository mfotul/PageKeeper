package com.example.pagekeeper.pages.presentation.reader

sealed interface ReaderAction {
    data object OnLockScreenClick: ReaderAction
}