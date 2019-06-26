package com.movie.app.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SplashViewModel : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var subscribeLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getSubscribeLiveData(): LiveData<Boolean> = subscribeLiveData

    fun subscribe() {
        Completable.complete()
            .delay(1, TimeUnit.SECONDS)
            .doOnComplete { subscribeLiveData.postValue(true) }
            .subscribe()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
