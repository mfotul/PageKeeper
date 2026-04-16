@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.pagekeeper.pages.presentation.library

import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagekeeper.core.domain.util.ParserError
import com.example.pagekeeper.core.domain.util.onError
import com.example.pagekeeper.core.domain.util.onSuccess
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.presentation.library.models.Screen
import com.example.pagekeeper.pages.domain.library.XmlParser
import com.example.pagekeeper.pages.presentation.library.models.DialogType
import com.example.pagekeeper.pages.presentation.library.models.ScreenType
import com.example.pagekeeper.pages.presentation.util.toBookUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class LibraryViewModel(
    private val xmlParser: XmlParser,
    private val pageDataSource: PageDataSource
) : ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(LibraryState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData)
                loadInitialData()
            hasLoadedInitialData = true
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LibraryState()
        )

    private val eventChannel = Channel<LibraryEvent>()
    val events = eventChannel.receiveAsFlow()

    val searchFieldState = TextFieldState()

    private fun loadInitialData() {
        viewModelScope.launch {
            pageDataSource.removeSelected()
        }
        state
            .map {
                it.screen
            }
            .distinctUntilChanged()
            .flatMapLatest {
                when (it) {
                    Screen.LIBRARY -> pageDataSource.observeLibrary()
                    Screen.FAVORITES -> pageDataSource.observeFavorites()
                    Screen.FINISHED -> pageDataSource.observeFinished()
                }
            }
            .onEach { books ->
                _state.update { state ->
                    val booksUi = books.map { it.toBookUi() }
                    val screenType = if (booksUi.any { it.isSelected }) ScreenType.SELECTED
                    else ScreenType.LIST
                    state.copy(books = booksUi, screenType = screenType)
                }
            }
            .launchIn(viewModelScope)


        snapshotFlow { searchFieldState.text }
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isNotEmpty())
                    pageDataSource.searchBooksByTitle(query.toString())
                else
                    flowOf(emptyList())
            }
            .onEach { books ->
                _state.update { it.copy(searchResult = books.map { book -> book.toBookUi() }) }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: LibraryAction) {
        when (action) {
            is LibraryAction.OnScreenChange -> onScreenChange(action.screen)
            LibraryAction.OnSearchIconClick -> _state.update { it.copy(screenType = ScreenType.SEARCH, isTabletSearchBarEnabled = true) }
            LibraryAction.OnBackClick -> onBackClick()
            LibraryAction.OnMenuIconClick -> onEventSend(LibraryEvent.OnDrawerOpen)
            LibraryAction.OnDrawerClose -> onEventSend(LibraryEvent.OnDrawerClose)
            LibraryAction.OnImportBookClick -> onImportBook()
            LibraryAction.OnDialogCloseClick -> _state.update { it.copy(dialogType = DialogType.NONE) }
            is LibraryAction.OnImportFile -> onImportFile(action.uri)
            is LibraryAction.OnBookClick -> onBookClick(action.bookId)
            is LibraryAction.OnBookDeleteOneClick -> onDelete(action.bookId)
            LibraryAction.OnBooksDeleteClick -> onDeleteMultiple()
            is LibraryAction.OnBookFavoriteClick -> onFavoriteClick(action.bookId)
            is LibraryAction.OnBookFinishClick -> onFinishedClick(action.bookId)
            is LibraryAction.OnBookLongClick -> onBookLongClick(action.bookId)
            is LibraryAction.OnBookShareClick -> onBookShareClick(action.bookId)
            LibraryAction.OnBookDeleteConfirmClick -> onConfirmDelete()
            LibraryAction.OnBooksFavoriteClick -> onBooksFavoriteClick()
            LibraryAction.OnBooksShareClick -> onMultipleBookShare()
        }
    }

    private fun onBookLongClick(bookId: Int) {
        viewModelScope.launch {
            pageDataSource
                .observeBookById(bookId)
                .firstOrNull()
                ?.let { book ->
                    pageDataSource.upsertBook(book.copy(isSelected = true))
                }
            _state.update {
                it.copy(
                    screenType = ScreenType.SELECTED,
                )
            }
        }
    }

    private fun onBackClick() {
        viewModelScope.launch {
            pageDataSource.removeSelected()
            searchFieldState.clearText()
            _state.update { state ->
                state.copy(
                    screenType = ScreenType.LIST,
                )
            }
        }
    }

    private fun onBookClick(bookId: Int) {
        viewModelScope.launch {
            if (state.value.screenType != ScreenType.SELECTED) return@launch
            pageDataSource
                .observeBookById(bookId)
                .firstOrNull()
                ?.let { book ->
                    pageDataSource.upsertBook(book.copy(isSelected = !book.isSelected))
                }
        }
    }

    private fun onImportBook() {
        onEventSend(LibraryEvent.OnDrawerClose)
        onEventSend(LibraryEvent.OnImportBook)
    }

    private fun onScreenChange(screen: Screen) {
        onEventSend(LibraryEvent.OnDrawerClose)
        searchFieldState.clearText()
        _state.update { it.copy(
            screen = screen,
            screenType = ScreenType.LIST,
            isTabletSearchBarEnabled = false
        ) }
    }

    private fun onEventSend(event: LibraryEvent) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    private fun onImportFile(uri: Uri) {
        onEventSend(LibraryEvent.OnDrawerClose)
        viewModelScope.launch {
            _state.update {
                it.copy(
                    dialogType = DialogType.LOADING,
                    screen = Screen.LIBRARY
                )
            }
            xmlParser.parseBook(uri)
                .onSuccess { book ->
                    pageDataSource.upsertBook(book)
                    _state.update { it.copy(dialogType = DialogType.NONE) }
                }
                .onError { error ->
                    when (error) {
                        ParserError.PARSING_ERROR -> _state.update { it.copy(dialogType = DialogType.UNSUPPORTED_FORMAT) }
                        ParserError.DUPLICATE_ERROR -> _state.update { it.copy(dialogType = DialogType.DUPLICATE_DOCUMENT) }
                        ParserError.IO_ERROR -> Timber.e("Unable write file")
                        else -> {}
                    }
                }
        }
    }

    private fun onDelete(bookId: Int) {
        _state.update {
            it.copy(
                dialogType = DialogType.DELETE,
                booksPendingDeletion = state.value.books.filter { bookUi -> bookUi.id == bookId }
            )
        }
    }

    private fun onDeleteMultiple() {
        _state.update {
            it.copy(
                dialogType = DialogType.DELETE,
                booksPendingDeletion = state.value.books.filter { bookUi -> bookUi.isSelected }
            )
        }
    }

    private fun onConfirmDelete() {
        _state.update { it.copy(dialogType = DialogType.NONE) }
        viewModelScope.launch {
            state.value.booksPendingDeletion.let { bookUi ->
                pageDataSource
                    .observeBooksByIds(bookUi.map { it.id })
                    .firstOrNull()
                    ?.let { books ->
                        books.forEach { book ->
                            book.bookPath
                                ?.let {
                                    xmlParser.deleteBook(it) ?: Timber.e("Unable delete file: $it")
                                }
                        }
                        pageDataSource.deleteBook(books)
                    }
            }
        }
    }


    private fun onFavoriteClick(bookId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(screenType = ScreenType.LIST) }
            pageDataSource
                .observeBookById(bookId)
                .firstOrNull()
                ?.let { book ->
                    pageDataSource.upsertBook(book = book.copy(isFavorite = !book.isFavorite))
                }
        }
    }

    private fun onBookShareClick(bookId: Int) {
        viewModelScope.launch {
            pageDataSource
                .observeBookById(bookId)
                .firstOrNull()
                ?.let { book ->
                    if (book.bookPath == null) return@let
                    eventChannel.send(LibraryEvent.OnShareBook(book.bookPath))
                }
        }
    }

    private fun onMultipleBookShare() {
        viewModelScope.launch {
            state.value.books
                .filter { it.isSelected }
                .map { it.id }
                .let { ids ->
                    pageDataSource
                        .observeBooksByIds(ids)
                        .firstOrNull()
                        ?.map {
                            it.bookPath!!
                        }
                        ?.let {
                            eventChannel.send(LibraryEvent.OnShareMultipleBooks(it))
                        }
                }
        }
    }

    private fun onBooksFavoriteClick() {
        viewModelScope.launch {
            state.value.books
                .filter { it.isSelected }
                .forEach { bookUi ->
                    pageDataSource
                        .observeBookById(bookUi.id)
                        .firstOrNull()
                        ?.let { book ->
                            pageDataSource.upsertBook(book = book.copy(isFavorite = true))
                        }
                }
            _state.update { it.copy(screenType = ScreenType.LIST) }
        }
    }

    private fun onFinishedClick(bookId: Int) {
        _state.update { it.copy(screenType = ScreenType.LIST) }
        viewModelScope.launch {
            pageDataSource
                .observeBookById(bookId)
                .firstOrNull()
                ?.let { book ->
                    pageDataSource.upsertBook(book = book.copy(isFinished = !book.isFinished))
                }
        }
    }
}