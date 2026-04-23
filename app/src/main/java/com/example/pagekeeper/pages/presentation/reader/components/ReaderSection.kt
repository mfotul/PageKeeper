package com.example.pagekeeper.pages.presentation.reader.components

import android.widget.Space
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.magnifier
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pagekeeper.core.presentation.designsystem.theme.paragraphItalic
import com.example.pagekeeper.core.presentation.designsystem.theme.paragraphRegular
import com.example.pagekeeper.pages.presentation.reader.models.Fb2BlockElementUi
import com.example.pagekeeper.pages.presentation.reader.models.Fb2SectionUi

@Composable
fun ReaderSection(
    fB2SectionUi: Fb2SectionUi?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        val borderColor = MaterialTheme.colorScheme.outline

        fB2SectionUi?.content?.forEachIndexed { index, element ->
            when (element) {
                is Fb2BlockElementUi.Cite -> {
                    Column(
                        modifier = Modifier
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
                        element.lines.forEach {
                            Text(
                                text = it.text,
                                style = MaterialTheme.typography.paragraphItalic
                            )
                        }
                        element.author?.let {
                            Text(
                                text = it.text,
                                style = MaterialTheme.typography.paragraphRegular,
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
                        text = element.text,
                        lineHeight = 24.sp
                    )
                }

                is Fb2BlockElementUi.Subtitle -> {
                    Text(
                        text = element.text
                    )
                }

                is Fb2BlockElementUi.Title -> {
                    Text(
                        text = element.lines
                    )
                }
            }
        }
    }
}