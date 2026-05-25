package com.example.pagekeeper.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pagekeeper.core.database.pages.bookmarks.BookmarkEntity
import com.example.pagekeeper.core.database.pages.library.BookEntity
import com.example.pagekeeper.core.database.pages.library.PageDao
import com.example.pagekeeper.core.database.pages.navigation.ChapterEntity
import com.example.pagekeeper.core.database.pages.navigation.ContentEntity
import com.example.pagekeeper.core.database.pages.reader.ElementEntity
import com.example.pagekeeper.core.database.pages.reader.Fb2BlockElementConverter

@Database(
    entities = [
        BookEntity::class,
        ElementEntity::class,
        ChapterEntity::class,
        ContentEntity::class,
        BookmarkEntity::class
    ],
    version = 1
)
@TypeConverters(
    Fb2BlockElementConverter::class
)
abstract class PageDatabase : RoomDatabase() {
    abstract val pageDao: PageDao
}