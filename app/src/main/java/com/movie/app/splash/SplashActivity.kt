package com.movie.app.splash

import android.content.Intent
import android.os.Bundle
import com.movie.app.BaseActivity
import com.movie.app.main.MainActivity
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashActivityContractor.View {

    @Inject
    lateinit var presenter: SplashActivityContractor.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.subscribe()
    }

    override fun startNextActivity(bundle: Bundle?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
