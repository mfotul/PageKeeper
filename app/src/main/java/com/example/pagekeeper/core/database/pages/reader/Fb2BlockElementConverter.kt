package com.example.pagekeeper.core.database.pages.reader

import androidx.room.TypeConverter
import com.example.pagekeeper.pages.data.reader.Fb2BlockElementDto
import kotlinx.serialization.json.Json

class Fb2BlockElementConverter {
    val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromFb2BlockElement(element: Fb2BlockElementDto): String {
        return json.encodeToString(element)
    }

    @TypeConverter
    fun toFb2BlockElement(content: String): Fb2BlockElementDto {
        return json.decodeFromString(content)
    }
}