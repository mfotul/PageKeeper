package com.example.pagekeeper.pages.presentation.library.components


import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme

@Composable
fun LibraryDialog(
    title: String,
    confirmButton: String,
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    cancelButton: String? = null,
    onCancelClick: (() -> Unit)? = null,
    isTextButtonRed: Boolean = false
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
    ) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .width(312.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = (-1).sp
                )
                description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    cancelButton?.let { button ->
                        onCancelClick?.let { action ->
                            TextButton(
                                onClick = action
                            ) {
                                Text(
                                    text = button,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        }
                    }
                    TextButton(
                        onClick = onConfirmClick
                    ) {
                        Text(
                            text = confirmButton,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isTextButtonRed)
                                MaterialTheme.colorScheme.onError
                                        else
                                MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LibraryDialogPreview() {
    PageKeeperTheme {
        LibraryDialog(
            title = stringResource(R.string.delete_one, "The Adventures of Tom Sawyer"),
            description = stringResource(R.string.this_action_will_remove_the_book_from_your_library),
            confirmButton = stringResource(R.string.delete),
            onConfirmClick = {},
            cancelButton = stringResource(R.string.cancel),
            onCancelClick = {},
            onDismiss = {},
            isTextButtonRed = true
        )
    }
}