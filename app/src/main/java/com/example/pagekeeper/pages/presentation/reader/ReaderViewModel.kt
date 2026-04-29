@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)

package com.example.pagekeeper.pages.presentation.reader

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.pagekeeper.core.database.pages.library.PageDao
import com.example.pagekeeper.core.domain.util.onError
import com.example.pagekeeper.pages.data.reader.toSection
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.domain.library.XmlParser
import com.example.pagekeeper.pages.domain.reader.ReaderPreferences
import com.example.pagekeeper.pages.presentation.util.toSectionUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ReaderViewModel(
    private val bookId: Int,
    private val xmlParser: XmlParser,
    private val pageDao: PageDao,
    private val pageDataSource: PageDataSource,
    private val readerPreferences: ReaderPreferences
) : ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ReaderState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeSettings()
                observerBook()
            }
            hasLoadedInitialData = true
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ReaderState()
        )

    val bookPager = state
        .map { it.fonSizeChangeCounter }
        .distinctUntilChanged()
        .flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 1),
                pagingSourceFactory = {
                    pageDao
                        .observeSectionsByBookIdPaginated(bookId)
                }
            ).flow
        }
        .map { pagingData ->
            pagingData.map {
                it.toSection().toSectionUi(fontSize = state.value.fontSize.sp)
            }
        }
        .cachedIn(viewModelScope)

    private fun observerBook() {
        pageDataSource
            .observeBookById(bookId)
            .onEach { book ->
                book?.let { book ->
                    if (book.sections.isEmpty())
                        viewModelScope.launch {
                            xmlParser
                                .parseBookBodyFile(book)
                                .onError {
                                    Timber.e("Error $it")
                                }
                        }
                    _state.update { state ->
                        state.copy(
                            bookName = book.title
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeSettings() {
        readerPreferences
            .observerFontSize()
            .onEach { newFontSize ->
                _state.update {
                    it.copy(
                        fontSize = newFontSize,
                        fonSizeChangeCounter = it.fonSizeChangeCounter + 1
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ReaderAction) {
        when (action) {
            ReaderAction.OnLockScreenClick -> onLockScreen()
            ReaderAction.OnBackClick -> {}
            ReaderAction.OnFavoritesClick -> {}
            ReaderAction.OnScreenClick -> onScreenClick()
            ReaderAction.OnFontSizeClick -> onFontSizeClick()
            is ReaderAction.OnFontSizeChange -> onFontChange(action.fontSize, true)
            is ReaderAction.OnSliderPositionChange -> onFontChange(action.fontSize, false)
        }
    }

    private fun onFontChange(fontSize: Float, changeFontSize: Boolean) {
        val newFontSize = fontSize.toInt().toFloat().coerceIn(10f, 40f)

        if (changeFontSize)
            viewModelScope.launch {
                readerPreferences.saveFontSize(newFontSize)
            }
        else
            _state.update { it.copy(fontSize = newFontSize) }
    }

    private fun onFontSizeClick() {
        _state.update { it.copy(isFontSliderVisible = true) }
    }

    private fun onLockScreen() {
        viewModelScope.launch {
            _state.update {
                it.copy(isAutRotate = !it.isAutRotate)
            }
        }
    }

    private fun onScreenClick() {
        _state.update {
            it.copy(
                areBarsVisible = !it.areBarsVisible,
                isFontSliderVisible = false
            )
        }
    }
}
