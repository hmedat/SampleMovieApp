package com.movie.app.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.movie.app.BaseActivity
import com.movie.app.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getSubscribeLiveData().observe(this, Observer {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
        viewModel.subscribe()
    }

}
