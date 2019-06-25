package com.movie.app.main

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.modules.MovieSortType
import com.movie.app.repositories.MovieDataSource
import com.movie.app.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val schedulerProvider: BaseSchedulerProvider,
    private val movieRepository: MovieDataSource,
    private val searchFilter: MovieSearchFilter
) : MainActivityContractor.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: MainActivityContractor.View

    override fun bindView(view: MainActivityContractor.View) {
        this.view = view
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
        movieRepository.getMovies(searchFilter)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui(), true)
            .subscribe(object : Observer<MoviesResult> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(result: MoviesResult) {
                    if (result.isEmptyData()) {
                        if (!result.isLoadMore()) {
                            view.showNoData()
                        }
                        return
                    }
                    val results = result.results!!
                    if (result.isLoadMore()) {
                        view.showLoadMoreData(results)
                    } else {
                        view.showFirstData(results)
                    }
                    view.onDataCompleted(result.isFinished())
                }

                override fun onError(throwable: Throwable) {
                    view.hideProgressBar()
                    view.showError(isFirstPage, throwable)
                }

                override fun onComplete() {
                    searchFilter.pageNumber = searchFilter.pageNumber + 1
                    view.hideProgressBar()
                }
            })
    }

    override fun onSearchFilterChanged(movieSortType: MovieSortType) {
        searchFilter.sortBy = movieSortType
        searchFilter.pageNumber = MovieSearchFilter.First_PAGE
        loadData()
    }

    override fun addRemoveFavMovie(movie: Movie) {
        movieRepository.removeAddFavMovie(movie.id, movie.isFav)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .ignoreElements()
            .subscribe()
    }

    override fun syncFavouritesStatues() {
        movieRepository.getFavMovieIds()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe {
                view.updateFavouritesStatues(it)
                view.notifyVisibleItems()
            }
    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }
}
