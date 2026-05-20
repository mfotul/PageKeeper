package com.example.pagekeeper.pages.presentation.bookmarks.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.bgModalInput
import com.example.pagekeeper.pages.presentation.bookmarks.models.ColorItem
import com.example.pagekeeper.pages.presentation.util.thenIf

@Composable
fun BookmarkDropDownMenu(
    expanded: Boolean,
    items: List<ColorItem>,
    selectedItem: ColorItem,
    onDismiss: () -> Unit,
    onSelectedItem: (ColorItem) -> Unit,
    modifier: Modifier = Modifier,
    dropDownOffset: IntOffset = IntOffset.Zero,
) {
    if (expanded)
        Popup(
            onDismissRequest = onDismiss,
            offset = dropDownOffset,
            properties = PopupProperties(
                dismissOnClickOutside = false
            )
        ) {
            AnimatedVisibility(
                visible = expanded,
                enter = expandIn(expandFrom = Alignment.TopCenter) + fadeIn(),
                exit = shrinkOut(shrinkTowards = Alignment.TopCenter) + fadeOut()
            ) {
                BookmarkDropDownMenuContent(
                    items = items,
                    selectedItem = selectedItem,
                    onSelectedItem = onSelectedItem,
                    modifier = modifier
                )
            }
        }
}

@Composable
fun BookmarkDropDownMenuContent(
    items: List<ColorItem>,
    selectedItem: ColorItem,
    onSelectedItem: (ColorItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.bgModalInput,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        shadowElevation = 4.dp,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .width(IntrinsicSize.Max)
                .padding(4.dp)
        ) {
            items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            onClick = { onSelectedItem(item) }
                        )
                        .thenIf(item == selectedItem) {
                            background(MaterialTheme.colorScheme.onSurface)
                        }
                        .padding(vertical = 16.dp)
                        .padding(start = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                color = item.color,
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = item.title.asString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier

                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun BookmarkDropDownMenuPreview() {
    PageKeeperTheme {
        val items = ColorItem.entries.map { it }
        BookmarkDropDownMenuContent(
            items = items,
            selectedItem = items.first(),
            onSelectedItem = {},
        )
    }
}