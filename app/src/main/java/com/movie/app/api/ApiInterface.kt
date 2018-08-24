package com.movie.app.api

import com.movie.app.api.result.MoviesResult
import com.movie.app.modules.Movie

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("discover/movie")
    fun getLatestMovies(
        @Query("page") pageNumber: Int
        , @Query("sort_by") sortBy: String
    ): Observable<MoviesResult>

    @GET("movie/{movieId}/similar")
    fun getSimilarMovies(@Path("movieId") id: Long): Observable<MoviesResult>

    @GET("movie/{movieId}?append_to_response=videos,reviews")
    fun findMovie(@Path("movieId") id: Long): Observable<Movie>
}
