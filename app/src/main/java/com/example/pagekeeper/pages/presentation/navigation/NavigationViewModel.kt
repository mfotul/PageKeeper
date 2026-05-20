@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.pagekeeper.pages.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.domain.reader.Element
import com.example.pagekeeper.pages.domain.reader.Fb2BlockElement
import com.example.pagekeeper.pages.domain.reader.StyledText
import com.example.pagekeeper.pages.presentation.navigation.models.ChapterUi
import com.example.pagekeeper.pages.presentation.navigation.models.ContentUi
import com.example.pagekeeper.pages.presentation.util.toChapterUi
import com.example.pagekeeper.pages.presentation.util.toContentsUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
    private var backupSource = false

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
            flow = pageDataSource.observerChaptersByBookId(bookId)
                .flatMapLatest { list ->
                    if (list.isEmpty()) {
                        backupSource = true
                        pageDataSource.observerChaptersByBookIdAndSectionId(bookId)
                    } else {
                        backupSource = false
                        flowOf(list)
                    }
                },
            flow2 = pageDataSource.observeBookById(bookId)
        ) { elements, book ->
            if (book == null || elementId == null) return@combine
            val contents = elementsToContentsUi(elements, book.title)

            val (expandedContentId, contentWithTargetChapter) = contentWithSelectedChapter(
                contents = contents,
                targetId = elementId
            )

            _state.update { state ->
                state.copy(
                    contents = contentWithTargetChapter,
                    expandedContentId = expandedContentId
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
                .getIndexOfElementByBookId(bookId = bookId, elementId = elementId)
                .firstOrNull()
                ?.let { index ->
                    eventChannel.send(NavigationEvent.OnChapterSelected(index - 1))
                }
        }
    }

    private fun findChapterJustLower(chapters: List<ChapterUi>, targetId: Long): ChapterUi? {
        val index = chapters.binarySearchBy(targetId) { it.elementId }

        val lowerIndex = if (index >= 0) {
            index - 1
        } else {
            val insertionPoint = -(index + 1)
            insertionPoint - 1
        }
        return chapters.getOrNull(lowerIndex)
    }

    private fun elementsToContentsUi(
        elements: List<Element>,
        title: String
    ): List<ContentUi> {
        return elements
            .filter { element ->
                val content = element.content
                if (content is Fb2BlockElement.Paragraph) {
                    content.text.firstOrNull()?.text?.startsWith(
                        "chapter",
                        ignoreCase = true
                    ) == true || backupSource
                } else {
                    true
                }
            }
            .let { elements ->
                if (backupSource)
                    elements.mapIndexed { index, element ->
                        element.copy(
                            content = Fb2BlockElement.Title(
                                lines = listOf(listOf(StyledText(text = "Chapter ${index + 1}")))
                            )
                        )
                    }
                else
                    elements
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
            }.toContentsUi(bookName = title)
    }

    private fun contentWithSelectedChapter(
        contents: List<ContentUi>,
        targetId: Long
    ): Pair<Int, List<ContentUi>> {
        val chapters = contents.flatMap { content -> content.chapters }
        val targetChapter = findChapterJustLower(chapters, targetId)

        var expandedContentId = 0
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
}