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
import com.example.pagekeeper.pages.presentation.models.BookUi
import com.example.pagekeeper.pages.presentation.preview.PreviewModel

@Composable
fun LibraryList(
    bookUis: List<BookUi>,
    isTablet: Boolean,
    isSelectable: Boolean,
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
                onClick = { onAction(LibraryAction.OnBookClick(bookUi.id)) },
                onLongClick = { onAction(LibraryAction.OnBookLongClick(bookUi.id)) },
                onFavoriteClick = { onAction(LibraryAction.OnBookFavoriteClick(bookUi.id)) },
                onFinishClick = { onAction(LibraryAction.OnBookFinishClick(bookUi.id)) },
                onShareClick = { onAction(LibraryAction.OnBookShareClick(bookUi.id)) },
                onDeleteClick = { onAction(LibraryAction.OnBookDeleteOneClick(bookUi.id)) },
                isSelectable = isSelectable
            )
        }
    }
}

@Preview(device = TABLET, showBackground = true)
@Composable
private fun LibraryListPreview() {
    PageKeeperTheme {
        LibraryList(
            bookUis = PreviewModel.books,
            isTablet = false,
            isSelectable = true,
            onAction = {}
        )
    }
}