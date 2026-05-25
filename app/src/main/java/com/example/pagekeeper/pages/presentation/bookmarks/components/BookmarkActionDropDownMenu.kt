package com.example.pagekeeper.pages.presentation.bookmarks.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme

@Composable
fun BookmarkActionDropDownMenu(
    expanded: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(16.dp),
        offset = DpOffset(0.dp, 8.dp),
        modifier = modifier
    ) {
        DropdownMenuItem(
            enabled = true,
            onClick = onEditClick,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.edit),
                    contentDescription = stringResource(R.string.edit),
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.edit),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        )
        DropdownMenuItem(
            enabled = true,
            onClick = onDeleteClick,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.rounded_delete_24),
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.onError
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.delete),
                    color = MaterialTheme.colorScheme.onError
                )
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun BookmarkActionDropDownMenuPreview() {
    PageKeeperTheme {
        var expanded by remember { mutableStateOf(false) }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_more_vert_24),
                    contentDescription = null,
                )
            }
            BookmarkActionDropDownMenu(
                expanded = expanded,
                onEditClick = {},
                onDeleteClick = {},
                onDismiss = {}
            )
        }
    }
}