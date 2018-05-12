package com.movie.app.details

import com.movie.app.BaseContractor
import com.movie.app.modules.Movie

class DetailsActivityContractor {

    interface View {
        fun showProgressBar()

        fun hideProgressBar()

        fun showData(movie: Movie)

        fun showError(throwable: Throwable)

        fun showSimilarMovies(list: List<Movie>)
    }

    interface Presenter : BaseContractor.BasePresenter<View> {
        fun setMovieId(movieId: Long)

        fun getSimilarMovies()
    }

}