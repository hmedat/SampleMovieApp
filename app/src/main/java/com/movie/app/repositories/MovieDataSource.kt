package com.movie.app.repositories

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import io.reactivex.Observable

interface MovieDataSource {

    fun insertMovies(movies: List<Movie>)

    fun getMovies(searchFilter: MovieSearchFilter): Observable<MoviesResult>

    fun getMovie(movieId: Long): Observable<Movie>

    fun getFavMovies(): Observable<MoviesResult>

    fun removeAddFavMovie(movieId: Long, isFav: Boolean): Observable<Boolean>

    fun getFavMovieIds(): Observable<HashSet<Long>>
}
