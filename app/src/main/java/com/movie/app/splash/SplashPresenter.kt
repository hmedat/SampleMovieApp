package com.movie.app.splash

import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SplashPresenter constructor(private val view: SplashActivityContractor.View)
    : SplashActivityContractor.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun subscribe() {
        Completable.complete()
                .delay(1, TimeUnit.SECONDS)
                .doOnComplete { view.startNextActivity(null) }
                .subscribe()
    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }
}
