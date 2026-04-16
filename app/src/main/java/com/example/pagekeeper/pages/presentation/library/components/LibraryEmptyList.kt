package com.example.pagekeeper.pages.presentation.library.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pagekeeper.core.presentation.designsystem.button.PrimaryButton
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.R

@Composable
fun LibraryEmptyList(
    @StringRes title: Int,
    @StringRes description: Int,
    modifier: Modifier = Modifier,
    onImportClick: (() -> Unit)? = null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.image_32),
                contentDescription = stringResource(R.string.opened_book),
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = CircleShape
                    )
                    .padding(24.dp)
            )
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-1).sp
            )
            Text(
                text = stringResource(description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(250.dp)
            )
            onImportClick?.let { onClick ->
                PrimaryButton(
                    text = stringResource(R.string.import_book),
                    iconRes = R.drawable.import_book,
                    onClick = onClick
                )
            }

        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun LibraryEmptyListPreview() {
    PageKeeperTheme {
        LibraryEmptyList(
            title = R.string.your_favorites_is_empty,
            description = R.string.import_your_first_book_to_start_building_your_library,
            onImportClick = {}
        )
    }
}