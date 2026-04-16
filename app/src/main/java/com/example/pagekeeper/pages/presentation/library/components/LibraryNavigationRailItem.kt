package com.example.pagekeeper.pages.presentation.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme

@Composable
fun LibraryNavigationRailItem(
    railExpanded: Boolean,
    label: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor = if (selected)
        MaterialTheme.colorScheme.onSurface
    else
        Color.Transparent

    if (railExpanded)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .background(
                    color = bgColor,
                    shape = RoundedCornerShape(100.dp)
                )
                .clickable(onClick = onClick)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            icon()
            label()
        }
    else
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .clickable(onClick = onClick)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .widthIn(min = 70.dp)
                    .background(
                        color = bgColor,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .padding(8.dp)
            ) {
                icon()
            }
            label()
        }
}

@Preview(showBackground = true, backgroundColor = 0xAAA)
@Composable
private fun LibraryNavigationRailItemPreview() {
    PageKeeperTheme {
        var expanded by remember { mutableStateOf(false) }

        LibraryNavigationRailItem(
            railExpanded = true,
            label = {
                Text("Library")
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.menu_library_active),
                    contentDescription = null
                )
            },
            selected = true,
            onClick = {
                expanded = !expanded
            }
        )
    }
}
