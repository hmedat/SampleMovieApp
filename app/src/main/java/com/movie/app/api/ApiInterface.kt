package com.movie.app.api

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("discover/movie")
    fun getLatestMoviesAsync(
        @Query("page") pageNumber: Int,
        @Query("sort_by") sortBy: String
    ): Deferred<Response<MoviesResult>>

    @GET("movie/{movieId}/similar")
    fun getSimilarMovies(@Path("movieId") id: Long): Observable<MoviesResult>

    @GET("movie/{movieId}?append_to_response=videos,reviews")
    fun findMovie(@Path("movieId") id: Long): Observable<Movie>
}
