package com.movie.app.fav

import com.movie.app.BaseContractor
import com.movie.app.modules.Movie
import com.movie.app.splash.SplashActivityContractor

class FavouritesActivityContractor {

    interface View {
        fun showProgressBar()

        fun hideProgressBar()

        fun showNoData()

        fun showData(movies: List<Movie>)

        fun showError(throwable: Throwable)
    }

    interface Presenter : BaseContractor.BasePresenter<View> {
        fun bindView(view: View)

        fun removeFromList(movieId: Long)
    }
}
