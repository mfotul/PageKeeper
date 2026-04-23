package com.example.pagekeeper.pages.presentation.reader

import androidx.compose.runtime.Stable
import com.example.pagekeeper.pages.presentation.reader.models.SectionUi

@Stable
data class ReaderState(
    val isLoading: Boolean = false,
    val sections: List<SectionUi> = emptyList()
)
