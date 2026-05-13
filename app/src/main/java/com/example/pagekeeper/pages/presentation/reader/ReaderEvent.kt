package com.example.pagekeeper.pages.presentation.reader

sealed interface ReaderEvent {
    data object OnBackClick: ReaderEvent
    data class OnChapterClick(val bookId: Long, val currentElementIdOnTop: Long?): ReaderEvent
}