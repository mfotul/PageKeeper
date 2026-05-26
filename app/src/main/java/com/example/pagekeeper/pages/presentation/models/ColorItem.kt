package com.example.pagekeeper.pages.presentation.models

import androidx.compose.ui.graphics.Color
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.BookmarkBlue
import com.example.pagekeeper.core.presentation.designsystem.theme.BookmarkGreen
import com.example.pagekeeper.core.presentation.designsystem.theme.BookmarkPurple
import com.example.pagekeeper.core.presentation.designsystem.theme.BookmarkRed
import com.example.pagekeeper.core.presentation.designsystem.theme.BookmarkYellow
import com.example.pagekeeper.core.presentation.util.UiText

enum class ColorItem(
    val color: Color,
    val title: UiText,
) {
    BLUE(
        color = BookmarkBlue,
        title = UiText.StringResource(R.string.blue)
    ),
    GREEN(
        color = BookmarkGreen,
        title = UiText.StringResource(R.string.green)
    ),
    YELLOW(
        color = BookmarkYellow,
        title = UiText.StringResource(R.string.yellow)
    ),
    RED(
        color = BookmarkRed,
        title = UiText.StringResource(R.string.red)
    ),
    PURPLE(
        color = BookmarkPurple,
        title = UiText.StringResource(R.string.purple)
    ),
}