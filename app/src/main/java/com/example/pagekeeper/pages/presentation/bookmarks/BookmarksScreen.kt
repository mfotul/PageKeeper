package com.example.pagekeeper.pages.presentation.bookmarks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.pages.presentation.bookmarks.components.BookmarksDialog
import com.example.pagekeeper.pages.presentation.bookmarks.components.BookmarksEmptyList
import com.example.pagekeeper.pages.presentation.bookmarks.components.BookmarksFloatingActionButton
import com.example.pagekeeper.pages.presentation.bookmarks.components.BookmarksTopAppBar
import com.example.pagekeeper.pages.presentation.bookmarks.models.ColorItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookmarksScreenRoot(
    onBackClick: () -> Unit,
    viewModel: BookmarksViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookmarksScreen(
        state = state,
        titleState = viewModel.titleState,
        onAction = {
            when (it) {
                BookmarksAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(it)
            }
        }
    )
}

@Composable
fun BookmarksScreen(
    state: BookmarksState,
    titleState: TextFieldState,
    onAction: (BookmarksAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            BookmarksTopAppBar(
                onBackClick = {
                    onAction(BookmarksAction.OnBackClick)
                }
            )
        },
        floatingActionButton = {
            BookmarksFloatingActionButton(
                onClick = {
                    onAction(BookmarksAction.OnAddBookmarkClick)
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (state.bookmarks.isNotEmpty()) {

            } else {
                BookmarksEmptyList()
            }

            if (state.isBookmarkDialogOpen)
                BookmarksDialog(
                    titleState = titleState,
                    colorItems = ColorItem.entries.map { it },
                    isDropDownMenuOpen = state.isDropDownMenuOpen,
                    selectedColor = state.selectedColor,
                    onDropDownSelect = {
                        onAction(BookmarksAction.OnColorClick(it))
                    },
                    onDialogDismiss = {
                        onAction(BookmarksAction.OnDismissBookmarkDialog)
                    },
                    onDropDownMenuDismiss = {
                        onAction(BookmarksAction.OnDismissDropDownMenu)
                    },
                    onSaveClick = {
                        onAction(BookmarksAction.OnSaveBookmarkClick)
                    },
                    onColorMenuClick = {
                        onAction(BookmarksAction.OnDropDownClick)
                    }
                )


        }
    }
}

@Preview
@Composable
private fun BookmarksScreenPreview() {
    PageKeeperTheme {
        BookmarksScreen(
            state = BookmarksState(
                isBookmarkDialogOpen = true,
                isDropDownMenuOpen = false
            ),
            titleState = rememberTextFieldState(),
            onAction = {}
        )
    }
}