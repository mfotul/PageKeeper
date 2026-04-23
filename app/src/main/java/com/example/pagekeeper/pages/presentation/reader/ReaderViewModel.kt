package com.example.pagekeeper.pages.presentation.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagekeeper.core.domain.util.onError
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.domain.library.XmlParser
import com.example.pagekeeper.pages.presentation.util.toSectionUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
                                sections = book.sections.map { it.toSectionUi() }
                            )
                        }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ReaderAction) {
        when (action) {
            ReaderAction.OnLockScreenClick -> TODO()
        }
    }
}
