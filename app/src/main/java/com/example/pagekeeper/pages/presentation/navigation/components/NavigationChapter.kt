package com.example.pagekeeper.pages.presentation.navigation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.chapterTitle
import com.example.pagekeeper.core.presentation.designsystem.theme.contentsTitle
import com.example.pagekeeper.pages.presentation.navigation.models.ContentUi
import com.example.pagekeeper.pages.presentation.preview.PreviewModel

@Composable
fun NavigationChapter(
    contentUi: ContentUi,
    isLast: Boolean,
    isExpanded: Boolean,
    onChapterClick: (Long) -> Unit,
    onTitleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onTitleClick
                )
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(
                    if (isExpanded)
                        R.drawable.baseline_arrow_drop_up_24
                    else
                        R.drawable.baseline_arrow_drop_down_24
                ),
                contentDescription = stringResource(
                    if (isExpanded)
                        R.string.collapse_content
                    else
                        R.string.expand_content
                )
            )
            Column {
                contentUi.title.forEach { title ->
                    Text(
                        text = title,
                        style = MaterialTheme.typography.contentsTitle
                    )
                }
            }
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline
        )
        AnimatedVisibility(
            visible = isExpanded
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                contentUi.chapters.forEachIndexed { index, chapter ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    onChapterClick(chapter.elementId)
                                }
                            )
                            .padding(16.dp)
                    ) {
                        chapter.title.forEach { title ->
                            Text(
                                text = title,
                                style = MaterialTheme.typography.chapterTitle,
                                fontWeight = if (chapter.isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                    if (index != contentUi.chapters.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                if (!isLast)
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline
                    )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xfff)
@Composable
private fun NavigationChapterPreview() {
    PageKeeperTheme {
        NavigationChapter(
            contentUi = PreviewModel.contents[0],
            isLast = false,
            onChapterClick = {},
            onTitleClick = {},
            isExpanded = false
        )
    }
}