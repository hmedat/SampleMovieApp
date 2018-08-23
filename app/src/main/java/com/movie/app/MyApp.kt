package com.movie.app

import com.movie.app.di.DaggerAppComponent
import com.movie.app.util.TimberUtil
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Created by mohammedhmedat on 12/15/17.
 */

class MyApp : DaggerApplication() {
    companion object {
        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        TimberUtil.init()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }
}
