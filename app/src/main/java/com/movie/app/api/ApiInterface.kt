package com.movie.app.api

import com.movie.app.modules.Movie
import com.movie.app.modules.MoviesResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("discover/movie")
    suspend fun getLatestMoviesAsync(
        @Query("page") pageNumber: Int,
        @Query("sort_by") sortBy: String
    ): MoviesResult

    @GET("movie/{movieId}/similar")
    suspend fun getSimilarMoviesAsync(@Path("movieId") id: Long): MoviesResult

    @GET("movie/{movieId}?append_to_response=videos,reviews")
    suspend fun findMovieAsync(@Path("movieId") id: Long): Movie
}
