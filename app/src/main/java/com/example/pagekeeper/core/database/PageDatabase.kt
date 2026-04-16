package com.example.pagekeeper.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pagekeeper.core.database.pages.BookEntity
import com.example.pagekeeper.core.database.pages.PageDao

@Database(
    entities = [BookEntity::class],
    version = 1
)

abstract class PageDatabase: RoomDatabase() {
    abstract val pageDao: PageDao
}