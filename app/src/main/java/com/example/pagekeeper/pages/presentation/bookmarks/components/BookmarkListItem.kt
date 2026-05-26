package com.example.pagekeeper.pages.presentation.bookmarks.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.button.ThreeDotButton
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.pages.presentation.bookmarks.models.BookmarkUi
import com.example.pagekeeper.pages.presentation.components.EditViewDeleteDropDownMenu
import com.example.pagekeeper.pages.presentation.preview.PreviewModel

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
            ThreeDotButton(
                onClick = onMenuClick,
                isSelected = isActionDropDownMenuOpen
            )

            EditViewDeleteDropDownMenu(
                expanded = isActionDropDownMenuOpen,
                editViewIconRes = R.drawable.edit,
                editViewTextRes = R.string.edit,
                onEditViewClick = onEditClick,
                deleteTextRes = R.string.delete,
                onDeleteClick = onDeleteClick,
                onDismiss = onDismiss,
                offset = DpOffset(0.dp, 8.dp)
            )
        }
    }


}

@Preview(showBackground = true, backgroundColor = 0xFFF, device = TABLET)
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