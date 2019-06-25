package com.movie.app.splash

import android.content.Intent
import android.os.Bundle
import com.movie.app.BaseActivity
import com.movie.app.main.MainActivity
import org.koin.android.ext.android.inject
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashActivityContractor.View {

    val presenter: SplashActivityContractor.Presenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.bindView(this)
        presenter.subscribe()
    }

    override fun startNextActivity(bundle: Bundle?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
