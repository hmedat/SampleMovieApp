package com.movie.app.fav

import com.movie.app.di.scope.ActivityScope
import com.movie.app.repositories.local.LocalMovieRepository
import com.movie.app.util.schedulers.BaseSchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class FavouritesActivityModule {

    @Binds
    abstract fun provideMainView(activity: FavouritesActivity): FavouritesActivityContractor.View

    @Module
    companion object {
        @Provides
        @JvmStatic
        @ActivityScope
        internal fun provideFavPresenter(
            schedulerProvider: BaseSchedulerProvider,
            movieDataSource: LocalMovieRepository,
            mainView: FavouritesActivityContractor.View
        ): FavouritesActivityContractor.Presenter {
            return FavouritesMoviesPresenter(schedulerProvider, movieDataSource, mainView)
        }
    }
}
