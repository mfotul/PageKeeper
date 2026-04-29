package com.example.pagekeeper.pages.presentation.library.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.icons

@Composable
fun LibrarySelectedTopRow(
    selectedItemsCount: Int,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        IconButton(
            onClick = onBackClick
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_24),
                contentDescription = stringResource(R.string.menu),
                tint = MaterialTheme.colorScheme.icons
            )
        }
        Text(
            text = stringResource(R.string.selected_items, selectedItemsCount),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            letterSpacing = (-1).sp,
            modifier = Modifier.weight(1f)
        )
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
    }
}

@Preview
@Composable
private fun LibrarySelectedTopRowPreview() {
    PageKeeperTheme {
        LibrarySelectedTopRow(
            selectedItemsCount = 1,
            onBackClick = {},
            onFavoriteClick = {},
            onShareClick = {},
            onDeleteClick = {},
        )
    }
}