package com.movie.app.main

import android.os.Bundle
import com.movie.app.BaseActivity
import com.movie.app.R

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
