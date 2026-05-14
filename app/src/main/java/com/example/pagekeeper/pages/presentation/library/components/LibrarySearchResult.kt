@file:OptIn(ExperimentalLayoutApi::class)

package com.example.pagekeeper.pages.presentation.library.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.pages.presentation.models.BookUi
import com.example.pagekeeper.pages.presentation.util.thenIf

@Composable
fun LibrarySearchResult(
    bookUis: List<BookUi>,
    isTablet: Boolean,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val columns = GridCells.Fixed(if (isTablet) 2 else 1)

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .fillMaxWidth()
                .thenIf(isTablet) {
                    padding(horizontal = 16.dp)
                }
        )
        if (bookUis.isEmpty())
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.weight(0.1f))
                Text(
                    text = stringResource(R.string.no_result_found),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.weight(0.9f))
            }
        else
            LazyVerticalGrid(
                columns = columns,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(items = bookUis, key = { it.id }) { bookUi ->
                    LibrarySmallBookCard(
                        bookUi = bookUi,
                        onClick = { onClick(bookUi.id) }
                    )
                }
            }
    }
}

@Preview(showBackground = true)
@Composable
private fun LibrarySearchResultPreview() {
    PageKeeperTheme {
        LibrarySearchResult(
//            bookUis = PreviewModel.books,
            bookUis = emptyList(),
            isTablet = false,
            onClick = {}
        )
    }
}