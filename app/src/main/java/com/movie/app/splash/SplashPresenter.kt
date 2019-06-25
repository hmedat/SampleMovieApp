package com.movie.app.splash

import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SplashPresenter : SplashActivityContractor.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: SplashActivityContractor.View

    override fun bindView(view: SplashActivityContractor.View) {
        this.view = view
    }

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
