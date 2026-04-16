package com.example.pagekeeper.pages.presentation.library.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.pagekeeper.R
import com.example.pagekeeper.core.presentation.designsystem.theme.PageKeeperTheme
import com.example.pagekeeper.pages.presentation.models.BookUi
import com.example.pagekeeper.pages.presentation.preview.PreviewModel

@Composable
fun LibrarySmallBookCard(
    bookUi: BookUi,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        AsyncImage(
            model = bookUi.bookCoverPath,
            contentDescription = null,
            fallback = painterResource(R.drawable.book_cover_placeholder),
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .width(40.dp)
                .height(60.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = bookUi.bookTitle,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = bookUi.authorName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LibrarySmallBookCardPreview() {
    PageKeeperTheme {
        LibrarySmallBookCard(
            bookUi = PreviewModel.books[0]
        )
    }
}