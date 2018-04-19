package com.movie.app.main

import com.movie.app.api.result.LatestMoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class MainPresenter(scheduler: Scheduler, mainView: MainActivityContractor.View) : MainActivityContractor.Presenter {
    private var moviesInteractor: MoviesInteractor
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val view: MainActivityContractor.View = mainView
    private val searchFilter: MovieSearchFilter = MovieSearchFilter()

    init {
        moviesInteractor = MoviesInteractor(scheduler, compositeDisposable)
    }

    override fun subscribe() {
        loadFirstPage()
    }

    override fun loadFirstPage() {
        searchFilter.pageNumber = MovieSearchFilter.First_PAGE
        loadData()
    }

    override fun loadNextPage() {
        loadData()
    }

    private fun loadData() {
        val isFirstPage = searchFilter.isFirstPage()
        if (isFirstPage) {
            view.showProgressBar()
        }
        moviesInteractor.getLatest(searchFilter, object : MoviesInteractor.CallBack {

            override fun onSuccess(result: LatestMoviesResult) {
                if (result.results!!.isEmpty() && !result.isLoadMore()) {
                    view.showNoData()
                }
                view.showData(result)
                searchFilter.pageNumber++
                view.hideProgressBar()
            }

            override fun onError(throwable: Throwable) {
                view.hideProgressBar()
                view.showError(isFirstPage, throwable)
            }

        })
    }

    override fun onMovieClicked(movie: Movie) {

    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }

}