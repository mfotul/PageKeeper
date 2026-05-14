package com.example.pagekeeper.pages.presentation.library.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme

@Composable
fun LibraryFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.read),
            contentDescription = stringResource(R.string.continue_reading),
            tint = MaterialTheme.colorScheme.background
        )
    }
}

@Preview
@Composable
private fun LibraryFloatingActionButtonPreview() {
    PageKeeperTheme {
        LibraryFloatingActionButton(
            {}
        )
    }
}