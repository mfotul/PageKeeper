@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pagekeeper.pages.presentation.reader

import android.content.pm.ActivityInfo
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pagekeeper.pages.domain.reader.BodyType
import com.example.pagekeeper.pages.presentation.preview.PreviewModel
import com.example.pagekeeper.pages.presentation.reader.components.ReaderBottomButtons
import com.example.pagekeeper.pages.presentation.reader.components.ReaderBottomSlider
import com.example.pagekeeper.pages.presentation.reader.components.ReaderSection
import com.example.pagekeeper.pages.presentation.reader.components.ReaderTitle
import com.example.pagekeeper.pages.presentation.reader.components.ReaderTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReaderScreenRoot(
    onBackClick: () -> Unit,
    viewModel: ReaderViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
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
    onAction: (ReaderAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            ReaderTopAppBar(
                bookName = state.bookName,
                isVisible = state.areBarsVisible,
                onBackClick = { onAction(ReaderAction.OnBackClick) },
                onFavoriteClick = { onAction(ReaderAction.OnFavoritesClick) }
            )
        },
        bottomBar = {
            Box {
                if (state.isFontSliderVisible && state.fontSize != null)
                    ReaderBottomSlider(
                        fontSize = state.fontSize,
                        onFontSizeSet = { onAction(ReaderAction.OnFontSizeChange(it)) },
                        onSliderPositionChange = { onAction(ReaderAction.OnSliderPositionChange(it)) }
                    )
                else
                    ReaderBottomButtons(
                        isAutoRotate = state.isAutRotate,
                        isVisible = state.areBarsVisible,
                        onRotateClick = { onAction(ReaderAction.OnLockScreenClick) },
                        onFonSizeClick = { onAction(ReaderAction.OnFontSizeClick) }
                    )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .padding(innerPadding)
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
            items(items = state.sections, key = { it.sectionId }) { section ->
                when (section.body) {
                    BodyType.TITLE -> {
                        ReaderTitle(
                            section.title
                        )
                    }

                    BodyType.SECTION -> {
                        ReaderTitle(section.section?.title)
                        ReaderSection(section.section?.content)
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun ReaderScreenPreview() {
    ReaderScreen(
        state = ReaderState(sections = PreviewModel.section),
        onAction = {}
    )
}