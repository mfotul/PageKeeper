package com.example.pagekeeper.pages.presentation.reader

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pagekeeper.pages.domain.reader.BodyType
import com.example.pagekeeper.pages.presentation.preview.PreviewModel
import com.example.pagekeeper.pages.presentation.reader.components.ReaderSection
import com.example.pagekeeper.pages.presentation.reader.components.ReaderTitle
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReaderScreenRoot(
    viewModel: ReaderViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ReaderScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ReaderScreen(
    state: ReaderState,
    onAction: (ReaderAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .padding(innerPadding)
        ) {
            items(items = state.sections, key = { it.sectionId }) {section ->
                when(section.body) {
                    BodyType.TITLE -> {
                        ReaderTitle(
                            section.title
                        )
                    }
                    BodyType.SECTION -> {
                        ReaderTitle(section.section?.title)
                        ReaderSection(section.section)
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