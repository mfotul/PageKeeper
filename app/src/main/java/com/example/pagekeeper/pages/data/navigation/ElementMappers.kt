package com.example.pagekeeper.pages.data.navigation

import com.example.pagekeeper.pages.domain.navigation.Chapter
import com.example.pagekeeper.pages.domain.reader.Element

fun Element.toChapter(): Chapter {
    return Chapter(
        elementId = elementId!!,
        title = content
    )
}
