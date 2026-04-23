package com.example.pagekeeper.core.database.pages.reader

import androidx.room.TypeConverter
import com.example.pagekeeper.pages.domain.reader.BodyType

class BodyConverter {

    @TypeConverter
    fun fromBodyType(body: BodyType): String {
        return body.name
    }

    @TypeConverter
    fun toBodyType(bodyName: String): BodyType {
        return BodyType.valueOf(bodyName)
    }
}