package com.movie.app

import com.movie.app.splash.SplashActivityContractor

/**
 * Created by mohammedhmedat on 12/16/17.
 */

class BaseContractor {
    interface BasePresenter<T> {
        fun subscribe()

        fun unSubscribe()
    }

    interface BaseView<T>
}
