package com.example.pagekeeper.pages.presentation.bookmarks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.example.pagekeeper.app.navigation.ResultStore
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.util.ObserveAsEvents
import com.example.pagekeeper.pages.presentation.bookmarks.components.BookmarkListItem
import com.example.pagekeeper.pages.presentation.bookmarks.components.BookmarksAddDialog
import com.example.pagekeeper.pages.presentation.bookmarks.components.BookmarksDeleteDialog
import com.example.pagekeeper.pages.presentation.bookmarks.components.BookmarksEmptyList
import com.example.pagekeeper.pages.presentation.bookmarks.components.BookmarksFloatingActionButton
import com.example.pagekeeper.pages.presentation.bookmarks.components.BookmarksTopAppBar
import com.example.pagekeeper.pages.presentation.bookmarks.models.DialogType
import com.example.pagekeeper.pages.presentation.preview.PreviewModel
import com.example.pagekeeper.pages.presentation.util.thenIf
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookmarksScreenRoot(
    onBackClick: () -> Unit,
    onBookmarksChosen: (Long) -> Unit,
    resultStore: ResultStore,
    viewModel: BookmarksViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is BookmarksEvent.OnBookmarkOpen -> {
                resultStore.setResult("positionIndex", event.positionIndex)
                resultStore.setResult("positionOffset", event.positionOffset)
                onBookmarksChosen(event.bookId)
            }
        }
    }

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
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isTablet =
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
                && windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)


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
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.bookmarks.isNotEmpty()) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(top = 16.dp),
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    items(items = state.bookmarks, key = { it.id }) { bookmark ->
                        BookmarkListItem(
                            bookmark = bookmark,
                            isActionDropDownMenuOpen = state.actionDropDownMenuOpen == bookmark,
                            onBookmarkClick = {
                                onAction(BookmarksAction.OnBookmarkClick(bookmark.id))
                            },
                            onMenuClick = {
                                onAction(BookmarksAction.OnActionDropDownClick(bookmark))
                            },
                            onDismiss = {
                                onAction(BookmarksAction.OnDismissActionDropDownMenu)
                            },
                            onEditClick = {
                                onAction(BookmarksAction.OnBookmarkEditClick(bookmark.id))
                            },
                            onDeleteClick = {
                                onAction(BookmarksAction.OnBookmarkDeleteClick(bookmark.id))
                            },
                            modifier = Modifier
                                .thenIf(isTablet) {
                                    widthIn(max = 600.dp)
                                }
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .thenIf(isTablet) {
                                    widthIn(max = 600.dp)
                                }
                        )
                    }
                }
            } else {
                BookmarksEmptyList()
            }

            when (state.dialogOpen) {
                DialogType.ADD ->
                    BookmarksAddDialog(
                        titleState = titleState,
                        colorItems = state.colorItems,
                        isDropDownMenuOpen = state.isColorDropDownMenuOpen,
                        selectedColor = state.selectedColorItem,
                        onDropDownSelect = {
                            onAction(BookmarksAction.OnColorClick(it))
                        },
                        onDialogDismiss = {
                            onAction(BookmarksAction.OnDismissBookmarkDialog)
                        },
                        onDropDownMenuDismiss = {
                            onAction(BookmarksAction.OnDismissColorDropDownMenu)
                        },
                        onSaveClick = {
                            onAction(BookmarksAction.OnSaveBookmarkClick)
                        },
                        onColorMenuClick = {
                            onAction(BookmarksAction.OnColorDropDownClick)
                        }
                    )

                DialogType.DELETE ->
                    BookmarksDeleteDialog(
                        onDismiss = {
                            onAction(BookmarksAction.OnDismissBookmarkDialog)
                        },
                        onCancel = {
                            onAction(BookmarksAction.OnDismissBookmarkDialog)
                        },
                        onDelete = {
                            onAction(BookmarksAction.OnBookmarkDeleteConfirmClick)
                        }
                    )

                else -> {}
            }
        }
    }
}

@Preview(device = TABLET)
@Composable
private fun BookmarksScreenPreview() {
    PageKeeperTheme {
        BookmarksScreen(
            state = BookmarksState(
                bookmarks = PreviewModel.bookmark,
                dialogOpen = DialogType.NONE,
                isColorDropDownMenuOpen = false
            ),
            titleState = rememberTextFieldState(),
            onAction = {}
        )
    }
}