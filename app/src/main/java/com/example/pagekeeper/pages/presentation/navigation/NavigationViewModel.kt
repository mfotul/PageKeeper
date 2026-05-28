@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.pagekeeper.pages.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.presentation.models.ChapterUi
import com.example.pagekeeper.pages.presentation.navigation.models.ContentUi
import com.example.pagekeeper.pages.presentation.util.toContentUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val bookId: Long,
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
        pageDataSource
            .observeBookById(bookId)
            .flatMapLatest { book ->
                if (book != null)
                    pageDataSource.observeContentsByBookId(bookId).map { contents ->
                        contents to book.currentElementId
                    }
                else
                    emptyFlow()
            }
            .onEach { (contents, elementId) ->
                if (elementId == null) return@onEach

                val (expandedContentId, contentWithTargetChapter) = contentWithSelectedChapter(
                    contents = contents.map { it.toContentUi() },
                    targetId = elementId
                )

                _state.update { state ->
                    state.copy(
                        contents = contentWithTargetChapter,
                        expandedContentId = expandedContentId
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: NavigationAction) {
        when (action) {
            NavigationAction.OnBackClick -> {}
            is NavigationAction.OnChapterClick -> onChapterClick(action.elementId)
            is NavigationAction.OnTitleClick -> onTitleClick(action.contentUi)
        }
    }

    private fun onTitleClick(contentUi: ContentUi) {
        _state.update { state ->
            state.copy(
                expandedContentId = contentUi.id
            )
        }
    }

    private fun onChapterClick(elementId: Long) {
        viewModelScope.launch {
            pageDataSource
                .getPositionInBook(bookId = bookId, elementId = elementId)
                .firstOrNull()
                ?.let { index ->
                    eventChannel.send(NavigationEvent.OnChapterSelected(index - 1))
                }
        }
    }

    private fun contentWithSelectedChapter(
        contents: List<ContentUi>,
        targetId: Long
    ): Pair<Int, List<ContentUi>> {
        val chapters = contents.flatMap { content -> content.chapters }
        val targetChapter = findChapterJustLower(chapters, targetId)

        var expandedContentId = contents.getOrNull(0)?.id ?: -1
        val contentWithTargetChapter = contents.map { content ->
            val chapters = content.chapters.map { chapter ->
                if (chapter.elementId == targetChapter?.elementId) {
                    expandedContentId = content.id
                    chapter.copy(isSelected = true)
                } else {
                    chapter
                }
            }
            content.copy(chapters = chapters)
        }
        return expandedContentId to contentWithTargetChapter
    }

    private fun findChapterJustLower(chapters: List<ChapterUi>, targetId: Long?): ChapterUi? {
        val index = chapters.binarySearchBy(targetId) { it.elementId }
        val lowerIndex = if (index >= 0) {
            index - 1
        } else {
            val insertionPoint = index.inv()
            insertionPoint - 1
        }
        return chapters.getOrNull(lowerIndex)
    }
}