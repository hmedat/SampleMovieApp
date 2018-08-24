package com.movie.app.util

import com.movie.app.BuildConfig
import timber.log.Timber

object TimberUtil {
    fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
