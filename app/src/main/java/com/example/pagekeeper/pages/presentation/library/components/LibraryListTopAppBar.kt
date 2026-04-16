@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pagekeeper.pages.presentation.library.components

import androidx.annotation.StringRes
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
fun LibraryListTopAppBar(
    @StringRes title: Int,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onMenuClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_menu_24),
                    contentDescription = stringResource(R.string.menu),
                    tint = MaterialTheme.colorScheme.icons
                )
            }
        },
        title = {
            Text(
                text = stringResource(title),
                letterSpacing = (-1).sp,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        actions = {
            IconButton(
                onClick = onSearchClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_search_24),
                    contentDescription = stringResource(R.string.menu),
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
private fun LibraryListTopAppBarPreview() {
    PageKeeperTheme {
        LibraryListTopAppBar(
            title = R.string.library,
            onMenuClick = {},
            onSearchClick = {},
        )
    }
}