package com.movie.app.details

import com.movie.app.api.ApiClient
import com.movie.app.di.scope.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers


@Module
abstract class DetailsActivityModule {

    @Binds
    abstract fun provideMainView(activity: DetailsMovieActivity): DetailsActivityContractor.View

    @Module
    companion object {
        @Provides
        @JvmStatic
        @ActivityScope
        internal fun provideDetailsPresenter(apiClient: ApiClient, mainView
        : DetailsActivityContractor.View): DetailsActivityContractor.Presenter {
            return DetailsMoviePresenter(Schedulers.io(), apiClient, mainView)
        }
    }
}