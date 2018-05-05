package com.movie.app.api

import com.movie.app.api.result.LatestMoviesResult
import com.movie.app.modules.Movie

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {
    @GET("discover/movie")
    fun getLatestMovies(@Query("page") pageNumber: Int): Observable<LatestMoviesResult>

    @GET("movie/{movieId}?append_to_response=videoResult,reviewResult")
    fun findMovie(@Path("movieId") id: Long): Observable<Movie>

}