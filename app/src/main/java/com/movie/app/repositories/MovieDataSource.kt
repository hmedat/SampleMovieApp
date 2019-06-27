package com.movie.app.repositories

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import io.reactivex.Observable

interface MovieDataSource {

    fun insertMovies(movies: List<Movie>)

    suspend fun getMovies(searchFilter: MovieSearchFilter): MoviesResult?

    fun getMovie(movieId: Long): Observable<Movie>

    suspend fun getFavMovies(): MoviesResult

    fun removeAddFavMovie(movieId: Long, isFav: Boolean): Boolean

    fun getFavMovieIds(): Observable<HashSet<Long>>
}
