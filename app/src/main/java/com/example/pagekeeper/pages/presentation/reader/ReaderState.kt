package com.example.pagekeeper.pages.presentation.reader

import androidx.compose.runtime.Stable
import com.example.pagekeeper.pages.presentation.reader.models.SectionUi

@Stable
data class ReaderState(
    val areBarsVisible: Boolean = false,
    val bookName: String = "",
    val isAutRotate: Boolean = true,
    val fontSize: Float? = null,
    val fonSizeChangeCounter: Int = 0,
    val isFontSliderVisible: Boolean = false,
    val sections: List<SectionUi> = emptyList()
)
