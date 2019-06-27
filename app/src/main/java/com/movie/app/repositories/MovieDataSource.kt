package com.movie.app.repositories

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter

interface MovieDataSource {

    fun insertMovies(movies: List<Movie>)

    suspend fun getMovies(searchFilter: MovieSearchFilter): MoviesResult?

    suspend fun getMovie(movieId: Long): Movie?

    suspend fun getFavMovies(): MoviesResult

    fun removeAddFavMovie(movieId: Long, isFav: Boolean): Boolean

    fun getFavMovieIds(): HashSet<Long>
}
