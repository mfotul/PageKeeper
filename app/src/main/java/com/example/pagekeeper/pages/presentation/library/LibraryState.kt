package com.example.pagekeeper.pages.presentation.library

import androidx.compose.runtime.Stable
import com.example.pagekeeper.pages.presentation.library.models.Screen
import com.example.pagekeeper.pages.presentation.library.models.DialogType
import com.example.pagekeeper.pages.presentation.library.models.ScreenType
import com.example.pagekeeper.pages.presentation.models.BookUi

@Stable
data class LibraryState(
    val books: List<BookUi> = emptyList(),
    val screenType: ScreenType = ScreenType.LIST,
    val searchResult: List<BookUi> = emptyList(),
    val screen: Screen = Screen.LIBRARY,
    val isTabletSearchBarEnabled: Boolean = false,
    val dialogType: DialogType = DialogType.NONE,
    val booksPendingDeletion: List<BookUi> = emptyList(),
    val recentlyOpenedBooks: List<Long> = emptyList()
)
