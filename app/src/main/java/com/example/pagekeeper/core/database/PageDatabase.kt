package com.example.pagekeeper.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pagekeeper.core.database.pages.reader.BodyConverter
import com.example.pagekeeper.core.database.pages.library.BookEntity
import com.example.pagekeeper.core.database.pages.library.PageDao
import com.example.pagekeeper.core.database.pages.reader.SectionEntity

@Database(
    entities = [BookEntity::class, SectionEntity::class],
    version = 1
)
@TypeConverters(
    BodyConverter::class
)
abstract class PageDatabase : RoomDatabase() {
    abstract val pageDao: PageDao
}