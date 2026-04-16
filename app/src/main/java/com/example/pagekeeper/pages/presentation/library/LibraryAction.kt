package com.example.pagekeeper.pages.presentation.library

import android.net.Uri
import com.example.pagekeeper.pages.presentation.library.models.Screen

sealed interface LibraryAction {
    data object OnSearchIconClick: LibraryAction
    data object OnMenuIconClick: LibraryAction
    data object OnDrawerClose: LibraryAction
    data object OnBackClick: LibraryAction
    data object OnImportBookClick: LibraryAction
    data object OnDialogCloseClick: LibraryAction
    data class OnScreenChange(val screen: Screen): LibraryAction
    data class OnImportFile(val uri: Uri): LibraryAction
    data class OnBookClick(val bookId: Int): LibraryAction
    data class OnBookLongClick(val bookId: Int): LibraryAction
    data class OnBookFavoriteClick(val bookId: Int): LibraryAction
    data object OnBooksFavoriteClick: LibraryAction
    data class OnBookFinishClick(val bookId: Int): LibraryAction
    data class OnBookShareClick(val bookId: Int): LibraryAction
    data object OnBooksShareClick: LibraryAction
    data class OnBookDeleteOneClick(val bookId: Int): LibraryAction
    data object OnBooksDeleteClick: LibraryAction
    data object OnBookDeleteConfirmClick: LibraryAction
}