package com.example.pagekeeper.pages.presentation.bookmarks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.icons
import com.example.pagekeeper.pages.presentation.bookmarks.models.BookmarkUi
import com.example.pagekeeper.pages.presentation.preview.PreviewModel
import com.example.pagekeeper.pages.presentation.util.thenIf

@Composable
fun BookmarkListItem(
    bookmark: BookmarkUi,
    isActionDropDownMenuOpen: Boolean,
    onBookmarkClick: () -> Unit,
    onMenuClick: () -> Unit,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onBookmarkClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.bookmark_color),
            contentDescription = null,
            tint = bookmark.colorItem.color
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = bookmark.text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = bookmark.chapter,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = bookmark.formattedTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        Box {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier
                    .thenIf(isActionDropDownMenuOpen) {
                        background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_more_vert_24),
                    contentDescription = stringResource(R.string.more_options),
                    tint = if (isActionDropDownMenuOpen)
                        MaterialTheme.colorScheme.background
                    else
                        MaterialTheme.colorScheme.icons
                )
            }

            BookmarkActionDropDownMenu(
                expanded = isActionDropDownMenuOpen,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onDismiss = onDismiss
            )
        }
    }


}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun BookmarkListItemPreview() {
    PageKeeperTheme {
        BookmarkListItem(
            bookmark = PreviewModel.bookmark[0],
            isActionDropDownMenuOpen = true,
            onBookmarkClick = {},
            onMenuClick = {},
            onDismiss = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}