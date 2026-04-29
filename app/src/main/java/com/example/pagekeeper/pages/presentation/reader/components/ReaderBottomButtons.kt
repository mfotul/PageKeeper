package com.example.pagekeeper.pages.presentation.reader.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.bgBottomNav
import com.example.pagekeeper.core.presentation.designsystem.theme.icons

@Composable
fun ReaderBottomButtons(
    isAutoRotate: Boolean,
    isVisible: Boolean,
    isTablet: Boolean,
    onRotateClick: () -> Unit,
    onFonSizeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.bgBottomNav,
        contentColor = MaterialTheme.colorScheme.icons,
        modifier = modifier
            .then(
                if (isVisible)
                    Modifier
                else
                    Modifier
                        .height(0.dp)
            )
            .animateContentSize()
    ) {
        if (!isTablet)
            NavigationBarItem(
                selected = isAutoRotate,
                onClick = onRotateClick,
                icon = {
                    Icon(
                        painter = painterResource(
                            if (isAutoRotate) R.drawable.portrait
                            else R.drawable.landscape
                        ),
                        contentDescription = stringResource(
                            if (isAutoRotate) R.string.lock_screen_rotation
                            else R.string.unlock_screen_rotation
                        )
                    )
                },
                label = {
                    Text(
                        text = stringResource(
                            if (isAutoRotate) R.string.auto_rotate
                            else R.string.landscape
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.icons,
                    indicatorColor = MaterialTheme.colorScheme.onSurface
                )
            )
        NavigationBarItem(
            selected = false,
            onClick = onFonSizeClick,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.font_size),
                    contentDescription = stringResource(R.string.font_size_setting)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.font_size)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.icons,
                indicatorColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Preview
@Composable
private fun ReaderBottomButtonsPreview() {
    PageKeeperTheme {
        ReaderBottomButtons(
            isAutoRotate = false,
            isVisible = true,
            isTablet = false,
            onRotateClick = {},
            onFonSizeClick = {}
        )
    }
}