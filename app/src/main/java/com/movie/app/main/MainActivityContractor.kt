package com.movie.app.main

import com.movie.app.BaseContractor
import com.movie.app.api.result.MoviesResult

class MainActivityContractor {

    interface View {
        fun showProgressBar()

        fun hideProgressBar()

        fun showNoData()

        fun showData(result: MoviesResult)

        fun showError(isFirstPage: Boolean, throwable: Throwable)
    }

    interface Presenter : BaseContractor.BasePresenter<View> {
        fun loadFirstPage()

        fun loadNextPage()
    }

}