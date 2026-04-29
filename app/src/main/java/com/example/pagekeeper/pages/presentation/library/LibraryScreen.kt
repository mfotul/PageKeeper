@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pagekeeper.pages.presentation.library

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.util.ObserveAsEvents
import com.example.pagekeeper.pages.presentation.library.components.LibraryDialog
import com.example.pagekeeper.pages.presentation.library.components.LibraryEmptyList
import com.example.pagekeeper.pages.presentation.library.components.LibraryList
import com.example.pagekeeper.pages.presentation.library.components.LibraryListTopAppBar
import com.example.pagekeeper.pages.presentation.library.components.LibraryLoadingIndicator
import com.example.pagekeeper.pages.presentation.library.components.LibraryNavigationDrawer
import com.example.pagekeeper.pages.presentation.library.components.LibraryNavigationalRail
import com.example.pagekeeper.pages.presentation.library.components.LibrarySearchBar
import com.example.pagekeeper.pages.presentation.library.components.LibrarySearchResult
import com.example.pagekeeper.pages.presentation.library.components.LibrarySearchTopAppBar
import com.example.pagekeeper.pages.presentation.library.components.LibrarySelectedTopAppBar
import com.example.pagekeeper.pages.presentation.library.components.LibrarySelectedTopRow
import com.example.pagekeeper.pages.presentation.library.models.DialogType
import com.example.pagekeeper.pages.presentation.library.models.Screen
import com.example.pagekeeper.pages.presentation.library.models.ScreenType
import com.example.pagekeeper.pages.presentation.preview.PreviewModel
import com.example.pagekeeper.pages.presentation.util.getStringForScreen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.io.File

@Composable
fun LibraryScreenRoot(
    onBookSelected: (Int) -> Unit,
    viewModel: LibraryViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.onAction(LibraryAction.OnImportFile(it))
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        scope.launch {
            when (event) {
                LibraryEvent.OnDrawerOpen -> drawerState.open()
                LibraryEvent.OnDrawerClose -> drawerState.close()
                LibraryEvent.OnImportBook -> launcher.launch("*/*")
                is LibraryEvent.OnShareBook -> {
                    val uri = getFileProviderUri(context, event.path)
                    val shareIntent = getShareBookIntent(uri)
                    context.startActivity(Intent.createChooser(shareIntent, null))
                }

                is LibraryEvent.OnShareMultipleBooks -> {
                    val uris: ArrayList<Uri> = event.paths.mapTo(ArrayList()) {
                        getFileProviderUri(context, it)
                    }
                    val shareIntent = getShareMultipleBooksIntent(uris)
                    context.startActivity(Intent.createChooser(shareIntent, null))
                }

                is LibraryEvent.OnBookSelected -> onBookSelected(event.bookId)
            }
        }
    }

    LibraryScreen(
        state = state,
        drawerState = drawerState,
        searchFieldState = viewModel.searchFieldState,
        onAction = viewModel::onAction
    )
}

