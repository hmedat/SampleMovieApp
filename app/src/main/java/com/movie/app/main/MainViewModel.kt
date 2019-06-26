package com.movie.app.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.modules.MovieSortType
import com.movie.app.repositories.MovieDataSource
import com.movie.app.util.PaginationLiveDataResult
import com.movie.app.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MainViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val movieRepository: MovieDataSource,
    private val searchFilter: MovieSearchFilter
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var favStatusLiveData: MutableLiveData<HashSet<Long>> = MutableLiveData()
    private var searchResultLiveData: MutableLiveData<PaginationLiveDataResult<MoviesResult>> = MutableLiveData()

    fun getSearchResultLiveData(): LiveData<PaginationLiveDataResult<MoviesResult>> = searchResultLiveData
    fun getFavStatusLiveData(): LiveData<HashSet<Long>> = favStatusLiveData

    fun subscribe() {
        loadFirstPage()
    }

    fun loadFirstPage() {
        searchFilter.pageNumber = MovieSearchFilter.First_PAGE
        loadData()
    }

    fun loadNextPage() {
        loadData()
    }

    private fun loadData() {
        val isFirstPage = searchFilter.isFirstPage()
        if (isFirstPage) {
            searchResultLiveData.postValue(PaginationLiveDataResult.loading())
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
                            searchResultLiveData.postValue(PaginationLiveDataResult.noData())
                        }
                        return
                    }
                    if (result.isLoadMore()) {
                        searchResultLiveData.postValue(PaginationLiveDataResult.moreData(result))
                    } else {
                        searchResultLiveData.postValue(PaginationLiveDataResult.firstData(result))
                    }
                }

                override fun onError(throwable: Throwable) {
                    searchResultLiveData.postValue(PaginationLiveDataResult.error(throwable))
                }

                override fun onComplete() {
                    searchFilter.pageNumber = searchFilter.pageNumber + 1
                }
            })
    }

    fun onSearchFilterChanged(movieSortType: MovieSortType) {
        searchFilter.sortBy = movieSortType
        searchFilter.pageNumber = MovieSearchFilter.First_PAGE
        loadData()
    }

    fun addRemoveFavMovie(movie: Movie) {
        Observable.fromCallable {
            movieRepository.removeAddFavMovie(movie.id, movie.isFav)
        }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .ignoreElements()
            .subscribe()
    }

    fun syncFavouritesStatues() {
        compositeDisposable.add(movieRepository.getFavMovieIds()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe {
                favStatusLiveData.postValue(it)
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}
