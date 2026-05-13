package com.example.pagekeeper.pages.presentation.reader.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.bgBottomNav
import com.example.pagekeeper.core.presentation.designsystem.theme.icons
import com.example.pagekeeper.core.presentation.designsystem.theme.loaderSecondary

@Composable
fun ReaderBottomButtons(
    isAutoRotate: Boolean,
    isTablet: Boolean,
    progress: () -> Float,
    onChaptersClick: () -> Unit,
    onRotateClick: () -> Unit,
    onFonSizeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.bgBottomNav)
    ) {
        Text(
            text = stringResource(R.string.progress, (progress() * 100).toInt()),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        LinearProgressIndicator(
            progress = progress,
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.loaderSecondary,
            strokeCap = StrokeCap.Round,
            gapSize = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.bgBottomNav,
            contentColor = MaterialTheme.colorScheme.icons,
        ) {
            NavigationBarItem(
                selected = false,
                onClick = onChaptersClick,
                icon = {
                    Icon(
                        painter = painterResource(
                            R.drawable.chapter
                        ),
                        contentDescription = stringResource(R.string.chapter_navigation)
                    )
                },
                label = {
                    Text(
                        text = stringResource(
                            R.string.chapters
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.icons,
                    indicatorColor = MaterialTheme.colorScheme.onSurface
                )
            )
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
}

@Preview
@Composable
private fun ReaderBottomButtonsPreview() {
    PageKeeperTheme {
        ReaderBottomButtons(
            isAutoRotate = false,
            isTablet = false,
            progress = { 0.45f },
            onChaptersClick = {},
            onRotateClick = {},
            onFonSizeClick = {}
        )
    }
}