@Composable
fun LibraryScreen(
    state: LibraryState,
    drawerState: DrawerState,
    searchFieldState: TextFieldState,
    onAction: (LibraryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowsSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isTablet =
        windowsSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)

    LibraryNavigationDrawer(
        drawerState = drawerState,
        selectedScreen = state.screen,
        isEnabled = !isTablet,
        onAction = onAction
    ) {
        Scaffold(
            topBar = {
                if (!isTablet)
                    when (state.screenType) {
                        ScreenType.LIST ->
                            LibraryListTopAppBar(
                                title = getStringForScreen(state.screen),
                                onMenuClick = { onAction(LibraryAction.OnMenuIconClick) },
                                onSearchClick = { onAction(LibraryAction.OnSearchIconClick) },
                            )

                        ScreenType.SEARCH ->
                            LibrarySearchTopAppBar(
                                onBackClick = { onAction(LibraryAction.OnBackClick) },
                                searchFieldState = searchFieldState,
                            )

                        ScreenType.SELECTED ->
                            LibrarySelectedTopAppBar(
                                onBackClick = { onAction(LibraryAction.OnBackClick) },
                                onFavoriteClick = { onAction(LibraryAction.OnBooksFavoriteClick) },
                                onShareClick = { onAction(LibraryAction.OnBooksShareClick) },
                                onDeleteClick = { onAction(LibraryAction.OnBooksDeleteClick) },
                                selectedItemsCount = state.books.count { it.isSelected }
                            )
                    }
            },
            modifier = modifier
                .fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                LibraryNavigationalRail(
                    selectedScreen = state.screen,
                    onAction = onAction,
                    isEnabled = isTablet
                ) {
                    if (isTablet)
                        if (state.screenType == ScreenType.SELECTED)
                            LibrarySelectedTopRow(
                                onBackClick = { onAction(LibraryAction.OnBackClick) },
                                onFavoriteClick = { onAction(LibraryAction.OnBooksFavoriteClick) },
                                onShareClick = { onAction(LibraryAction.OnBooksShareClick) },
                                onDeleteClick = { onAction(LibraryAction.OnBooksDeleteClick) },
                                selectedItemsCount = state.books.count { it.isSelected }
                            )
                        else
                            LibrarySearchBar(
                                textFieldState = searchFieldState,
                                screenType = state.screenType,
                                isTabletSearchBarEnabled = state.isTabletSearchBarEnabled,
                                onSearchClick = { onAction(LibraryAction.OnSearchIconClick) },
                                modifier = Modifier
                                    .padding(16.dp)
                            )
                    if (state.screenType == ScreenType.SEARCH)
                        LibrarySearchResult(
                            bookUis = state.searchResult,
                            isTablet = isTablet,
                            onClick = {
                                onAction(LibraryAction.OnBookClick(it))
                            }
                        )
                    else if (state.books.isNotEmpty())
                        LibraryList(
                            bookUis = state.books,
                            isTablet = isTablet,
                            isSelectable = state.screenType == ScreenType.SELECTED,
                            onAction = onAction,
                        )
                    else
                        when (state.screen) {
                            Screen.LIBRARY -> LibraryEmptyList(
                                title = R.string.your_library_is_empty,
                                description = R.string.import_your_first_book_to_start_building_your_library,
                                onImportClick = { onAction(LibraryAction.OnImportBookClick) },
                            )

                            Screen.FAVORITES -> LibraryEmptyList(
                                title = R.string.your_favorites_is_empty,
                                description = R.string.books_you_add_to_favorites_will_appears_here,
                            )

                            Screen.FINISHED -> LibraryEmptyList(
                                title = R.string.your_finished_is_empty,
                                description = R.string.books_you_mark_as_finished_will_appears_here,
                            )
                        }
                }


                when (state.dialogType) {
                    DialogType.UNSUPPORTED_FORMAT ->
                        LibraryDialog(
                            title = stringResource(R.string.unsupported_file_format),
                            description = stringResource(R.string.please_select_a_book_in_fb2_format),
                            onConfirmClick = { onAction(LibraryAction.OnDialogCloseClick) },
                            onDismiss = { onAction(LibraryAction.OnDialogCloseClick) },
                            confirmButton = stringResource(R.string.ok)
                        )

                    DialogType.DELETE -> {
                        val bookUi = state.booksPendingDeletion.first()
                        val title = if (state.booksPendingDeletion.size == 1)
                            stringResource(R.string.delete_one, bookUi.bookTitle)
                        else
                            stringResource(R.string.delete_more, state.booksPendingDeletion.size)

                        LibraryDialog(
                            title = title,
                            description = stringResource(R.string.this_action_will_remove_the_book_from_your_library),
                            confirmButton = stringResource(R.string.delete),
                            cancelButton = stringResource(R.string.cancel),
                            onConfirmClick = {
                                onAction(LibraryAction.OnBookDeleteConfirmClick)
                            },
                            onCancelClick = { onAction(LibraryAction.OnDialogCloseClick) },
                            onDismiss = { onAction(LibraryAction.OnDialogCloseClick) },
                            isTextButtonRed = true
                        )
                    }


                    DialogType.LOADING ->
                        LibraryLoadingIndicator()

                    DialogType.DUPLICATE_DOCUMENT ->
                        LibraryDialog(
                            title = "This book is already in your library.",
                            confirmButton = stringResource(R.string.ok),
                            onConfirmClick = { onAction(LibraryAction.OnDialogCloseClick) },
                            onDismiss = { onAction(LibraryAction.OnDialogCloseClick) }
                        )

                    DialogType.NONE -> {}
                }
            }
        }
    }
}

private fun getFileProviderUri(context: Context, path: String): Uri {
    val file = File(path)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}

private fun getShareBookIntent(uri: Uri): Intent {
    return Intent().apply {
        action = Intent.ACTION_SEND
        type = "application/x-fictionbook+xml"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
}

private fun getShareMultipleBooksIntent(uris: ArrayList<Uri>): Intent {
    return Intent().apply {
        action = Intent.ACTION_SEND_MULTIPLE
        type = "application/x-fictionbook+xml"
        putExtra(Intent.EXTRA_STREAM, uris)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
}

@Preview(device = TABLET)
@Composable
private fun LibraryScreenPreview() {
    PageKeeperTheme {
        LibraryScreen(
            state = LibraryState(
                books = PreviewModel.books,
//                books = emptyList(),
                screenType = ScreenType.SELECTED,
                dialogType = DialogType.NONE,
                booksPendingDeletion = listOf(PreviewModel.books[0])
            ),
            drawerState = rememberDrawerState(DrawerValue.Closed),
            searchFieldState = rememberTextFieldState(),
            onAction = {}
        )
    }
}