package com.example.pagekeeper.pages.presentation.bookmarks.models

import com.example.pagekeeper.pages.presentation.models.ColorItem
import com.example.pagekeeper.pages.presentation.util.toReadableTime
import java.time.Instant

data class BookmarkUi(
    val id: Int,
    val bookId: Long,
    val colorItem: ColorItem,
    val text: String,
    val chapter: String,
    val creationTime: Instant,
) {
    val formattedTime = creationTime.toReadableTime()
}
