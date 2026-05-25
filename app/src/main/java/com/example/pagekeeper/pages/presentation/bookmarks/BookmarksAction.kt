package com.example.pagekeeper.pages.presentation.bookmarks

import com.example.pagekeeper.pages.presentation.bookmarks.models.BookmarkUi
import com.example.pagekeeper.pages.presentation.bookmarks.models.ColorItem

sealed interface BookmarksAction {
    data object OnBackClick : BookmarksAction
    data object OnAddBookmarkClick : BookmarksAction
    data object OnSaveBookmarkClick : BookmarksAction
    data object OnDismissBookmarkDialog : BookmarksAction
    data object OnDismissColorDropDownMenu : BookmarksAction
    data object OnDismissActionDropDownMenu : BookmarksAction
    data class OnColorClick(val color: ColorItem) : BookmarksAction
    data object OnColorDropDownClick: BookmarksAction
    data class OnBookmarkClick(val bookmarkId: Int) : BookmarksAction
    data class OnActionDropDownClick(val bookmark: BookmarkUi) : BookmarksAction
    data class OnBookmarkDeleteClick(val bookmarkId: Int) : BookmarksAction
    data object OnBookmarkDeleteConfirmClick : BookmarksAction
    data class OnBookmarkEditClick(val bookmarkId: Int) : BookmarksAction
}