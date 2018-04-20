package com.movie.app.main

import com.movie.app.api.ApiClient
import com.movie.app.api.result.LatestMoviesResult
import com.movie.app.mapper.MovieMapper
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter

import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MoviesInteractor(private val scheduler: Scheduler, private val apiClient: ApiClient
                       , private val compositeDisposable: CompositeDisposable) {

    fun getLatest(filter: MovieSearchFilter, onSuccess: OnSuccessLatestMovies, onError: OnError) {
        apiClient.instance!!.getLatestMovies(filter.pageNumber)
                .map { result: LatestMoviesResult ->
                    MovieMapper.map(result.results!!)
                    result
                }
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<LatestMoviesResult> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(result: LatestMoviesResult) {
                        onSuccess.onSuccess(result)
                    }

                    override fun onError(e: Throwable) {
                        onError.onError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }


    fun findMovie(movieId: Long, onSuccess: OnSuccessMovie, onError: OnError) {
        apiClient.instance!!.findMovie(movieId)
                .map { movie: Movie ->
                    MovieMapper.map(movie)
                    movie
                }
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Movie> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(movie: Movie) {
                        onSuccess.onSuccess(movie)
                    }

                    override fun onError(e: Throwable) {
                        onError.onError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }

    interface OnSuccessLatestMovies {
        fun onSuccess(result: LatestMoviesResult)

    }

    interface OnSuccessMovie {
        fun onSuccess(movie: Movie)

    }

    interface OnError {
        fun onError(throwable: Throwable)
    }


}

