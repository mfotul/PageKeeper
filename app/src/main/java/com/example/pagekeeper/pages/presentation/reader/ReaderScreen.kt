@file:OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)

package com.example.pagekeeper.pages.presentation.reader

import android.content.pm.ActivityInfo
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.window.core.layout.WindowSizeClass
import com.example.pagekeeper.R
import com.example.pagekeeper.app.navigation.ResultStore
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.util.ObserveAsEvents
import com.example.pagekeeper.pages.presentation.preview.PreviewModel
import com.example.pagekeeper.pages.presentation.reader.components.ReaderBookItem
import com.example.pagekeeper.pages.presentation.reader.components.ReaderBottomButtons
import com.example.pagekeeper.pages.presentation.reader.components.ReaderBottomSlider
import com.example.pagekeeper.pages.presentation.reader.components.ReaderLinearProgressIndicator
import com.example.pagekeeper.pages.presentation.reader.components.ReaderTopAppBar
import com.example.pagekeeper.pages.presentation.reader.models.ElementUi
import com.example.pagekeeper.pages.presentation.util.thenIf
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReaderScreenRoot(
    onBackClick: () -> Unit,
    onChapterClick: (Long, Long?) -> Unit,
    onBookmarksClick: (Long) -> Unit,
    resultStore: ResultStore,
    viewModel: ReaderViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lazyPagingItems = viewModel.bookPager.collectAsLazyPagingItems()

    val activity = LocalActivity.current

    DisposableEffect(state?.isAutRotate) {
        state?.isAutRotate?.let {
            if (!it)
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ReaderEvent.OnBackClick -> onBackClick()
            is ReaderEvent.OnChapterClick -> onChapterClick(
                event.bookId,
                event.currentElementIdOnTop
            )

            is ReaderEvent.OnBookmarksClick -> onBookmarksClick(event.bookId)
        }
    }


    state?.let { state ->
        if (lazyPagingItems.itemCount > 0)
            ReaderScreen(
                state = state,
                resultStore = resultStore,
                lazyPagingItems = lazyPagingItems,
                onAction = viewModel::onAction
            )
    }
}

