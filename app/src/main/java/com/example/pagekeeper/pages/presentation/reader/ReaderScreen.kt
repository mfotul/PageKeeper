@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pagekeeper.pages.presentation.reader

import android.content.pm.ActivityInfo
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.window.core.layout.WindowSizeClass
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.pages.domain.reader.BodyType
import com.example.pagekeeper.pages.presentation.preview.PreviewModel
import com.example.pagekeeper.pages.presentation.reader.components.ReaderBottomButtons
import com.example.pagekeeper.pages.presentation.reader.components.ReaderBottomSlider
import com.example.pagekeeper.pages.presentation.reader.components.ReaderSection
import com.example.pagekeeper.pages.presentation.reader.components.ReaderTitle
import com.example.pagekeeper.pages.presentation.reader.components.ReaderTopAppBar
import com.example.pagekeeper.pages.presentation.reader.models.SectionUi
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReaderScreenRoot(
    onBackClick: () -> Unit,
    viewModel: ReaderViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lazyPagingItems = viewModel.bookPager.collectAsLazyPagingItems()

    val activity = LocalActivity.current

    DisposableEffect(state.isAutRotate) {
        if (!state.isAutRotate)
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    ReaderScreen(
        state = state,
        lazyPagingItems = lazyPagingItems,
        onAction = {
            when (it) {
                ReaderAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(it)
            }
        }
    )
}

@Composable
fun ReaderScreen(
    state: ReaderState,
    lazyPagingItems: LazyPagingItems<SectionUi>,
    onAction: (ReaderAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var prevFontSize by remember { mutableStateOf(state.fontSize) }
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isTablet =
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
                && windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)

    LaunchedEffect(state.fonSizeChangeCounter) {
        if (state.fontSize != null && prevFontSize != null) {
            val ratio = state.fontSize / prevFontSize as Float

            val currentIndex = listState.firstVisibleItemIndex
            val currentOffset = listState.firstVisibleItemScrollOffset

            val newOffset = (currentOffset * ratio).toInt()
            listState.animateScrollToItem(currentIndex, newOffset)
        }
    }

    Scaffold(
        topBar = {
            ReaderTopAppBar(
                bookName = state.bookName,
                isVisible = state.areBarsVisible,
                isFavorite = state.isFavorite,
                onBackClick = { onAction(ReaderAction.OnBackClick) },
                onFavoriteClick = { onAction(ReaderAction.OnFavoritesClick) }
            )
        },
        bottomBar = {
            Box {
                if (state.isFontSliderVisible && state.fontSize != null)
                    ReaderBottomSlider(
                        fontSize = state.fontSize,
                        onFontSizeSet = { newSize ->
                            prevFontSize = state.fontSize
                            onAction(ReaderAction.OnFontSizeChange(newSize))
                        },
                        onSliderPositionChange = { onAction(ReaderAction.OnSliderPositionChange(it)) }
                    )
                else
                    ReaderBottomButtons(
                        isAutoRotate = state.isAutRotate,
                        isVisible = state.areBarsVisible,
                        isTablet = isTablet,
                        onRotateClick = { onAction(ReaderAction.OnLockScreenClick) },
                        onFonSizeClick = { onAction(ReaderAction.OnFontSizeClick) }
                    )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .fillMaxSize()
                .then(
                    if (state.areBarsVisible)
                        Modifier
                    else
                        Modifier
                            .padding(WindowInsets.statusBars.asPaddingValues())
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { onAction(ReaderAction.OnScreenClick) },
                )
        ) {
            items(
                count = lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey {
                    it.sectionId
                }) { index ->
                val section = lazyPagingItems[index] ?: return@items
                Column(
                    modifier = Modifier
                        .widthIn(max = 600.dp)

                ) {
                    when (section.body) {
                        BodyType.TITLE -> {
                            ReaderTitle(
                                fB2TitleUi = section.title,
                            )
                        }

                        BodyType.SECTION -> {
                            ReaderTitle(
                                fB2TitleUi = section.section?.title,
                            )
                            ReaderSection(
                                content = section.section?.content,
                            )
                        }
                    }
                }
            }

            item {
                if (lazyPagingItems.loadState.append == LoadState.Loading)
                    CircularProgressIndicator()
            }
        }
    }
}

@Preview(device = TABLET)
@Composable
private fun ReaderScreenPreview() {
    val sections = PreviewModel.section
    val lazyPagingItems = flowOf(PagingData.from(sections)).collectAsLazyPagingItems()
    PageKeeperTheme {
        ReaderScreen(
            state = ReaderState(
                bookName = "Alice's Adventures in Wonderland",
                areBarsVisible = true
            ),
            lazyPagingItems = lazyPagingItems,
            onAction = {}
        )
    }
}