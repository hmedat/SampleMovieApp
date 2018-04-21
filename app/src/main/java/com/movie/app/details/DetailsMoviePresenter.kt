package com.movie.app.details

import android.os.Bundle
import com.movie.app.RxSchedulers
import com.movie.app.api.ApiClient
import com.movie.app.main.MoviesInteractor
import com.movie.app.modules.Movie
import io.reactivex.disposables.CompositeDisposable

class DetailsMoviePresenter(rxSchedulers: RxSchedulers, apiClient: ApiClient
                            , mainView: DetailsActivityContractor.View)
    : DetailsActivityContractor.Presenter {

    private var moviesInteractor: MoviesInteractor
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val view: DetailsActivityContractor.View = mainView
    private lateinit var movie: Movie

    init {
        moviesInteractor = MoviesInteractor(rxSchedulers, apiClient, compositeDisposable)
    }

    override fun bindBundles(extras: Bundle) {
        movie = extras.getParcelable(DetailsMovieActivity.EXTRA_MOVIE)
    }

    override fun subscribe() {
        loadData()
    }

    private fun loadData() {
        view.showProgressBar()
        moviesInteractor.findMovie(movie.id, object : MoviesInteractor.OnSuccessMovie {
            override fun onSuccess(movie: Movie) {
                view.hideProgressBar()
                view.showData(movie)
            }
        }, object : MoviesInteractor.OnError {
            override fun onError(throwable: Throwable) {
                view.hideProgressBar()
                view.showError(throwable)
            }
        })
    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }

}