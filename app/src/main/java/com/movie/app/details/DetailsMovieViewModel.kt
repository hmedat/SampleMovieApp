package com.movie.app.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movie.app.api.ApiInterface
import com.movie.app.mapper.MovieMapper
import com.movie.app.modules.Movie
import com.movie.app.repositories.MovieDataSource
import com.movie.app.util.LiveDataResult
import com.movie.app.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DetailsMovieViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private var movieDataSource: MovieDataSource,
    private var apiInterface: ApiInterface
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var movieId: Long = 0
    private var movie: Movie? = null

    private var movieDetailsLiveData: MutableLiveData<LiveDataResult<Movie>> = MutableLiveData()
    fun getMovieDetailsLiveData(): LiveData<LiveDataResult<Movie>> = movieDetailsLiveData

    private var similarMoviesLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    fun getSimilarMoviesLiveData(): LiveData<List<Movie>> = similarMoviesLiveData

    fun setMovieId(movieId: Long) {
        this.movieId = movieId
    }

    fun subscribe() {
        loadData()
    }

    private fun loadData() {
        movieDetailsLiveData.postValue(LiveDataResult.loading())
        movieDataSource.getMovie(movieId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui(), true)
            .subscribe(object : Observer<Movie> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(item: Movie) {
                    movie = item
                    movieDetailsLiveData.postValue(LiveDataResult.success(movie))
                }

                override fun onError(throwable: Throwable) {
                    movieDetailsLiveData.postValue(LiveDataResult.error(throwable))
                }

                override fun onComplete() {
                    getSimilarMovies()
                }
            })
    }

    fun getSimilarMovies() {
        apiInterface.getSimilarMovies(movieId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .map { it.results }
            .map {
                MovieMapper.map(it)
                movieDataSource.insertMovies(it)
                it
            }
            .filter { it.isNotEmpty() }
            .subscribe(object : Observer<List<Movie>?> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(list: List<Movie>) {
                    similarMoviesLiveData.postValue(list)
                }

                override fun onError(throwable: Throwable) {
                }

                override fun onComplete() {
                }
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
