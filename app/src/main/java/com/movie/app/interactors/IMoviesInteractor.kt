package com.movie.app.interactors

import com.movie.app.api.result.LatestMoviesResult
import com.movie.app.modules.Movie
import com.movie.app.modules.MovieSearchFilter
import io.reactivex.Observable

interface IMoviesInteractor {
    fun getLatest(filter: MovieSearchFilter): Observable<LatestMoviesResult>
    fun findMovie(movieId: Long): Observable<Movie>
}