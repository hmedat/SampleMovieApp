package com.movie.app.splash

import android.os.Bundle
import com.movie.app.BaseContractor

class SplashActivityContractor {

    interface View {
        fun startNextActivity(bundle: Bundle?)
    }

    interface Presenter : BaseContractor.BasePresenter<View> {
    }

}