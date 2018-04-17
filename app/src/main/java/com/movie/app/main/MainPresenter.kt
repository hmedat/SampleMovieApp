package com.movie.app.main

import com.movie.app.api.result.LatestMoviesResult
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
        moviesInteractor.getLatest(searchFilter, object : MoviesInteractor.CallBack {

            override fun onSuccess(result: LatestMoviesResult) {
                view.showData(result)
            }

            override fun onError(throwable: Throwable) {
                view.showErrorScreen(throwable)
            }

        })
    }

    override fun onMovieClicked() {

    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }

}