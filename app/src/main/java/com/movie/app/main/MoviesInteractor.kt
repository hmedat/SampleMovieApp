package com.movie.app.main

import com.movie.app.api.ApiClient
import com.movie.app.api.result.LatestMoviesResult
import com.movie.app.modules.MovieSearchFilter

import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MoviesInteractor(private val scheduler: Scheduler
                       , private val compositeDisposable: CompositeDisposable) {

    fun getLatest(filter: MovieSearchFilter, callBack: CallBack) {
        ApiClient.getInstance()
                .getLatestMovies(filter.pageNumber)
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<LatestMoviesResult> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(result: LatestMoviesResult) {
                        callBack.onSuccess(result)
                    }

                    override fun onError(e: Throwable) {
                        callBack.onError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }

    interface CallBack {
        fun onSuccess(result: LatestMoviesResult)

        fun onError(throwable: Throwable)
    }

}

