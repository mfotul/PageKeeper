package com.example.pagekeeper.pages.presentation.bookmarks

import com.example.pagekeeper.pages.presentation.bookmarks.models.ColorItem

sealed interface BookmarksAction {
    data object OnBackClick : BookmarksAction
    data object OnAddBookmarkClick : BookmarksAction
    data object OnSaveBookmarkClick : BookmarksAction
    data object OnDismissBookmarkDialog : BookmarksAction
    data object OnDismissDropDownMenu : BookmarksAction
    data class OnColorClick(val color: ColorItem) : BookmarksAction
    data object OnDropDownClick: BookmarksAction
}