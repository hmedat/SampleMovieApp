package com.movie.app.fav

import com.movie.app.api.result.MoviesResult
import com.movie.app.repositories.MovieDataSource
import com.movie.app.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class FavouritesMoviesPresenter(
    private val schedulerProvider: BaseSchedulerProvider,
    private var movieDataSource: MovieDataSource
) : FavouritesActivityContractor.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: FavouritesActivityContractor.View

    override fun bindView(view: FavouritesActivityContractor.View) {
        this.view = view
    }

    override fun subscribe() {
        loadData()
    }

    private fun loadData() {
        view.showProgressBar()
        movieDataSource.getFavMovies()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(object : Observer<MoviesResult> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(result: MoviesResult) {
                    if (result.isEmptyData()) {
                        view.showNoData()
                    } else {
                        view.showData(result.results!!)
                    }
                }

                override fun onError(throwable: Throwable) {
                    view.hideProgressBar()
                    view.showError(throwable)
                }

                override fun onComplete() {
                    view.hideProgressBar()
                }
            })
    }

    override fun removeFromList(movieId: Long) {
        movieDataSource.removeAddFavMovie(movieId, false)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .ignoreElements()
            .subscribe()
    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }
}
