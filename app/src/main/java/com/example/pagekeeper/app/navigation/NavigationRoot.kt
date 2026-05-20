package com.example.pagekeeper.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.pagekeeper.pages.presentation.bookmarks.BookmarksScreenRoot
import com.example.pagekeeper.pages.presentation.library.LibraryScreenRoot
import com.example.pagekeeper.pages.presentation.navigation.NavigationScreenRoot
import com.example.pagekeeper.pages.presentation.reader.ReaderScreenRoot
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(NavigationRoute.LibraryScreen)
    val resultStore = rememberResultStore()

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavigationRoute.LibraryScreen> {
                LibraryScreenRoot(
                    onBookSelected = { bookId ->
                        backStack.add(NavigationRoute.ReaderScreen(bookId))
                    }
                )
            }
            entry<NavigationRoute.ReaderScreen> { route ->
                ReaderScreenRoot(
                    onBackClick = {
                        backStack.removeLastOrNull()
                    },
                    onChapterClick = { bookId, elementId ->
                        backStack.add(NavigationRoute.NavigationScreen(bookId, elementId))
                    },
                    onBookmarksClick = { bookId ->
                        backStack.add(NavigationRoute.BookmarksScreen(bookId))
                    },
                    resultStore = resultStore,
                    viewModel = koinViewModel { parametersOf(route.bookId) }
                )
            }

            entry<NavigationRoute.NavigationScreen> { route ->
                NavigationScreenRoot(
                    onBackClick = {
                        backStack.removeLastOrNull()
                    },
                    resultStore = resultStore,
                    viewModel = koinViewModel { parametersOf(route.bookId, route.elementId) }
                )
            }
            entry<NavigationRoute.BookmarksScreen> { route ->
                BookmarksScreenRoot(
                    onBackClick = {
                        backStack.removeLastOrNull()
                    },
                    viewModel = koinViewModel { parametersOf(route.bookId) }
                )
            }
        }
    )
}