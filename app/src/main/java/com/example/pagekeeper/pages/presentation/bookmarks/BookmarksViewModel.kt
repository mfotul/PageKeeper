package com.example.pagekeeper.pages.presentation.bookmarks

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagekeeper.pages.domain.bookmarks.Bookmark
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.presentation.bookmarks.models.ColorItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

class BookmarksViewModel(
    private val bookId: Long,
    private val pageDataSource: PageDataSource
) : ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(BookmarksState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadInitialData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = BookmarksState()
        )

    val titleState = TextFieldState()


    fun loadInitialData() {
        _state.update {
            BookmarksState()
        }

        snapshotFlow { titleState.text }
            .onEach { title ->

            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: BookmarksAction) {
        when (action) {
            BookmarksAction.OnBackClick -> {}
            BookmarksAction.OnAddBookmarkClick -> dialog(true)
            BookmarksAction.OnDismissBookmarkDialog -> dialog(false)
            BookmarksAction.OnSaveBookmarkClick -> saveBookmark()
            is BookmarksAction.OnColorClick -> selectColor(action.color)
            BookmarksAction.OnDismissDropDownMenu -> dropDownMenu(false)
            BookmarksAction.OnDropDownClick -> dropDownMenu(!state.value.isDropDownMenuOpen)
        }
    }

    private fun saveBookmark() {
        viewModelScope.launch {
            pageDataSource
                .observeBookById(bookId)
                .firstOrNull()
                ?.let { book ->
                    val bookmark = Bookmark(
                        title = titleState.text.toString(),
                        bookId = bookId,
                        colorIndicator = state.value.selectedColor.color.value.toInt(),
                        chapter = "",
                        creationTime = Instant.now(),
                        readingPositionIndex = book.readingPositionIndex,
                        readingPositionOffset = book.readingPositionOffset
                    )
                }
        }


    }

    private fun selectColor(color: ColorItem) {
        _state.update {
            it.copy(
                selectedColor = color,
                isDropDownMenuOpen = false
            )
        }
    }

    private fun dialog(isDialogOpen: Boolean) {
        _state.update {
            it.copy(
                isBookmarkDialogOpen = isDialogOpen
            )
        }
    }

    private fun dropDownMenu(isDropDownMenuOpen: Boolean) {
        _state.update {
            it.copy(
                isDropDownMenuOpen = isDropDownMenuOpen
            )
        }
    }
}