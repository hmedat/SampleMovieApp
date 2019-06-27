package com.movie.app.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.modules.MovieSortType
import com.movie.app.repositories.MovieRepository
import com.movie.app.util.PaginationLiveDataResult
import com.movie.app.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val movieRepo: MovieRepository,
    private val searchFilter: MovieSearchFilter
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var favStatusLiveData: MutableLiveData<HashSet<Long>> = MutableLiveData()
    private var _result: MutableLiveData<PaginationLiveDataResult<MoviesResult>> = MutableLiveData()

    fun getResultLiveData(): LiveData<PaginationLiveDataResult<MoviesResult>> = _result
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
            _result.postValue(PaginationLiveDataResult.loading())
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (searchFilter.isFirstPage()) {
                    val localMovies = movieRepo.getLocalMovies(searchFilter)
                    _result.postValue(PaginationLiveDataResult.firstData(localMovies))
                }
                val result = movieRepo.getRemoteMovies(searchFilter)
                if (result?.isLoadMore() == true) {
                    _result.postValue(PaginationLiveDataResult.moreData(result))
                } else {
                    _result.postValue(PaginationLiveDataResult.firstData(result))
                }
                if (result?.isEmptyData() == true && !result.isLoadMore()) {
                    _result.postValue(PaginationLiveDataResult.noData())
                }
                searchFilter.increamentPage()
            } catch (e: Exception) {
                _result.postValue(PaginationLiveDataResult.error(e))
            }
        }
    }

    fun onSearchFilterChanged(movieSortType: MovieSortType) {
        searchFilter.sortBy = movieSortType
        searchFilter.pageNumber = MovieSearchFilter.First_PAGE
        loadData()
    }

    fun addRemoveFavMovie(movie: Movie) {
        Observable.fromCallable {
            movieRepo.removeAddFavMovie(movie.id, movie.isFav)
        }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .ignoreElements()
            .subscribe()
    }

    fun syncFavouritesStatues() {
        compositeDisposable.add(movieRepo.getFavMovieIds()
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
