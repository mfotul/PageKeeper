package com.example.pagekeeper.core.database.di

import androidx.room.Room
import com.example.pagekeeper.core.database.PageDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single<PageDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            PageDatabase::class.java,
            "pages.db"
        ).build()
    }

    single {
        get<PageDatabase>().pageDao
    }
}