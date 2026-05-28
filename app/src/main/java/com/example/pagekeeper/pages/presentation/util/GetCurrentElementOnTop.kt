package com.example.pagekeeper.pages.presentation.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.paging.compose.LazyPagingItems
import com.example.pagekeeper.pages.presentation.reader.models.ElementUi

fun getCurrentElementOnTop(
    listState: LazyListState,
    lazyPagingItems: LazyPagingItems<ElementUi>
): ElementUi? {
    val index = listState.firstVisibleItemIndex
    return if (index < lazyPagingItems.itemCount)
        lazyPagingItems[index]
    else
        null
}