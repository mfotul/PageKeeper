package com.example.pagekeeper.pages.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.domain.reader.Element
import com.example.pagekeeper.pages.domain.reader.Fb2BlockElement
import com.example.pagekeeper.pages.presentation.util.toChapterUi
import com.example.pagekeeper.pages.presentation.util.toContentsUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val bookId: Long,
    private val elementId: Long?,
    private val pageDataSource: PageDataSource
) : ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(NavigationState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadDefaultData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NavigationState()
        )

    private val eventChannel = Channel<NavigationEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun loadDefaultData() {
        combine(
            flow = pageDataSource.observerChaptersByBookId(bookId),
            flow2 = pageDataSource.observeBookById(bookId)
        ) { elements, book ->
            if (book == null || elementId == null) return@combine
            val contents = elements
                .asSequence()
                .filter { element ->
                    val content = element.content
                    if (content is Fb2BlockElement.Paragraph) {
                        content.text.firstOrNull()?.text?.startsWith(
                            "chapter",
                            ignoreCase = true
                        ) == true
                    } else {
                        true
                    }
                }
                .groupBy { it.bodyId }
                .mapValues { (_, elements) ->
                    elements
                        .groupBy { it.sectionId }
                        .filterValues { it.size == 1 }
                        .mapValues { (_, sectionElements) ->
                            sectionElements.map { element ->
                                element.toChapterUi()
                            }
                        }
                }.toContentsUi(bookName = book.title)


            _state.update { state ->
                state.copy(
                    contents = contents
                )
            }
        }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun onAction(action: NavigationAction) {
        when (action) {
            NavigationAction.OnBackClick -> {}
            is NavigationAction.OnChapterClick -> onChapterClick(action.elementId)
        }
    }

    private fun onChapterClick(elementId: Long) {
        viewModelScope.launch {
            pageDataSource
                .getIndexOfElementByBookId(bookId = bookId, elementId = elementId)
                .firstOrNull()
                ?.let { index ->
                    eventChannel.send(NavigationEvent.OnChapterSelected(index - 1))
                }
        }
    }

    private fun findChapterJustLower(elements: List<Element>, targetId: Long): Element? {
        val index = elements.binarySearchBy(targetId) { it.elementId }

        val lowerIndex = if (index >= 0) {
            index - 1
        } else {
            val insertionPoint = -(index + 1)
            insertionPoint - 1
        }
        return elements.getOrNull(lowerIndex)
    }
}