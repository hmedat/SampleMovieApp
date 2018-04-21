package com.movie.app.main

import com.movie.app.RxSchedulers
import com.movie.app.api.ApiClient
import com.movie.app.api.result.LatestMoviesResult
import com.movie.app.modules.MovieSearchFilter
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainPresenter @Inject constructor(rxSchedulers: RxSchedulers, apiClient: ApiClient
                                        , mainView: MainActivityContractor.View)
    : MainActivityContractor.Presenter {
    private var moviesInteractor: MoviesInteractor
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val view: MainActivityContractor.View = mainView
    private val searchFilter: MovieSearchFilter = MovieSearchFilter()

    init {
        moviesInteractor = MoviesInteractor(rxSchedulers, apiClient, compositeDisposable)
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
        moviesInteractor.getLatest(searchFilter, object : MoviesInteractor.OnSuccessLatestMovies {
            override fun onSuccess(result: LatestMoviesResult) {
                if (result.results!!.isEmpty() && !result.isLoadMore()) {
                    view.showNoData()
                }
                view.showData(result)
                searchFilter.pageNumber++
                view.hideProgressBar()
            }
        }, object : MoviesInteractor.OnError {
            override fun onError(throwable: Throwable) {
                view.hideProgressBar()
                view.showError(isFirstPage, throwable)
            }
        })
    }


    override fun unSubscribe() {
        compositeDisposable.clear()
    }

}