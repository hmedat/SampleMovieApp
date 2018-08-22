package com.movie.app.main

import com.movie.app.BaseContractor
import com.movie.app.modules.Movie

class MainActivityContractor {

    interface View {
        fun showProgressBar()

        fun hideProgressBar()

        fun showNoData()

        fun showFirstData(data: List<Movie>)

        fun showLoadMoreData(data: List<Movie>)

        fun showError(isFirstPage: Boolean, throwable: Throwable)

        fun onDataCompleted(finished: Boolean)
    }

    interface Presenter : BaseContractor.BasePresenter<View> {
        fun loadFirstPage()

        fun loadNextPage()

        fun addRemoveFavMovie(movie: Movie)
    }
}
