package com.example.pagekeeper.pages.presentation.library

import android.net.Uri
import com.example.pagekeeper.pages.presentation.library.models.Screen
import com.example.pagekeeper.pages.presentation.models.BookUi

sealed interface LibraryAction {
    data object OnSearchIconClick: LibraryAction
    data object OnMenuIconClick: LibraryAction
    data object OnDrawerClose: LibraryAction
    data object OnBackClick: LibraryAction
    data object OnImportBookClick: LibraryAction
    data object OnDialogCloseClick: LibraryAction
    data class OnScreenChange(val screen: Screen): LibraryAction
    data class OnImportFile(val uri: Uri): LibraryAction
    data class OnBookClick(val bookId: Long): LibraryAction
    data class OnBookLongClick(val bookId: Long): LibraryAction
    data class OnBookFavoriteClick(val bookId: Long): LibraryAction
    data object OnBooksFavoriteClick: LibraryAction
    data class OnBookFinishClick(val bookId: Long): LibraryAction
    data class OnBookShareClick(val bookId: Long): LibraryAction
    data object OnBooksShareClick: LibraryAction
    data class OnBookDeleteOneClick(val bookId: Long): LibraryAction
    data object OnBooksDeleteClick: LibraryAction
    data object OnBookDeleteConfirmClick: LibraryAction
    data object OnDropDownMenuDismiss: LibraryAction
    data class OnDropDownMenuClick(val book: BookUi): LibraryAction
    data class OnDropDownMenuViewBookmarkClick(val bookId: Long): LibraryAction
    data class OnDropDownMenuDeleteBookmarksClick(val bookId: Long): LibraryAction
    data object OnDropDownMenuDeleteConfirmClick: LibraryAction
}