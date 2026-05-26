@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pagekeeper.pages.presentation.reader.components

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.bgBottomNav
import com.example.pagekeeper.core.presentation.designsystem.theme.icons

@Composable
fun ReaderTopAppBar(
    bookName: String,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_arrow_back_24),
                    contentDescription = stringResource(R.string.back),
                )
            }
        },
        title = {
            Text(
                text = bookName,
                style = MaterialTheme.typography.titleMedium,
                letterSpacing = (-1).sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            IconButton(
                onClick = onFavoriteClick
            ) {
                Icon(
                    painter = painterResource(
                        if (isFavorite)
                            R.drawable.menu_favorites_active
                        else
                            R.drawable.favorites
                    ),
                    contentDescription = stringResource(R.string.favorites_label),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.bgBottomNav,
            navigationIconContentColor = MaterialTheme.colorScheme.icons,
            actionIconContentColor = MaterialTheme.colorScheme.icons,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}

@Preview
@Composable
private fun ReaderTopAppBarPreview() {
    PageKeeperTheme {
        ReaderTopAppBar(
            bookName = "very very long long book name very, very long long book name",
            isFavorite = true,
            onBackClick = {},
            onFavoriteClick = {}
        )
    }
}