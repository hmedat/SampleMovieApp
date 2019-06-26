package com.movie.app.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private var subscribeLiveData: MutableLiveData<Any> = MutableLiveData()

    fun getSubscribeLiveData(): LiveData<Any> = subscribeLiveData

    fun subscribe() {
        viewModelScope.launch(Dispatchers.Main) {
            delay(1000)
            subscribeLiveData.postValue(true)
        }
    }
}
