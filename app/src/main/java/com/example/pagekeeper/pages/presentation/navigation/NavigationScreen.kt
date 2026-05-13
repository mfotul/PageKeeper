package com.example.pagekeeper.pages.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pagekeeper.app.navigation.ResultStore
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.util.ObserveAsEvents
import com.example.pagekeeper.pages.presentation.navigation.components.NavigationChapter
import com.example.pagekeeper.pages.presentation.navigation.components.NavigationTopAppBar
import com.example.pagekeeper.pages.presentation.preview.PreviewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationScreenRoot(
    onBackClick: () -> Unit,
    resultStore: ResultStore,
    viewModel: NavigationViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is NavigationEvent.OnChapterSelected -> {
                resultStore.setResult("chapterIndex", event.index)
                onBackClick()
            }
        }
    }

    NavigationScreen(
        state = state,
        onAction = { action ->
            when (action) {
                NavigationAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun NavigationScreen(
    state: NavigationState,
    onAction: (NavigationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            NavigationTopAppBar(
                onBackClick = { onAction(NavigationAction.OnBackClick) }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier
                .padding(innerPadding)
        ) {
            itemsIndexed(items = state.contents, key = { _, contentUi -> contentUi.title }) { index, contentUi ->
                NavigationChapter(
                    contentUi = contentUi,
                    isLast = index == state.contents.lastIndex,
                    onChapterClick = { chapterId ->
                        onAction(NavigationAction.OnChapterClick(chapterId))
                    },
                    onTitleClick = {
                        onAction(NavigationAction.OnTitleClick(contentUi))
                    },
                    isExpanded = state.expandedContentId == contentUi.id
                )
            }
        }
    }
}

@Preview
@Composable
private fun NavigationScreenPreview() {
    PageKeeperTheme {
        NavigationScreen(
            state = NavigationState(
                contents = PreviewModel.contents,
                expandedContentId = PreviewModel.contents[0].id
            ),
            onAction = {}
        )
    }
}