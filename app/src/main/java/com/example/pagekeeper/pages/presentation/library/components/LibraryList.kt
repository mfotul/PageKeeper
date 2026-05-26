package com.example.pagekeeper.pages.presentation.library.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.pages.presentation.library.LibraryAction
import com.example.pagekeeper.pages.presentation.library.models.Screen
import com.example.pagekeeper.pages.presentation.models.BookUi
import com.example.pagekeeper.pages.presentation.preview.PreviewModel

@Composable
fun LibraryList(
    bookUis: List<BookUi>,
    screen: Screen,
    isTablet: Boolean,
    isSelectable: Boolean,
    dropDownMenuOpen: BookUi?,
    onAction: (LibraryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val columns = GridCells.Fixed(if (isTablet) 2 else 1)

    LazyVerticalGrid(
        columns = columns,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        items(items = bookUis, key = { it.id }) { bookUi ->
            LibraryBookCard(
                bookUi = bookUi,
                onClick = {
                    if (screen == Screen.BOOKMARKS)
                        onAction(
                            LibraryAction.OnDropDownMenuViewBookmarkClick(
                                bookUi.id
                            )
                        )
                    else
                        onAction(LibraryAction.OnBookClick(bookUi.id))
                },
                onLongClick = { onAction(LibraryAction.OnBookLongClick(bookUi.id)) },
                onFavoriteClick = { onAction(LibraryAction.OnBookFavoriteClick(bookUi.id)) },
                onFinishClick = { onAction(LibraryAction.OnBookFinishClick(bookUi.id)) },
                onShareClick = { onAction(LibraryAction.OnBookShareClick(bookUi.id)) },
                onDeleteClick = { onAction(LibraryAction.OnBookDeleteOneClick(bookUi.id)) },
                isSelectable = isSelectable,
                isMenuOpened = dropDownMenuOpen == bookUi,
                onMenuClick = { onAction(LibraryAction.OnDropDownMenuClick(bookUi)) },
                onViewBookmarkClick = {
                    onAction(LibraryAction.OnDropDownMenuViewBookmarkClick(bookUi.id))
                },
                onDeleteBookmarksClick = {
                    onAction(LibraryAction.OnDropDownMenuDeleteBookmarksClick(bookUi.id))
                },
                onMenuDismiss = { onAction(LibraryAction.OnDropDownMenuDismiss) },
            )
        }
    }
}

@Preview(device = TABLET, showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun LibraryListPreview() {
    PageKeeperTheme {
        LibraryList(
            bookUis = PreviewModel.books,
            screen = Screen.BOOKMARKS,
            isTablet = false,
            isSelectable = false,
            dropDownMenuOpen = null,
            onAction = {}
        )
    }
}