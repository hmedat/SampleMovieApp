package com.movie.app.details

import com.movie.app.api.ApiInterface
import com.movie.app.di.scope.ActivityScope
import com.movie.app.repositories.MovieDataSource
import com.movie.app.util.schedulers.BaseSchedulerProvider
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
        internal fun provideDetailsPresenter(
            schedulerProvider: BaseSchedulerProvider,
            movieDataSource: MovieDataSource,
            apiInterface: ApiInterface,
            mainView: DetailsActivityContractor.View
        ): DetailsActivityContractor.Presenter {
            return DetailsMoviePresenter(schedulerProvider, movieDataSource, apiInterface, mainView)
        }
    }
}