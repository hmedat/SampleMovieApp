package com.movie.app.main

import com.movie.app.BaseContractor
import com.movie.app.api.result.LatestMoviesResult
import com.movie.app.modules.Movie

class MainActivityContractor {

    interface View {
        fun showProgressBar()

        fun hideProgressBar()

        fun showNoData()

        fun showData(result: LatestMoviesResult)

        fun showError(isFirstPage: Boolean, throwable: Throwable)
    }

    interface Presenter : BaseContractor.BasePresenter<View> {
        fun onMovieClicked(movie: Movie)

        fun loadFirstPage()

        fun loadNextPage()
    }

}