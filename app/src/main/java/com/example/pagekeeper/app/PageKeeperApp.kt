package com.example.pagekeeper.app

import android.app.Application
import com.example.pagekeeper.BuildConfig
import com.example.pagekeeper.app.di.appModule
import com.example.pagekeeper.core.database.di.databaseModule
import com.example.pagekeeper.pages.di.pagesModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class PageKeeperApp : Application() {

    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@PageKeeperApp)
            modules(
                appModule,
                databaseModule,
                pagesModule
            )
        }
    }
}