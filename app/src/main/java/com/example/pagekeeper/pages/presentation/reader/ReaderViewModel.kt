package com.example.pagekeeper.pages.presentation.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagekeeper.core.domain.util.onError
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.domain.library.XmlParser
import com.example.pagekeeper.pages.presentation.util.toSectionUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ReaderViewModel(
    private val bookId: Int,
    private val xmlParser: XmlParser,
    private val pageDataSource: PageDataSource
) : ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ReaderState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData)
                loadInitialData()
            hasLoadedInitialData = true
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ReaderState()
        )

    private val eventChannel = Channel<ReaderEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun loadInitialData() {
        pageDataSource
            .observeBookById(bookId)
            .onEach { book ->
                book?.let { book ->
                    if (book.sections.isEmpty())
                        xmlParser
                            .parseBookBodyFile(book)
                            .onError {
                                Timber.e("Error $it")
                            }
                    else
                        _state.update { state ->
                            state.copy(
                                bookName = book.title,
                                sections = book.sections.map { it.toSectionUi() }
                            )
                        }
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
            ReaderAction.OnFontSizeClick -> {}
        }
    }

    private fun onLockScreen() {
        viewModelScope.launch {
            eventChannel.send(ReaderEvent.OnDeviceScreenLock)
            _state.update {
                it.copy(isAutRotate = !it.isAutRotate)

            }
        }
    }

    private fun onScreenClick() {
        _state.update {
            it.copy(areBarsVisible = !it.areBarsVisible)
        }
    }
}
