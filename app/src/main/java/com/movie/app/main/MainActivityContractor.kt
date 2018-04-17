package com.movie.app.main

import com.movie.app.BaseContractor
import com.movie.app.api.result.LatestMoviesResult

class MainActivityContractor {

    interface View {
        fun showProgressBar()

        fun hideProgressBar()

        fun showNoData()

        fun showData(result: LatestMoviesResult)

        fun showErrorScreen(throwable: Throwable)
    }

    interface Presenter : BaseContractor.BasePresenter<View> {
        fun onMovieClicked()
    }

}