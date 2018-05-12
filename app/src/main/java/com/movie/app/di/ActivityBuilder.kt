package com.movie.app.di

import com.movie.app.details.DetailsActivityModule
import com.movie.app.details.DetailsMovieActivity
import com.movie.app.di.scope.ActivityScope
import com.movie.app.main.MainActivity
import com.movie.app.main.MainActivityModule
import com.movie.app.splash.SplashActivity
import com.movie.app.splash.SplashActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [(SplashActivityModule::class)])
    internal abstract fun bindSplashActivity(): SplashActivity


    @ActivityScope
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    internal abstract fun bindMainActivity(): MainActivity


    @ActivityScope
    @ContributesAndroidInjector(modules = [(DetailsActivityModule::class)])
    internal abstract fun bindDetailsMovieActivity(): DetailsMovieActivity

}