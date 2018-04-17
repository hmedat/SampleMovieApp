package com.movie.app

import android.app.Application


/**
 * Created by mohammedhmedat on 12/15/17.
 */

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}
