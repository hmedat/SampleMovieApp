package com.movie.app.splash

import com.movie.app.di.scope.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
abstract class SplashActivityModule {

    @Binds
    abstract fun provideSplashView(splashActivity: SplashActivity): SplashActivityContractor.View

    @Module
    companion object {
        @Provides
        @JvmStatic
        @ActivityScope
        internal fun provideMainPresenter(mainView: SplashActivityContractor.View)
                : SplashActivityContractor.Presenter {
            return SplashPresenter(mainView)
        }

    }
}