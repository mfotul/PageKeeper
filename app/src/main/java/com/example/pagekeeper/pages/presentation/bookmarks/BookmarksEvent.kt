package com.example.pagekeeper.pages.presentation.bookmarks

sealed interface BookmarksEvent {
    data class OnBookmarkOpen(
        val bookId: Long,
        val positionIndex: Int,
        val positionOffset: Int
    ) : BookmarksEvent
}