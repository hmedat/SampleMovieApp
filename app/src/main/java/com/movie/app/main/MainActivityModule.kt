package com.movie.app.main

import com.movie.app.RxSchedulers
import com.movie.app.api.ApiClient
import com.movie.app.di.scope.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
abstract class MainActivityModule {

    @Binds
    abstract fun provideMainView(mainActivity: MainActivity): MainActivityContractor.View

    @Module
    companion object {
        @Provides
        @JvmStatic
        @ActivityScope
        internal fun provideMainPresenter(apiClient: ApiClient, rxSchedulers: RxSchedulers
                                          , mainView: MainActivityContractor.View)
                : MainActivityContractor.Presenter {
            return MainPresenter(rxSchedulers, apiClient, mainView)
        }
    }
}