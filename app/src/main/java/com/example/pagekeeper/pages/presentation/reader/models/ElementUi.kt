package com.example.pagekeeper.pages.presentation.reader.models

import com.example.pagekeeper.pages.presentation.models.Fb2BlockElementUi

data class ElementUi(
    val elementId: Long,
    val bookId: Long,
    val bodyId: Int,
    val sectionId: Int,
    val content: Fb2BlockElementUi
)
