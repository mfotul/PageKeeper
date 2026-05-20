package com.example.pagekeeper.pages.presentation.bookmarks

import com.example.pagekeeper.pages.presentation.bookmarks.models.BookmarkUi
import com.example.pagekeeper.pages.presentation.bookmarks.models.ColorItem

data class BookmarksState(
    val bookmarks: List<BookmarkUi> = emptyList(),
    val selectedColor: ColorItem = ColorItem.BLUE,
    val isBookmarkDialogOpen: Boolean = false,
    val isDropDownMenuOpen: Boolean = false
)
