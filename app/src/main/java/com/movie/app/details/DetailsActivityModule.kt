package com.movie.app.details

import com.movie.app.RxSchedulers
import com.movie.app.api.ApiInterface
import com.movie.app.di.scope.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
abstract class DetailsActivityModule {

    @Binds
    abstract fun provideMainView(activity: DetailsMovieActivity): DetailsActivityContractor.View

    @Module
    companion object {
        @Provides
        @JvmStatic
        @ActivityScope
        internal fun provideDetailsPresenter(apiInterface: ApiInterface, rxSchedulers: RxSchedulers
                                             , mainView: DetailsActivityContractor.View)
                : DetailsActivityContractor.Presenter {
            return DetailsMoviePresenter(rxSchedulers, apiInterface, mainView)
        }
    }
}