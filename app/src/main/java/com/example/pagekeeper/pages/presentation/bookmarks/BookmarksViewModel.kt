package com.example.pagekeeper.pages.presentation.bookmarks

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagekeeper.pages.domain.bookmarks.Bookmark
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.presentation.bookmarks.models.BookmarkUi
import com.example.pagekeeper.pages.presentation.bookmarks.models.ColorItem
import com.example.pagekeeper.pages.presentation.bookmarks.models.DialogType
import com.example.pagekeeper.pages.presentation.util.findChapterJustLower
import com.example.pagekeeper.pages.presentation.util.toBookmarkUi
import com.example.pagekeeper.pages.presentation.util.toChapterUi
import com.example.pagekeeper.pages.presentation.util.toFb2BlockElementUi
import com.example.pagekeeper.pages.presentation.util.toTitleString
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
    private var titleForNewBookmark = ""

    fun loadInitialData() {
        viewModelScope.launch {
            pageDataSource
                .observeBookById(bookId)
                .firstOrNull()
                ?.let { book ->
                    val element = pageDataSource
                        .observeElementByBookIdAndPosition(bookId, book.readingPositionIndex + 1)
                        .firstOrNull() ?: return@launch

                    titleForNewBookmark = element
                        .content
                        .toFb2BlockElementUi()
                        .toTitleString()
                }
        }

        pageDataSource.observeBookmarksByBookId(bookId)
            .onEach { bookmarks ->
                _state.update {
                    it.copy(
                        bookmarks = bookmarks.map { bookmark ->
                            bookmark.toBookmarkUi()
                        }
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun onAction(action: BookmarksAction) {
        when (action) {
            BookmarksAction.OnBackClick -> {}
            BookmarksAction.OnAddBookmarkClick -> newDialog()
            BookmarksAction.OnDismissBookmarkDialog -> closeDialog()
            BookmarksAction.OnSaveBookmarkClick -> saveBookmark()
            is BookmarksAction.OnColorClick -> selectColor(action.color)
            BookmarksAction.OnDismissColorDropDownMenu -> colorDropDownMenu(false)
            BookmarksAction.OnColorDropDownClick -> colorDropDownMenu(!state.value.isColorDropDownMenuOpen)
            is BookmarksAction.OnActionDropDownClick -> actionDropDownMenu(action.bookmark)
            is BookmarksAction.OnBookmarkClick -> {}
            is BookmarksAction.OnBookmarkDeleteClick -> deleteDialog(action.bookmarkId)
            is BookmarksAction.OnBookmarkEditClick -> editDialog(action.bookmarkId)
            BookmarksAction.OnDismissActionDropDownMenu -> _state.update {
                it.copy(
                    actionDropDownMenuOpen = null
                )
            }

            BookmarksAction.OnBookmarkDeleteConfirmClick -> deleteBookmark()
        }
    }

    private fun closeDialog() {
        _state.update {
            it.copy(
                dialogOpen = DialogType.NONE,
                pendingBookmarkId = null
            )
        }
    }

    private fun deleteDialog(bookmarkId: Int) {
        _state.update {
            it.copy(
                dialogOpen = DialogType.DELETE,
                pendingBookmarkId = bookmarkId,
                actionDropDownMenuOpen = null
            )
        }
    }

    private fun deleteBookmark() {
        viewModelScope.launch {
            state.value.pendingBookmarkId?.let {
                pageDataSource
                    .observeBookmarkById(it)
                    .firstOrNull()
                    ?.let { bookmark ->
                        pageDataSource.deleteBookmark(bookmark)
                        _state.update { state ->
                            state.copy(
                                pendingBookmarkId = null,
                                dialogOpen = DialogType.NONE
                            )
                        }
                    }
            }
        }
    }

    private fun actionDropDownMenu(bookmark: BookmarkUi) {
        _state.update { state ->
            state.copy(
                actionDropDownMenuOpen = if (state.actionDropDownMenuOpen == bookmark) null else bookmark
            )
        }
    }

    private fun saveBookmark() {
        viewModelScope.launch {
            _state.update { state ->
                if (state.pendingBookmarkId != null)
                    pageDataSource
                        .observeBookmarkById(state.pendingBookmarkId)
                        .firstOrNull()
                        ?.let {
                            pageDataSource.upsertBookmark(
                                it.copy(
                                    title = titleState.text.toString(),
                                    colorItem = state.selectedColorItem.name
                                )
                            )
                        }
                else
                    pageDataSource
                        .observeBookById(bookId)
                        .firstOrNull()
                        ?.let { book ->
                            val chapters = pageDataSource
                                .observeContentsByBookId(bookId)
                                .firstOrNull()
                                ?.flatMap { it.chapters }
                                ?.map { it.toChapterUi() }
                                ?: emptyList()

                            val chapter =
                                findChapterJustLower(chapters, book.readingPositionIndex.toLong())
                                    ?.title[0]
                                    ?: book.title

                            val bookmark = Bookmark(
                                title = titleState.text.toString(),
                                bookId = bookId,
                                colorItem = state.selectedColorItem.name,
                                chapter = chapter,
                                creationTime = Instant.now(),
                                readingPositionIndex = book.readingPositionIndex,
                                readingPositionOffset = book.readingPositionOffset
                            )
                            pageDataSource.upsertBookmark(bookmark)
                        }


                state.copy(
                    dialogOpen = DialogType.NONE,
                    pendingBookmarkId = null
                )
            }
        }
    }

    private fun selectColor(color: ColorItem) {
        _state.update {
            it.copy(
                selectedColorItem = color,
                isColorDropDownMenuOpen = false
            )
        }
    }

    private fun newDialog() {
        _state.update { state ->
            titleState.edit {
                delete(0, length)
                append(titleForNewBookmark)
            }
            state.copy(
                dialogOpen = DialogType.ADD,
                selectedColorItem = ColorItem.BLUE
            )
        }
    }

    private fun editDialog(bookmarkId: Int) {
        viewModelScope.launch {
            pageDataSource
                .observeBookmarkById(bookmarkId)
                .firstOrNull()
                ?.let { bookmark ->
                    titleState.edit {
                        delete(0, length)
                        append(bookmark.title)
                    }
                    _state.update {
                        it.copy(
                            dialogOpen = DialogType.ADD,
                            actionDropDownMenuOpen = null,
                            pendingBookmarkId = bookmarkId,
                            selectedColorItem = ColorItem.valueOf(bookmark.colorItem)
                        )
                    }
                }
        }
    }


    private fun colorDropDownMenu(isDropDownMenuOpen: Boolean) {
        _state.update {
            it.copy(
                isColorDropDownMenuOpen = isDropDownMenuOpen
            )
        }
    }

}