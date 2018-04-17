package com.movie.app.main

import android.os.Bundle
import com.movie.app.BaseActivity
import com.movie.app.R
import com.movie.app.api.result.LatestMoviesResult
import io.reactivex.schedulers.Schedulers

class MainActivity : BaseActivity(), MainActivityContractor.View {

    private lateinit var presenter: MainActivityContractor.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.presenter = MainPresenter(Schedulers.io(), this)
        presenter.subscribe()
    }

    override fun showProgressBar() {
    }

    override fun hideProgressBar() {
    }

    override fun showNoData() {
    }

    override fun showData(result: LatestMoviesResult) {
    }

    override fun showErrorScreen(throwable: Throwable) {
        // CHeck if the adapter has been loaded show snackbar error
        //Else show error screen
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unSubscribe()
    }
}
