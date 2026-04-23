package com.example.pagekeeper.pages.data.reader

import com.example.pagekeeper.core.database.pages.reader.SectionEntity
import com.example.pagekeeper.pages.domain.reader.BodyType
import com.example.pagekeeper.pages.domain.reader.Section
import kotlinx.serialization.json.Json

fun Section.toSectionEntity(): SectionEntity {
    val json = Json { ignoreUnknownKeys = true }
    return SectionEntity(
        sectionId = sectionId ?: 0,
        bookId = bookId,
        body = body,
        content = when (body) {
            BodyType.TITLE -> json.encodeToString(title.toFb2TitleDto())
            BodyType.SECTION -> json.encodeToString(section?.toFb2SectionDto())
        }
    )
}

fun SectionEntity.toSection(): Section {
    val json = Json { ignoreUnknownKeys = true }
    return Section(
        sectionId = sectionId,
        bookId = bookId,
        body = body,
        section = if (body == BodyType.SECTION) (json.decodeFromString(content) as Fb2SectionDto).toFb2Section()
        else null,
        title = if (body == BodyType.TITLE) (json.decodeFromString(content) as FB2TitleDto).toFb2Title()
        else null
    )
}