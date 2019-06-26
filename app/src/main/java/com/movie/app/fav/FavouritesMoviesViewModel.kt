package com.movie.app.fav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movie.app.api.result.MoviesResult
import com.movie.app.repositories.MovieDataSource
import com.movie.app.util.LiveDataResult
import com.movie.app.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class FavouritesMoviesViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private var movieDataSource: MovieDataSource
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var resultLiveData: MutableLiveData<LiveDataResult<MoviesResult>> = MutableLiveData()

    fun getResultLiveData(): LiveData<LiveDataResult<MoviesResult>> = resultLiveData

    fun subscribe() {
        loadData()
    }

    private fun loadData() {
        resultLiveData.postValue(LiveDataResult.loading())
        movieDataSource.getFavMovies()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(object : Observer<MoviesResult> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(result: MoviesResult) {
                    resultLiveData.postValue(LiveDataResult.success(result))
                }

                override fun onError(throwable: Throwable) {
                    resultLiveData.postValue(LiveDataResult.error(throwable))
                }

                override fun onComplete() {
                }
            })
    }

    fun removeFromList(movieId: Long) {
        movieDataSource.removeAddFavMovie(movieId, false)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .ignoreElements()
            .subscribe()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
