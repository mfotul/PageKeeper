package com.example.pagekeeper.pages.di

import com.example.pagekeeper.pages.data.library.Fb2XmlParser
import com.example.pagekeeper.pages.data.library.InternalLibraryStorage
import com.example.pagekeeper.pages.data.library.RoomPageDataSource
import com.example.pagekeeper.pages.data.reader.DataStorePagePreferences
import com.example.pagekeeper.pages.domain.library.LibraryStorage
import com.example.pagekeeper.pages.domain.library.PageDataSource
import com.example.pagekeeper.pages.domain.library.XmlParser
import com.example.pagekeeper.pages.domain.library.PagePreferences
import com.example.pagekeeper.pages.presentation.library.LibraryViewModel
import com.example.pagekeeper.pages.presentation.navigation.NavigationViewModel
import com.example.pagekeeper.pages.presentation.reader.ReaderViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val pagesModule = module {
    singleOf(::InternalLibraryStorage) bind LibraryStorage::class
    singleOf(::Fb2XmlParser) bind XmlParser::class
    singleOf(::RoomPageDataSource) bind PageDataSource::class
    singleOf(::DataStorePagePreferences) bind PagePreferences::class

    viewModelOf(::LibraryViewModel)
    viewModelOf(::ReaderViewModel)
    viewModelOf(::NavigationViewModel)
}