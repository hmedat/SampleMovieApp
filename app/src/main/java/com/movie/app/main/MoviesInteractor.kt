package com.movie.app.main

import com.movie.app.RxSchedulers
import com.movie.app.api.ApiClient
import com.movie.app.api.result.LatestMoviesResult
import com.movie.app.mapper.MovieMapper
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MoviesInteractor(private val rxSchedulers: RxSchedulers, private val apiClient: ApiClient
                       , private val compositeDisposable: CompositeDisposable) {

    fun getLatest(filter: MovieSearchFilter, onSuccess: OnSuccessLatestMovies, onError: OnError) {
        apiClient.instance!!.getLatestMovies(filter.pageNumber)
                .map { result: LatestMoviesResult ->
                    MovieMapper.map(result.results!!)
                    result
                }
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.ui())
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
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.ui())
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

