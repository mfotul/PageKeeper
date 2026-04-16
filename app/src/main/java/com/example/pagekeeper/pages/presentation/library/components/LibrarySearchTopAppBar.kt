@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pagekeeper.pages.presentation.library.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.icons

@Composable
fun LibrarySearchTopAppBar(
    searchFieldState: TextFieldState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_arrow_back_24),
                    contentDescription = stringResource(R.string.menu),
                    tint = MaterialTheme.colorScheme.icons
                )
            }
        },
        title = {
            BasicTextField(
                state = searchFieldState,
                lineLimits = TextFieldLineLimits.SingleLine,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                ),
                decorator = { innerTextField ->
                    if (searchFieldState.text.isEmpty())
                        Text(
                            text = stringResource(R.string.search_books),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    innerTextField()
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondary),
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        actions = {
            if (searchFieldState.text.isNotEmpty())
                IconButton(
                    onClick = { searchFieldState.clearText() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_close_24),
                        contentDescription = stringResource(R.string.remove_search_query),
                        tint = MaterialTheme.colorScheme.icons
                    )
                }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        modifier = modifier
    )
}

@Preview
@Composable
private fun LibrarySearchTopAppBarPreview() {
    PageKeeperTheme {
        LibrarySearchTopAppBar(
            onBackClick = {},
            searchFieldState = rememberTextFieldState()
        )
    }
}