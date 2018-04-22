package com.movie.app.details

import android.os.Bundle
import com.movie.app.BaseContractor
import com.movie.app.modules.Movie

class DetailsActivityContractor {

    interface View {
        fun showProgressBar()

        fun hideProgressBar()

        fun showData(movie: Movie)

        fun showError(throwable: Throwable)
    }

    interface Presenter : BaseContractor.BasePresenter<View> {
        fun setExtraBundles(extras: Bundle)
    }

}