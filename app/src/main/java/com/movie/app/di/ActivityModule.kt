package com.movie.app.di

import com.movie.app.details.DetailsActivityContractor
import com.movie.app.details.DetailsMoviePresenter
import com.movie.app.fav.FavouritesActivityContractor
import com.movie.app.fav.FavouritesMoviesPresenter
import com.movie.app.main.MainActivityContractor
import com.movie.app.main.MainPresenter
import com.movie.app.main.MainViewModel
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val activityModule = module {
    viewModel { SplashViewModel() }
    viewModel { MainViewModel(get(), get(), MovieSearchFilter()) }
    factory<DetailsActivityContractor.Presenter> { DetailsMoviePresenter(get(), get(), get()) }
    factory<FavouritesActivityContractor.Presenter> { FavouritesMoviesPresenter(get(), get()) }
}
