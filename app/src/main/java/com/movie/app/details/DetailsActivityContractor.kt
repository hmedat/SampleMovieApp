package com.movie.app.details

import android.content.Intent
import android.os.Bundle
import com.movie.app.BaseContractor
import com.movie.app.modules.Movie

class DetailsActivityContractor {

    interface View {
        fun showProgressBar()

        fun hideProgressBar()

        fun showData(movie: Movie)

        fun showError(throwable: Throwable)

        fun startYoutubeActivity(chooser: Intent)
    }

    interface Presenter : BaseContractor.BasePresenter<View> {
        fun setExtraBundles(extras: Bundle)

        fun showTrailerVideo()
    }

}