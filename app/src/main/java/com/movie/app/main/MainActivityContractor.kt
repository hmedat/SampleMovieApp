package com.movie.app.main

import com.movie.app.BaseContractor
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSortType

class MainActivityContractor {

    interface View {
        fun showProgressBar()

        fun hideProgressBar()

        fun showNoData()

        fun showFirstData(data: List<Movie>)

        fun showLoadMoreData(data: List<Movie>)

        fun showError(isFirstPage: Boolean, throwable: Throwable)

        fun onDataCompleted(finished: Boolean)

        fun updateFavouritesStatues(list: HashSet<Long>)

        fun notifyVisibleItems()
    }

    interface Presenter : BaseContractor.BasePresenter<View> {
        fun loadFirstPage()

        fun loadNextPage()

        fun addRemoveFavMovie(movie: Movie)

        fun syncFavouritesStatues()

        fun onSearchFilterChanged(movieSortType: MovieSortType)
    }
}
