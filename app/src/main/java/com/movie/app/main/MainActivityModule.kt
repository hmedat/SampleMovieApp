package com.movie.app.main

import com.movie.app.api.ApiClient
import com.movie.app.di.scope.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers


@Module
abstract class MainActivityModule {

    @Binds
    abstract fun provideMainView(mainActivity: MainActivity): MainActivityContractor.View

    @Module
    companion object {
        @Provides
        @JvmStatic
        @ActivityScope
        internal fun provideMainPresenter(apiClient: ApiClient, mainView: MainActivityContractor.View)
                : MainActivityContractor.Presenter {
            return MainPresenter(Schedulers.io(), apiClient, mainView)
        }
    }
}