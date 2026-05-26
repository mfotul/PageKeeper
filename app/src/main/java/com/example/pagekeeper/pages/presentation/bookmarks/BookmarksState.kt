package com.example.pagekeeper.pages.presentation.bookmarks

import com.example.pagekeeper.pages.presentation.bookmarks.models.BookmarkUi
import com.example.pagekeeper.pages.presentation.models.ColorItem
import com.example.pagekeeper.pages.presentation.bookmarks.models.DialogType

data class BookmarksState(
    val bookmarks: List<BookmarkUi> = emptyList(),
    val colorItems: List<ColorItem> = ColorItem.entries.map { it },
    val selectedColorItem: ColorItem = ColorItem.BLUE,
    val dialogOpen: DialogType = DialogType.NONE,
    val isColorDropDownMenuOpen: Boolean = false,
    val actionDropDownMenuOpen: BookmarkUi? = null,
    val pendingBookmarkId: Int? = null
)
