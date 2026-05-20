package com.example.pagekeeper.pages.presentation.reader.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.pages.presentation.preview.PreviewModel
import com.example.pagekeeper.pages.presentation.models.Fb2BlockElementUi

@Composable
fun ReaderBookItem(
    content: Fb2BlockElementUi,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(bottom = 16.dp)
    ) {
        val borderColor = MaterialTheme.colorScheme.outline

        when (content) {
            is Fb2BlockElementUi.Cite -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(
                                color = borderColor,
                                start = Offset(0f, 0f),
                                end = Offset(0f, size.height)
                            )
                        }
                        .padding(vertical = 16.dp)
                        .padding(start = 16.dp)
                ) {
                    content.lines.forEach {
                        Text(
                            text = it
                        )
                    }
                    content.author?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier
                                .padding(top = 8.dp)
                        )
                    }
                }

            }

            Fb2BlockElementUi.EmptyLine -> {
                Spacer(modifier = Modifier.height(8.dp))
            }


            is Fb2BlockElementUi.Paragraph -> {
                Text(
                    text = content.text
                )
            }

            is Fb2BlockElementUi.Subtitle -> {
                Text(
                    text = content.text
                )
            }

            is Fb2BlockElementUi.Title -> {
                content.lines.forEach {
                    Text(
                        text = it,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun ReaderBookItemPreview() {
    PageKeeperTheme {
        ReaderBookItem(
            PreviewModel.element[2].content
        )
    }
}