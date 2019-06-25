package com.movie.app.di

import com.movie.app.details.DetailsActivityContractor
import com.movie.app.details.DetailsMoviePresenter
import com.movie.app.fav.FavouritesActivityContractor
import com.movie.app.fav.FavouritesMoviesPresenter
import com.movie.app.main.MainActivityContractor
import com.movie.app.main.MainPresenter
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.splash.SplashActivityContractor
import com.movie.app.splash.SplashPresenter
import org.koin.dsl.module

val activityModule = module {
    factory<SplashActivityContractor.Presenter> { SplashPresenter() }
    factory<MainActivityContractor.Presenter> { MainPresenter(get(), get(), MovieSearchFilter()) }
    factory<DetailsActivityContractor.Presenter> { DetailsMoviePresenter(get(), get(), get()) }
    factory<FavouritesActivityContractor.Presenter> { FavouritesMoviesPresenter(get(), get()) }
}
