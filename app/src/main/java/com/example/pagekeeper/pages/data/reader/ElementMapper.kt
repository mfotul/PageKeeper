package com.example.pagekeeper.pages.data.reader

import com.example.pagekeeper.core.database.pages.reader.ElementEntity
import com.example.pagekeeper.pages.domain.reader.Element

fun Element.toElementEntity(): ElementEntity {
    return ElementEntity(
        elementId = elementId ?: 0,
        bookId = bookId,
        bodyId = bodyId,
        sectionId = sectionId,
        content = content.toFB2BlockElementDto()
    )
}

fun ElementEntity.toElement(): Element {
    return Element(
        elementId = elementId,
        bookId = bookId,
        bodyId = bodyId,
        sectionId = sectionId,
        content = content.toFB2BlockElement()
    )
}