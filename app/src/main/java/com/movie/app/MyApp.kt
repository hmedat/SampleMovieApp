package com.movie.app

import android.app.Application
import com.movie.app.di.activityModule
import com.movie.app.di.appModule
import com.movie.app.di.roomModule
import com.movie.app.util.TimberUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by mohammedhmedat on 12/15/17.
 */

class MyApp : Application() {

    companion object {
        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        TimberUtil.init()
        startKoin {
            androidContext((this@MyApp))
            androidLogger()
            modules(listOf(appModule, roomModule, activityModule))
        }
    }
}
