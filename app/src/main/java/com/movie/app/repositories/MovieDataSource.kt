package com.movie.app.repositories

import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import com.movie.app.modules.MoviesResult

interface MovieDataSource {

    fun insertMovies(movies: List<Movie>)

    suspend fun getMovies(filter: MovieSearchFilter): MoviesResult?

    suspend fun getMovie(movieId: Long): Movie?

    suspend fun getFavMovies(): MoviesResult

    suspend fun getSimilarMovies(movieId: Long): MoviesResult?

    fun removeAddFavMovie(movieId: Long, isFav: Boolean): Boolean

    fun getFavMovieIds(): HashSet<Long>
}
