@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pagekeeper.pages.presentation.library.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.icons

@Composable
fun LibrarySelectedTopAppBar(
    selectedItemsCount: Int,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit,
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
            Text(
                text = stringResource(R.string.selected_items, selectedItemsCount),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                letterSpacing = (-1).sp
            )
        },
        actions = {
            IconButton(
                onClick = onFavoriteClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.favorites),
                    contentDescription = stringResource(R.string.favorites_label),
                    tint = MaterialTheme.colorScheme.icons
                )
            }
            IconButton(
                onClick = onShareClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.share),
                    contentDescription = stringResource(R.string.share),
                    tint = MaterialTheme.colorScheme.icons
                )
            }
            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.rounded_delete_24),
                    contentDescription = stringResource(R.string.delete),
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
private fun LibrarySelectedTopAppBarPreview() {
    PageKeeperTheme {
        LibrarySelectedTopAppBar(
            onBackClick = {},
            onFavoriteClick = {},
            onShareClick = {},
            onDeleteClick = {},
            selectedItemsCount = 2
        )
    }
}