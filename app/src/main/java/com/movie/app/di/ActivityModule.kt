package com.movie.app.di

import com.movie.app.details.DetailsMoviePresenter
import com.movie.app.fav.FavouritesMoviesPresenter
import com.movie.app.main.MainPresenter
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.splash.SplashPresenter
import org.koin.dsl.module

val activityModule = module {
    factory { SplashPresenter() }
    factory { MainPresenter(get(), get(), MovieSearchFilter()) }
    factory { DetailsMoviePresenter(get(), get(), get()) }
    factory { FavouritesMoviesPresenter(get(), get()) }
}