@Composable
fun ReaderScreen(
    state: ReaderState,
    resultStore: ResultStore,
    lazyPagingItems: LazyPagingItems<ElementUi>,
    onAction: (ReaderAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = state.readingPositionIndex,
        initialFirstVisibleItemScrollOffset = state.readingPositionOffset
    )
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isTablet =
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
                && windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)
    val navState = rememberNavigationEventState(NavigationEventInfo.None)
    var progress by remember { mutableFloatStateOf(0f) }
    var bookmarkColor by remember { mutableStateOf<Color?>(null) }

    NavigationBackHandler(
        state = navState
    ) {
        onAction(
            ReaderAction.OnBackClick(
                readingPositionIndex = listState.firstVisibleItemIndex,
                readingPositionOffset = listState.firstVisibleItemScrollOffset,
                readingProgress = progress
            )
        )
    }

    LaunchedEffect(listState, state.elementCount) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collectLatest { firstVisibleItemIndex ->
                bookmarkColor = state.bookmarks[firstVisibleItemIndex]?.color
                progress =
                    if (!listState.canScrollForward) 1f else firstVisibleItemIndex / state.elementCount.toFloat()
            }
    }

    LaunchedEffect(resultStore) {
        val positionIndex = resultStore.getResult<Int>("positionIndex")
        val positionOffset = resultStore.getResult<Int>("positionOffset")
        if (positionIndex != null) {
            onAction(ReaderAction.OnChapterSelected(positionIndex))
            if (positionOffset != null) {
                listState.animateScrollToItem(positionIndex, positionOffset)
                resultStore.removeResult("positionOffset")
            } else
                listState.animateScrollToItem(positionIndex)
            resultStore.removeResult("positionIndex")
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(),
        modifier = modifier
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .thenIf(!state.areBarsVisible) {
                    systemBarsPadding()
                }
        ) {
            AnimatedVisibility(
                visible = state.areBarsVisible,
                enter = slideInVertically(
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = LinearOutSlowInEasing
                    ),
                    initialOffsetY = { -it }
                ),
                exit = slideOutVertically(
                    animationSpec = snap(),
                    targetOffsetY = { -it }
                )
            ) {
                ReaderTopAppBar(
                    bookName = state.bookName,
                    isFavorite = state.isFavorite,
                    onBackClick = {
                        onAction(
                            ReaderAction.OnBackClick(
                                readingPositionIndex = listState.firstVisibleItemIndex,
                                readingPositionOffset = listState.firstVisibleItemScrollOffset,
                                readingProgress = progress
                            )
                        )
                    },
                    onFavoriteClick = { onAction(ReaderAction.OnFavoritesClick) }
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                LazyColumn(
                    state = listState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { onAction(ReaderAction.OnScreenClick) },
                        )
                ) {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = lazyPagingItems.itemKey {
                            it.elementId
                        }) { index ->
                        val element = lazyPagingItems[index]
                        Column(
                            modifier = Modifier
                                .thenIf(isTablet) {
                                    widthIn(max = 600.dp)
                                }
                        ) {
                            if (element != null)
                                ReaderBookItem(element.content)
                            else
                                Spacer(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                )
                        }
                    }

                    item {
                        if (lazyPagingItems.loadState.append == LoadState.Loading)
                            CircularProgressIndicator()
                    }
                }
                bookmarkColor?.let { color ->
                    Icon(
                        painter = painterResource(R.drawable.bookmark_color),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    )
                }
            }
            AnimatedVisibility(
                visible = !state.areBarsVisible,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 700,
                        delayMillis = 200
                    )
                ),
                exit = fadeOut(
                    animationSpec = snap()
                )
            ) {
                ReaderLinearProgressIndicator(
                    progress = { progress },
                )
            }

            AnimatedVisibility(
                visible = state.areBarsVisible,
                enter = slideInVertically(
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = LinearOutSlowInEasing
                    ),
                    initialOffsetY = { it }
                ),
                exit = slideOutVertically(
                    animationSpec = snap(),
                    targetOffsetY = { it }
                )
            ) {
                if (state.isFontSliderVisible)
                    ReaderBottomSlider(
                        fontSize = state.fontSize,
                        onFontSizeSet = { newSize ->
                            onAction(
                                ReaderAction.OnFontSizeChange(fontSize = newSize)
                            )
                        },
                        onSliderPositionChange = {
                            onAction(
                                ReaderAction.OnSliderPositionChange(
                                    it
                                )
                            )
                        }
                    )
                else
                    ReaderBottomButtons(
                        isAutoRotate = state.isAutRotate,
                        isTablet = isTablet,
                        progress = { progress },
                        onRotateClick = { onAction(ReaderAction.OnLockScreenClick) },
                        onFonSizeClick = { onAction(ReaderAction.OnFontSizeClick) },
                        onChaptersClick = {
                            val index = listState.firstVisibleItemIndex
                            val elementUi = if (index < lazyPagingItems.itemCount)
                                lazyPagingItems[index]
                            else
                                null
                            onAction(ReaderAction.OnChapterClick(elementUi))
                        },
                        onBookmarksClick = {
                            onAction(
                                ReaderAction.OnBookmarksClick(
                                    readingPositionIndex = listState.firstVisibleItemIndex,
                                    readingPositionOffset = listState.firstVisibleItemScrollOffset,
                                    readingProgress = progress
                                )
                            )
                        }
                    )
            }

        }
    }
}

@Preview(device = PHONE)
@Composable
private fun ReaderScreenPreview() {
    val elements = PreviewModel.element
    val lazyPagingItems = flowOf(PagingData.from(elements)).collectAsLazyPagingItems()
    PageKeeperTheme {
        ReaderScreen(
            state = ReaderState(
                bookName = "Alice's Adventures in Wonderland",
                areBarsVisible = false,
                readingPositionIndex = 0,
                readingPositionOffset = 0,
                fontSize = 18f,
                isFavorite = false,
                elementCount = 100
            ),
            lazyPagingItems = lazyPagingItems,
            resultStore = ResultStore(),
            onAction = {}
        )
    }
}