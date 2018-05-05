package com.movie.app.repositories

import com.movie.app.modules.Movie
import io.reactivex.Observable

interface RemoteMovieRepository {

    fun getMovies(): Observable<List<Movie>>

    fun getMovie(movieId: Long): Observable<Movie>
}