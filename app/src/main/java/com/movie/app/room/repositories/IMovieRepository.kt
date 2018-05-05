package com.movie.app.room.repositories

import com.movie.app.modules.Movie

interface IMovieRepository {

    fun insertMovies(movies: List<Movie>)

    fun getMovies()

    fun getMovie(movieId: Long)
}