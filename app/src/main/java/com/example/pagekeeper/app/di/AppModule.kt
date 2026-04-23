package com.example.pagekeeper.app.di

import com.example.pagekeeper.app.MainViewModel
import com.example.pagekeeper.app.PageKeeperApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as PageKeeperApp).applicationScope
    }

    viewModelOf(::MainViewModel)
}