@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.pagekeeper.pages.presentation.reader

import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.pagekeeper.core.database.pages.library.PageDao
import com.example.pagekeeper.core.domain.util.onError
import com.example.pagekeeper.pages.data.reader.toElement
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.domain.library.PagePreferences
import com.example.pagekeeper.pages.domain.library.XmlParser
import com.example.pagekeeper.pages.presentation.models.ColorItem
import com.example.pagekeeper.pages.presentation.reader.models.ElementUi
import com.example.pagekeeper.pages.presentation.util.toElementUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ReaderViewModel(
    private val bookId: Long,
    private val xmlParser: XmlParser,
    private val pageDao: PageDao,
    private val pageDataSource: PageDataSource,
    private val readerPreferences: PagePreferences
) : ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow<ReaderState?>(null)
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observerBook()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            null
        )

    val bookPager = state
        .filterNotNull()
        .distinctUntilChanged { old, new ->
            old.fontSize == new.fontSize && old.readingPositionIndex == new.readingPositionIndex
        }
        .flatMapLatest { state ->
            Pager(
                config = PagingConfig(
                    pageSize = 100,
                ),
                initialKey = state.readingPositionIndex,
                pagingSourceFactory = {
                    pageDao
                        .observeElementsByBookId(bookId)
                }
            ).flow
                .map { pagingData ->
                    pagingData.map {
                        it.toElement().toElementUi(fontSize = state.fontSize.sp)
                    }
                }
        }
        .cachedIn(viewModelScope)

    private val eventChannel = Channel<ReaderEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun observerBook() {
        combine(
            pageDataSource.getBookTitleWithCount(bookId),
            readerPreferences.observerFontSize(),
            pageDataSource.observeBookmarksByBookId(bookId)
        ) { book, fontSize, bookmarks ->
            if (book == null) return@combine
            _state.update { state ->
                val newState = state ?: ReaderState(
                    bookName = "",
                    isFavorite = false,
                    fontSize = 0f,
                    readingPositionIndex = 0,
                    readingPositionOffset = 0,
                    elementCount = 0
                )
                newState.copy(
                    bookName = book.title,
                    isFavorite = book.isFavorite,
                    fontSize = fontSize,
                    readingPositionIndex = book.readingPositionIndex,
                    readingPositionOffset = book.readingPositionOffset,
                    elementCount = book.elementCount ?: 0,
                    bookmarks = bookmarks.associate {
                        it.readingPositionIndex to ColorItem.valueOf(it.colorItem)
                    }
                )
            }

            if (book.elementCount == 0)
                viewModelScope.launch {
                    xmlParser
                        .parseBookBodyFile(book.bookId!!)
                        .onError {
                            Timber.e("Error $it")
                        }
                }
        }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ReaderAction) {
        when (action) {
            ReaderAction.OnLockScreenClick -> onLockScreen()
            is ReaderAction.OnBackClick -> onSaveAndNavigate(
                readingPositionIndex = action.readingPositionIndex,
                readingPositionOffset = action.readingPositionOffset,
                readingProgress = action.readingProgress,
                event = ReaderEvent.OnBackClick
            )

            ReaderAction.OnFavoritesClick -> onFavoriteClick()
            ReaderAction.OnScreenClick -> onScreenClick()
            ReaderAction.OnFontSizeClick -> onFontSizeClick()
            is ReaderAction.OnFontSizeChange -> onFontChange(
                fontSize = action.fontSize,
                changeFontSize = true,
            )

            is ReaderAction.OnSliderPositionChange -> onFontChange(
                fontSize = action.fontSize,
                changeFontSize = false,
            )

            is ReaderAction.OnChapterClick -> onChapterClick(action.currentElementOnTop)
            is ReaderAction.OnChapterSelected -> onChapterSelected(action.index)
            is ReaderAction.OnBookmarksClick -> onSaveAndNavigate(
                readingPositionIndex = action.readingPositionIndex,
                readingPositionOffset = action.readingPositionOffset,
                readingProgress = action.readingProgress,
                event = ReaderEvent.OnBookmarksClick(bookId)
            )
        }
    }

    private fun onChapterSelected(index: Int) {
        _state.update {
            it?.copy(readingPositionIndex = index)
        }
    }

    private fun onChapterClick(currentElementOnTop: ElementUi?) {
        viewModelScope.launch {
            eventChannel.send(ReaderEvent.OnChapterClick(bookId, currentElementOnTop?.elementId))
        }
    }

    private fun onSaveAndNavigate(
        readingPositionIndex: Int,
        readingPositionOffset: Int,
        readingProgress: Float,
        event: ReaderEvent
    ) {
        viewModelScope.launch {
            pageDataSource
                .observeBookById(bookId)
                .firstOrNull()
                ?.let { book ->
                    pageDataSource.upsertBook(
                        book = book.copy(
                            readingPositionIndex = readingPositionIndex,
                            readingPositionOffset = readingPositionOffset,
                            readingProgress = readingProgress
                        )
                    )
                }
            eventChannel.send(event)
        }
    }

    private fun onFavoriteClick() {
        viewModelScope.launch {
            pageDataSource
                .observeBookById(bookId)
                .firstOrNull()
                ?.let { book ->
                    pageDataSource.upsertBook(book = book.copy(isFavorite = !book.isFavorite))
                }
        }
    }

    private fun onFontChange(
        fontSize: Float,
        changeFontSize: Boolean,
    ) {
        val newFontSize = fontSize.toInt().toFloat().coerceIn(10f, 40f)

        if (changeFontSize)
            viewModelScope.launch {
                readerPreferences.saveFontSize(newFontSize)
                _state.update { it?.copy(fonSizeChangeCounter = it.fonSizeChangeCounter + 1) }
            }
        else
            _state.update { it?.copy(fontSize = newFontSize) }
    }

    private fun onFontSizeClick() {
        _state.update {
            it?.copy(isFontSliderVisible = true)
        }
    }

    private fun onLockScreen() {
        viewModelScope.launch {
            _state.update {
                it?.copy(isAutRotate = !it.isAutRotate)
            }
        }
    }

    private fun onScreenClick() {
        _state.update {
            it?.copy(
                areBarsVisible = !it.areBarsVisible,
                isFontSliderVisible = false
            )
        }
    }
}
