package com.example.pagekeeper.pages.presentation.bookmarks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.bgModalInput
import com.example.pagekeeper.core.presentation.designsystem.theme.buttonMedium
import com.example.pagekeeper.core.presentation.designsystem.theme.modalOutline
import com.example.pagekeeper.pages.presentation.bookmarks.models.ColorItem

@Composable
fun BookmarksDialog(
    titleState: TextFieldState,
    colorItems: List<ColorItem>,
    selectedColor: ColorItem,
    isDropDownMenuOpen: Boolean,
    onDropDownSelect: (ColorItem) -> Unit,
    onDialogDismiss: () -> Unit,
    onDropDownMenuDismiss: () -> Unit,
    onSaveClick: () -> Unit,
    onColorMenuClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDialogDismiss,
    ) {
        BookmarksDialogContent(
            titleState = titleState,
            colorItems = colorItems,
            selectedColor = selectedColor,
            isDropDownMenuOpen = isDropDownMenuOpen,
            onDropDownSelect = onDropDownSelect,
            onDialogDismiss = onDialogDismiss,
            onDropDownMenuDismiss = onDropDownMenuDismiss,
            onSaveClick = onSaveClick,
            onColorMenuClick = onColorMenuClick
        )
    }
}

@Composable
fun BookmarksDialogContent(
    titleState: TextFieldState,
    colorItems: List<ColorItem>,
    selectedColor: ColorItem,
    isDropDownMenuOpen: Boolean,
    onDropDownSelect: (ColorItem) -> Unit,
    onDialogDismiss: () -> Unit,
    onDropDownMenuDismiss: () -> Unit,
    onSaveClick: () -> Unit,
    onColorMenuClick: () -> Unit,
) {
    var colorRowSize by remember { mutableStateOf(IntSize.Zero) }
    var textInputSize by remember { mutableStateOf(IntSize.Zero) }
    val textMeasurer = rememberTextMeasurer()
    val bodyMedium = MaterialTheme.typography.bodyMedium
    var isTitleFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    val customInputTransformation = remember(textMeasurer) {
        InputTransformation {
            val proposedText = asCharSequence().toString()
            textLayoutResult = textMeasurer.measure(
                text = proposedText,
                style = bodyMedium,
                constraints = Constraints(
                    minWidth = textInputSize.width,
                    maxWidth = textInputSize.width,
                )
            )

            if ((textLayoutResult as TextLayoutResult).lineCount > 2)
                revertAllChanges()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .width(312.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(28.dp)
            )
            .padding(32.dp)
    ) {
        Text(
            text = stringResource(R.string.add_bookmark),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            letterSpacing = (-1).sp
        )
        BasicTextField(
            state = titleState,
            lineLimits = TextFieldLineLimits.MultiLine(
                minHeightInLines = 2,
                maxHeightInLines = 2
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            decorator = { innerTextField ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Title",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    innerTextField()
                }
            },
            inputTransformation = customInputTransformation,
            outputTransformation = OutputTransformation {
                textLayoutResult?.let { textLayoutResult ->
                    if (!isTitleFocused && textLayoutResult.lineCount > 2) {
                        val lineTwoEnd = textLayoutResult.getLineEnd(1)
                        val safeCutoff = (lineTwoEnd - 3).coerceAtLeast(0)
                        replace(safeCutoff, length, "...")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.bgModalInput,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.modalOutline,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
                .onFocusChanged {
                    isTitleFocused = it.isFocused
                }
                .onSizeChanged{
                    textInputSize = it
                }
        )
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.bgModalInput,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.modalOutline,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .onSizeChanged {
                        colorRowSize = it
                    }
                    .padding(top = 8.dp, bottom = 8.dp, start = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = selectedColor.color,
                            shape = CircleShape
                        )
                )
                Text(
                    text = selectedColor.title.asString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .weight(1f)
                )
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        onColorMenuClick()
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            if (isDropDownMenuOpen)
                                R.drawable.baseline_arrow_drop_down_24
                            else
                                R.drawable.baseline_arrow_right_24
                        ),
                        contentDescription = null
                    )
                }

            }

            BookmarkDropDownMenu(
                expanded = isDropDownMenuOpen,
                items = colorItems,
                selectedItem = selectedColor,
                onSelectedItem = onDropDownSelect,
                onDismiss = onDropDownMenuDismiss,
                dropDownOffset = IntOffset(
                    x = 0,
                    y = colorRowSize.height
                ),
                modifier = Modifier
                    .width(with(LocalDensity.current) {
                        colorRowSize.width.toDp()
                    })

            )
        }
        HorizontalDivider()
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = onDialogDismiss
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.buttonMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            TextButton(
                onClick = onSaveClick
            ) {
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.buttonMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarksDialogPreview() {
    PageKeeperTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            BookmarksDialogContent(
                titleState = rememberTextFieldState(),
                colorItems = ColorItem.entries.map { it },
                selectedColor = ColorItem.BLUE,
                isDropDownMenuOpen = false,
                onDropDownSelect = {},
                onDialogDismiss = {},
                onDropDownMenuDismiss = {},
                onSaveClick = {},
                onColorMenuClick = {}
            )
        }
    }
}