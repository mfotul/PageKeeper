package com.example.pagekeeper.pages.presentation.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.button.PrimaryButton
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.icons
import com.example.pagekeeper.core.presentation.designsystem.theme.loaderSecondary
import com.example.pagekeeper.pages.presentation.models.BookUi
import com.example.pagekeeper.pages.presentation.preview.PreviewModel
import com.example.pagekeeper.pages.presentation.util.thenIf

@Composable
fun LibraryBookCard(
    bookUi: BookUi,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onFinishClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelectable: Boolean = false,
    internalPadding: Dp = 0.dp,
    wasRecentlyOpened: Boolean = false,
    onContinueReadingClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            )
            .background(
                color = when {
                    bookUi.isSelected -> MaterialTheme.colorScheme.surface
                    wasRecentlyOpened -> MaterialTheme.colorScheme.background
                    else -> Color.Transparent
                },
                shape = RoundedCornerShape(8.dp)
            )

            .padding(internalPadding)
    ) {
        if (isSelectable || bookUi.isSelected)
            Checkbox(
                checked = bookUi.isSelected,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.icons,
                    checkmarkColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        AsyncImage(
            model = bookUi.bookCoverPath,
            contentDescription = null,
            fallback = painterResource(R.drawable.book_cover_placeholder),
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .thenIf(wasRecentlyOpened) {
                    width(160.dp)
                    .height(240.dp)
                }
                .thenIf(!wasRecentlyOpened) {
                    width(104.dp)
                    .height(156.dp)
                }
        )
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(0.9f)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = bookUi.bookTitle,
                        style = if (wasRecentlyOpened)
                            MaterialTheme.typography.titleLarge
                        else
                            MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                    Text(
                        text = bookUi.authorName,
                        style = if (wasRecentlyOpened)
                            MaterialTheme.typography.bodyMedium
                        else
                            MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                }
                if (wasRecentlyOpened)
                    PrimaryButton(
                        text = stringResource(id = R.string.continue_label),
                        onClick = onContinueReadingClick,
                        iconRes = R.drawable.continue_reading,
                        isCollapsed = false
                    )
            }
            Column {
                LinearProgressIndicator(
                    progress = {
                        if (bookUi.isFinished)
                            1f
                        else
                            bookUi.readingProgress
                    },
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.loaderSecondary,
                    strokeCap = StrokeCap.Round,
                    gapSize = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp)
                        .thenIf(wasRecentlyOpened) {
                            padding(bottom = 8.dp)
                        }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            onClick = onFavoriteClick,
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (bookUi.isFavorite) R.drawable.menu_favorites_active else R.drawable.favorites
                                ),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.icons,
                                modifier = Modifier
                                    .thenIf(wasRecentlyOpened) {
                                        size(28.dp)
                                    }
                            )
                        }
                        IconButton(
                            onClick = onFinishClick,
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (bookUi.isFinished) R.drawable.finished else R.drawable.finish
                                ),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.icons,
                                modifier = Modifier
                                    .thenIf(wasRecentlyOpened) {
                                        size(28.dp)
                                    }
                            )
                        }
                        IconButton(
                            onClick = onShareClick,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.share),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.icons,
                                modifier = Modifier
                                    .thenIf(wasRecentlyOpened) {
                                        size(28.dp)
                                    }
                            )
                        }
                    }
                    IconButton(
                        onClick = onDeleteClick,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.rounded_delete_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.icons,
                            modifier = Modifier
                                .thenIf(wasRecentlyOpened) {
                                    size(28.dp)
                                }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = TABLET)
@Composable
private fun LibraryBookCardPreview() {
    PageKeeperTheme {
        val bookUi = PreviewModel.books.first()
        LibraryBookCard(
            bookUi = bookUi,
            onClick = {},
            onLongClick = {},
            isSelectable = false,
            onFavoriteClick = {},
            onFinishClick = {},
            onShareClick = {},
            onDeleteClick = {},
            wasRecentlyOpened = true
        )
    }
}