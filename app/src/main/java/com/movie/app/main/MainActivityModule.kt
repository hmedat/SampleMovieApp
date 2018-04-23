package com.movie.app.main

import com.movie.app.di.scope.ActivityScope
import com.movie.app.interactors.IMoviesInteractor
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.util.schedulers.SchedulerProvider
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
        internal fun provideMainPresenter(schedulerProvider: SchedulerProvider
                                          , moviesInteractor: IMoviesInteractor
                                          , mainView: MainActivityContractor.View)
                : MainActivityContractor.Presenter {
            return MainPresenter(schedulerProvider, moviesInteractor, mainView, MovieSearchFilter())
        }

    }
}