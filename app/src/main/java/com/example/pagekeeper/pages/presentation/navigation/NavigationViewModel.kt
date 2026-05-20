@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.pagekeeper.pages.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagekeeper.pages.data.navigation.toChapter
import com.example.pagekeeper.pages.data.navigation.toContents
import com.example.pagekeeper.pages.domain.library.Book
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.domain.navigation.Content
import com.example.pagekeeper.pages.domain.reader.Element
import com.example.pagekeeper.pages.domain.reader.Fb2BlockElement
import com.example.pagekeeper.pages.domain.reader.StyledText
import com.example.pagekeeper.pages.presentation.navigation.models.ChapterUi
import com.example.pagekeeper.pages.presentation.navigation.models.ContentUi
import com.example.pagekeeper.pages.presentation.util.toContentUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        pageDataSource
            .observeContentsByBookId(bookId)
            .onEach { contents ->
                if (elementId == null) return@onEach

                if (contents.isEmpty()) {
                    generateContents()
                    return@onEach
                }

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
                .getIndexOfElementByBookId(bookId = bookId, elementId = elementId)
                .firstOrNull()
                ?.let { index ->
                    eventChannel.send(NavigationEvent.OnChapterSelected(index - 1))
                }
        }
    }

    private fun generateContents() {
        viewModelScope.launch {
            pageDataSource
                .observeBookById(bookId)
                .firstOrNull()
                ?.let { book ->
                    val elements = pageDataSource
                        .observerChaptersByBookId(bookId)
                        .firstOrNull()
                        ?.let {
                            it.ifEmpty {
                                pageDataSource.observerChaptersByBookIdAndSectionId(bookId)
                                    .firstOrNull()
                            }
                        } ?: emptyList()

                    val contents = elementsToContents(elements, book)

                    contents.forEach {
                        pageDataSource.insertContentWithChapters(it)
                    }
                }
        }
    }

    private fun elementsToContents(
        elements: List<Element>,
        book: Book
    ): List<Content> {
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
                            element.toChapter()
                        }
                    }
            }.toContents(book)
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

    private fun contentWithSelectedChapter(
        contents: List<ContentUi>,
        targetId: Long
    ): Pair<Int, List<ContentUi>> {
        val chapters = contents.flatMap { content -> content.chapters }
        val targetChapter = findChapterJustLower(chapters, targetId)

        var expandedContentId = contents[0].id
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