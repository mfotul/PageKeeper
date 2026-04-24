package com.example.pagekeeper.pages.presentation.reader.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.chapterTitle
import com.example.pagekeeper.pages.presentation.reader.models.Fb2TitleUi

@Composable
fun ReaderTitle(
    fB2TitleUi: Fb2TitleUi?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        fB2TitleUi?.lines?.forEach {
            Text(
                text = it.text,
                style = MaterialTheme.typography.chapterTitle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun ReaderTitlePreview() {
    PageKeeperTheme {
        ReaderTitle(
            fB2TitleUi = Fb2TitleUi(
                lines = listOf(
                    AnnotatedString(text = "Chapter 1")
                )
            )
        )
    }
}