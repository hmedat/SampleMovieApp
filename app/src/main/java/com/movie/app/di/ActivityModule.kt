package com.movie.app.di

import com.movie.app.details.DetailsMovieViewModel
import com.movie.app.fav.FavouritesMoviesViewModel
import com.movie.app.main.MainViewModel
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val activityModule = module {
    viewModel { SplashViewModel() }
    viewModel { MainViewModel(get(), get(), MovieSearchFilter()) }
    viewModel { DetailsMovieViewModel(get(), get()) }
    viewModel { FavouritesMoviesViewModel(get(), get()) }
}
