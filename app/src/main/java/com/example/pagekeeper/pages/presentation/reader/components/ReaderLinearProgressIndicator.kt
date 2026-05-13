package com.example.pagekeeper.pages.presentation.reader.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.core.presentation.designsystem.theme.loaderSecondary

@Composable
fun ReaderLinearProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier
) {
    val stopIndicatorColor = MaterialTheme.colorScheme.onPrimary
    LinearProgressIndicator(
        progress = progress,
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.loaderSecondary,
        strokeCap = StrokeCap.Round,
        gapSize = 8.dp,
        drawStopIndicator = {
            drawCircle(
                color = stopIndicatorColor,
                center = Offset(
                    x = size.width * progress() + 16,
                    y = center.y
                )
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun ReaderLinearProgressIndicatorPreview() {
    PageKeeperTheme {
        ReaderLinearProgressIndicator(
            progress = { 0.5f  }
        )
    }
}